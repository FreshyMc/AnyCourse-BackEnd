package xyz.anycourse.app.service.contract;

import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.FileDTO;

public interface FileStorageService {
    String upload(MultipartFile file, String directory);

    boolean delete(String filename, String directory);

    FileDTO get(String path, String directory);

    void uploadChunk(MultipartFile fileChunk, int chunkNumber, String tempDir, String uploadDir);

    String reassembleFile(int totalChunks, String tempUploadDir, String fileExtension, String materialUploadFolder);
}
