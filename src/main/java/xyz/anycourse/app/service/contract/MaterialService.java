package xyz.anycourse.app.service.contract;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.*;

import java.io.IOException;

public interface MaterialService {
    MaterialCreatedDTO createMaterial(MaterialCreationDTO materialCreationDTO, Authentication authentication);

    void uploadMaterialByChunk(String materialId, MultipartFile fileChunk, int chunkNumber, int totalChunks, Authentication authentication) throws IOException, InterruptedException;

    MaterialDTO getMaterial(String materialId, Authentication authentication);

    PaginatedDTO<MaterialDTO> getAllMaterialsByShop(String shopId, Authentication authentication, Pageable pageable);

    void uploadThumbnailByChunk(String materialId, MultipartFile fileChunk, int chunkNumber, int totalChunks, Authentication authentication);

    FileDTO getMaterialThumbnail(String path);

    Resource getMaterialStream(String materialId, Authentication authentication);

    Resource serveFile(String fileName);

    PaginatedDTO<MaterialDTO> getRelatedMaterials(String materialId, Authentication authentication, Pageable pageable);
}
