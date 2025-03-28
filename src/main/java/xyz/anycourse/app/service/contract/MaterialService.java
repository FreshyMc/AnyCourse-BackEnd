package xyz.anycourse.app.service.contract;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.*;

public interface MaterialService {
    MaterialCreatedDTO createMaterial(MaterialCreationDTO materialCreationDTO, Authentication authentication);

    void uploadMaterialByChunk(String materialId, MultipartFile fileChunk, int chunkNumber, int totalChunks, Authentication authentication);

    MaterialDTO getMaterial(String materialId, Authentication authentication);

    PaginatedDTO<MaterialDTO> getAllMaterialsByShop(String shopId, Authentication authentication, Pageable pageable);

    void uploadThumbnailByChunk(String materialId, MultipartFile fileChunk, int chunkNumber, int totalChunks, Authentication authentication);

    FileDTO getMaterialThumbnail(String path);
}
