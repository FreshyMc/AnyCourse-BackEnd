package xyz.anycourse.app.service.contract;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.*;

public interface ShopService {
    ShopDTO create(ShopCreationDTO shopCreationDTO, Authentication authentication);

    ShopDTO update(ShopModificationDTO shopModificationDTO, Authentication authentication);

    ShopDTO findById(String id);

    ShopDTO changeThumbnail(String id, MultipartFile thumbnail, Authentication authentication);

    void followShop(String id, Authentication authentication);

    void unfollowShop(String id, Authentication authentication);

    FileDTO getThumbnail(String path);

    PaginatedDTO<ShopDTO> getShops(Integer page, Integer size, String name, String owner, String follower);
}
