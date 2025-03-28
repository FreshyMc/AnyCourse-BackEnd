package xyz.anycourse.app.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.UserPrincipal;
import xyz.anycourse.app.domain.dto.*;
import xyz.anycourse.app.domain.entity.Material;
import xyz.anycourse.app.domain.entity.Shop;
import xyz.anycourse.app.domain.entity.Tag;
import xyz.anycourse.app.domain.entity.User;
import xyz.anycourse.app.domain.enumeration.MaterialTag;
import xyz.anycourse.app.exception.ForbiddenActionException;
import xyz.anycourse.app.exception.ResourceNotFoundException;
import xyz.anycourse.app.repository.MaterialRepository;
import xyz.anycourse.app.repository.ShopRepository;
import xyz.anycourse.app.repository.TagRepository;
import xyz.anycourse.app.repository.UserRepository;
import xyz.anycourse.app.service.contract.MaterialService;
import xyz.anycourse.app.util.UserUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {

    private static final String MATERIAL_CHUNKS_UPLOAD_FOLDER = "/material_chunks";
    private static final String MATERIAL_UPLOAD_FOLDER = "/materials";
    private static final String MATERIAL_THUMBNAIL_CHUNKS_UPLOAD_FOLDER = "/material_thumbnail_chunks";
    private static final String MATERIAL_THUMBNAIL_UPLOAD_FOLDER = "/material_thumbnails";

    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final TagRepository tagRepository;
    private final FileStorageServiceImpl fileStorageService;

    public MaterialServiceImpl(
            MaterialRepository materialRepository,
            UserRepository userRepository,
            ShopRepository shopRepository,
            TagRepository tagRepository,
            FileStorageServiceImpl fileStorageService
    ) {
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
        this.shopRepository = shopRepository;
        this.tagRepository = tagRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public MaterialCreatedDTO createMaterial(MaterialCreationDTO materialCreationDTO, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Shop shop = shopRepository.findByIdAndOwner(materialCreationDTO.getShopId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        Material material = new Material();
        material.setShop(shop);
        material.setTitle(materialCreationDTO.getTitle());
        material.setDescription(materialCreationDTO.getDescription());
        tagMaterial(materialCreationDTO, material);

        return new MaterialCreatedDTO(materialRepository.save(material));
    }

    @Override
    public void uploadMaterialByChunk(
            String materialId,
            MultipartFile fileChunk,
            int chunkNumber,
            int totalChunks,
            Authentication authentication
    ) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));

        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        checkMaterialOwner(material, principal);

        String tempUploadDir = "/tempDir-" + materialId;

        fileStorageService.uploadChunk(fileChunk, chunkNumber, tempUploadDir, MATERIAL_CHUNKS_UPLOAD_FOLDER);

        if (chunkNumber == totalChunks) {
            String fileExtension = FilenameUtils.getExtension(fileChunk.getOriginalFilename());

            String filePath = fileStorageService.reassembleFile(totalChunks, MATERIAL_CHUNKS_UPLOAD_FOLDER + tempUploadDir, fileExtension, MATERIAL_UPLOAD_FOLDER);

            material.setLocation(filePath);
            materialRepository.save(material);
        }
    }

    @Override
    public MaterialDTO getMaterial(String materialId, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));

        return new MaterialDTO(material);
    }

    @Override
    public PaginatedDTO<MaterialDTO> getAllMaterialsByShop(String shopId, Authentication authentication, Pageable pageable) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        Page<Material> materials = materialRepository.findAllByShop_Id(shopId, pageable);

        List<MaterialDTO> content = materials.stream()
                .map(MaterialDTO::new)
                .toList();

        return new PaginatedDTO<>(content, materials.getTotalPages(), materials.getTotalElements());
    }

    @Override
    public void uploadThumbnailByChunk(
        String materialId,
        MultipartFile fileChunk,
        int chunkNumber,
        int totalChunks,
        Authentication authentication
    ) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));

        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        checkMaterialOwner(material, principal);

        String tempUploadDir = "/tempDir-" + materialId;

        fileStorageService.uploadChunk(fileChunk, chunkNumber, tempUploadDir, MATERIAL_THUMBNAIL_CHUNKS_UPLOAD_FOLDER);

        if (chunkNumber == totalChunks) {
            String fileExtension = FilenameUtils.getExtension(fileChunk.getOriginalFilename());

            String filePath = fileStorageService.reassembleFile(totalChunks, MATERIAL_THUMBNAIL_CHUNKS_UPLOAD_FOLDER + tempUploadDir, fileExtension, MATERIAL_THUMBNAIL_UPLOAD_FOLDER);

            material.setThumbnail(filePath);
            materialRepository.save(material);
        }
    }

    @Override
    public FileDTO getMaterialThumbnail(String id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found."));

        String thumbnail = Optional.ofNullable(material.getThumbnail())
                .orElseThrow(() -> new ResourceNotFoundException("Thumbnail not found."));

        return fileStorageService.get(thumbnail, MATERIAL_THUMBNAIL_UPLOAD_FOLDER);
    }

    private void checkMaterialOwner(Material material, UserPrincipal principal) {
        if (!material.getShop().getOwner().getId().equals(principal.getUserId())) {
            throw new ForbiddenActionException("Not authorized to upload material");
        }
    }

    private void tagMaterial(MaterialCreationDTO materialCreationDTO, Material material) {
        if (materialCreationDTO.getTags().isEmpty()) {
            Tag tag = tagRepository.findByName(MaterialTag.UNTAGGED.getName())
                    .orElseThrow(() -> new ResourceNotFoundException("Material tag not found"));

            material.addTag(tag);
        } else {
            Set<String> tagIds = materialCreationDTO.getTags().stream()
                    .map(MaterialTagDTO::getId)
                    .collect(Collectors.toSet());

            Set<Tag> tags = tagRepository.findByIdIn(tagIds);

            tags.forEach(tag -> material.addTag(tag));
        }
    }
}
