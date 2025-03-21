package xyz.anycourse.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.UserPrincipal;
import xyz.anycourse.app.domain.dto.*;
import xyz.anycourse.app.domain.entity.Shop;
import xyz.anycourse.app.domain.entity.User;
import xyz.anycourse.app.exception.ForbiddenActionException;
import xyz.anycourse.app.exception.ResourceNotFoundException;
import xyz.anycourse.app.exception.UserNotFoundException;
import xyz.anycourse.app.repository.ShopRepository;
import xyz.anycourse.app.repository.UserRepository;
import xyz.anycourse.app.service.contract.FileStorageService;
import xyz.anycourse.app.service.contract.ShopService;
import xyz.anycourse.app.specification.ShopSpecification;
import xyz.anycourse.app.util.UserUtil;

import java.util.List;
import java.util.Objects;

@Service
public class ShopServiceImpl implements ShopService {

    private static final String THUMBNAIL_DIRECTORY = "/thumbnails";

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ShopServiceImpl(
            ShopRepository shopRepository,
            UserRepository userRepository,
            FileStorageService fileStorageService
    ) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ShopDTO create(ShopCreationDTO shopCreationDTO, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        User shopOwner = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + principal.getUserName()));

        Shop shop = new Shop();
        shop.setName(shopCreationDTO.getName());
        shop.setDescription(shopCreationDTO.getDescription());
        shop.setOwner(shopOwner);

        return new ShopDTO(shopRepository.save(shop));
    }

    @Override
    public ShopDTO update(ShopModificationDTO shopModificationDTO, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        User shopOwner = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + principal.getUserName()));

        Shop shop = shopRepository.findById(shopModificationDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        if (!isShopOwner(shop, shopOwner)) {
            throw new ForbiddenActionException("Not a shop owner");
        }

        shop.setName(shopModificationDTO.getName());
        shop.setDescription(shopModificationDTO.getDescription());

        return new ShopDTO(shopRepository.save(shop));
    }

    @Override
    public ShopDTO findById(String id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        return new ShopDTO(shop);
    }

    @Override
    public ShopDTO changeThumbnail(String shopId, MultipartFile thumbnail, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);

        User shopOwner = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + principal.getUserName()));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        if (!isShopOwner(shop, shopOwner)) {
            throw new ForbiddenActionException("Not a shop owner");
        }

        if (!Objects.isNull(shop.getThumbnail())) {
            fileStorageService.delete(shop.getThumbnail(), THUMBNAIL_DIRECTORY);
        }

        String uploadedFileName = fileStorageService.upload(thumbnail, THUMBNAIL_DIRECTORY);

        shop.setThumbnail(uploadedFileName);

        return new ShopDTO(shopRepository.save(shop));
    }

    @Override
    public void followShop(String id, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        shop.addFollower(user);

        shopRepository.save(shop);
    }

    @Override
    public void unfollowShop(String id, Authentication authentication) {
        UserPrincipal principal = UserUtil.extractUserPrincipalFromAuthentication(authentication);
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        shop.removeFollower(user);

        shopRepository.save(shop);
    }

    @Override
    public FileDTO getThumbnail(String path) {
        return fileStorageService.get(path, THUMBNAIL_DIRECTORY);
    }

    @Override
    public PaginatedDTO<ShopDTO> getShops(Integer page, Integer size, String name, String owner, String follower) {
        PageRequest pageable = PageRequest.of(page, size);
        Specification<Shop> shopSpecification = ShopSpecification.filterBy(name, owner, follower);

        Page<Shop> shops = shopRepository.findAll(shopSpecification, pageable);

        List<ShopDTO> content = shops.stream()
                .map(ShopDTO::new)
                .toList();

        return new PaginatedDTO<>(content, shops.getTotalPages(), shops.getTotalElements());
    }

    private boolean isShopOwner(Shop shop, User user) {
        return shop.getOwner().getEmail().equals(user.getEmail());
    }
}
