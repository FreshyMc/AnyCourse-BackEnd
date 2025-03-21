package xyz.anycourse.app.web;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.*;
import xyz.anycourse.app.service.contract.ShopService;

import java.util.Map;

@RequestMapping("/api/shop")
@RestController
public class ShopController {

    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/all")
    public PaginatedDTO<ShopDTO> getShops(
        @RequestParam(name = "follower_id", required = false) String follower,
        @RequestParam(name = "owner_id", required = false) String owner,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "15") Integer size,
        @RequestParam Map<String, String> requestParams
    ) {
        log.info(requestParams.toString());

        return shopService.getShops(page, size, name, owner, follower);
    }

    @GetMapping("/{id}")
    public ShopDTO getShop(@PathVariable String id) {
        return shopService.findById(id);
    }

    @PostMapping("/create")
    public ShopDTO createShop(
        @RequestBody @Valid ShopCreationDTO shopCreationDTO,
        Authentication authentication
    ) {
        return shopService.create(shopCreationDTO, authentication);
    }

    @PutMapping("/update")
    public ShopDTO updateShop(
        @RequestBody @Valid ShopModificationDTO shopModificationDTO,
        Authentication authentication
    ) {
        return shopService.update(shopModificationDTO, authentication);
    }

    @PostMapping("/change/thumbnail/{id}")
    public ShopDTO changeThumbnail(
        @PathVariable String id,
        @RequestPart(value = "thumbnail") MultipartFile thumbnail,
        Authentication authentication
    ) {
        return shopService.changeThumbnail(id, thumbnail, authentication);
    }

    @GetMapping("/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(
        @RequestParam("path") String path
    ) {
        FileDTO thumbnail = shopService.getThumbnail(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, thumbnail.getType())
                .body(thumbnail.getContent());
    }

    @PostMapping("/follow/{id}")
    public void followShop(
        @PathVariable String id,
        Authentication authentication
    ) {
        shopService.followShop(id, authentication);
    }

    @PostMapping("/unfollow/{id}")
    public void unfollowShop(
        @PathVariable String id,
        Authentication authentication
    ) {
        shopService.unfollowShop(id, authentication);
    }
}
