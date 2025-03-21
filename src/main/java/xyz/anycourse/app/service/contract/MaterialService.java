package xyz.anycourse.app.service.contract;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.MaterialCreatedDTO;
import xyz.anycourse.app.domain.dto.MaterialCreationDTO;
import xyz.anycourse.app.domain.dto.MaterialDTO;

public interface MaterialService {
    MaterialCreatedDTO createMaterial(MaterialCreationDTO materialCreationDTO, Authentication authentication);

    void uploadMaterialByChunk(String materialId, MultipartFile fileChunk, int chunkNumber, int totalChunks, Authentication authentication);

    MaterialDTO getMaterial(String materialId, Authentication authentication);
}
