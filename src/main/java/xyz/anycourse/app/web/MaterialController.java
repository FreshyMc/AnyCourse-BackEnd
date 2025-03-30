package xyz.anycourse.app.web;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.*;
import xyz.anycourse.app.service.contract.MaterialService;

import java.io.IOException;

@RequestMapping("/api/material")
@RestController
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping("/create")
    public MaterialCreatedDTO createMaterial(
        @RequestBody @Valid MaterialCreationDTO materialCreationDTO,
        Authentication authentication
    ) {
        return materialService.createMaterial(materialCreationDTO, authentication);
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<MaterialUploadSuccessDTO> uploadMaterial(
        @PathVariable(name = "id") String materialId,
        @RequestParam("file") MultipartFile fileChunk,
        @RequestParam("chunkNumber") int chunkNumber,
        @RequestParam("totalChunks") int totalChunks,
        Authentication authentication
    ) {
        materialService.uploadMaterialByChunk(materialId, fileChunk, chunkNumber, totalChunks, authentication);

        return ResponseEntity.ok().body(new MaterialUploadSuccessDTO(materialId, chunkNumber, totalChunks));
    }

    @PostMapping("/thumbnail/{id}")
    public ResponseEntity<MaterialUploadSuccessDTO> uploadThumbnail(
        @PathVariable(name = "id") String materialId,
        @RequestParam("file") MultipartFile fileChunk,
        @RequestParam("chunkNumber") int chunkNumber,
        @RequestParam("totalChunks") int totalChunks,
        Authentication authentication
    ) throws IOException, InterruptedException {
        materialService.uploadThumbnailByChunk(materialId, fileChunk, chunkNumber, totalChunks, authentication);

        return ResponseEntity.ok().body(new MaterialUploadSuccessDTO(materialId, chunkNumber, totalChunks));
    }

    @GetMapping("/retrieve/{id}")
    public MaterialDTO getMaterial(
        @PathVariable(name = "id") String materialId,
        Authentication authentication
    ) {
        return materialService.getMaterial(materialId, authentication);
    }

    @GetMapping("/all")
    public PaginatedDTO<MaterialDTO> getAllMaterials(
        @RequestParam(name = "shop_id") String shopId,
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "15") Integer size,
        Authentication authentication
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        return materialService.getAllMaterialsByShop(shopId, authentication, pageable);
    }

    @GetMapping("/thumbnail/{id}")
    public ResponseEntity<byte[]> getThumbnail(
        @PathVariable String id
    ) {
        FileDTO thumbnail = materialService.getMaterialThumbnail(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, thumbnail.getType())
                .body(thumbnail.getContent());
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<Resource> getStream(
        @PathVariable(name = "id") String materialId,
        Authentication authentication
    ) {
        Resource resource = materialService.getMaterialStream(materialId, authentication);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl")
                .body(resource);
    }
}
