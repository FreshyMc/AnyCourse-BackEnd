package xyz.anycourse.app.web;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.MaterialCreatedDTO;
import xyz.anycourse.app.domain.dto.MaterialCreationDTO;
import xyz.anycourse.app.domain.dto.MaterialDTO;
import xyz.anycourse.app.domain.dto.MaterialUploadSuccessDTO;
import xyz.anycourse.app.service.contract.MaterialService;

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

    @GetMapping("/retrieve/{id}")
    public MaterialDTO getMaterial(
        @PathVariable(name = "id") String materialId,
        Authentication authentication
    ) {
        return materialService.getMaterial(materialId, authentication);
    }
}
