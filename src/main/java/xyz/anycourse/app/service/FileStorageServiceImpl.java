package xyz.anycourse.app.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.anycourse.app.domain.dto.FileDTO;
import xyz.anycourse.app.exception.StorageException;
import xyz.anycourse.app.service.contract.FileStorageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;

    public FileStorageServiceImpl() {
        this.rootLocation = Paths.get("/uploads");
    }

    @Override
    public String upload(MultipartFile file, String directory) {
        try {
            Path storePath = Paths.get(rootLocation.toString() + directory);

            if (!storePath.toFile().exists()) {
                new File(rootLocation + directory).mkdirs();
            }

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }

            String fileName = UUID.randomUUID().toString();

            String fileExtension = getFileExtension(file);

            Path destinationFile = storePath.resolve(
                    Paths.get(fileName + fileExtension).normalize()
            ).toAbsolutePath();

            if (!destinationFile.getParent().equals(storePath.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return FilenameUtils.getName(destinationFile.toString());
        } catch (IOException ex) {
            throw new StorageException("Failed to store file");
        }
    }

    @Override
    public boolean delete(String filename, String directory) {
        try {
            Path storePath = Paths.get(rootLocation.toString() + directory + "/" + filename);

            return Files.deleteIfExists(storePath);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file");
        }
    }

    @Override
    public FileDTO get(String fileName, String directory) {
        try {
            Path filePath = Paths.get(rootLocation.toString() + directory).resolve(fileName).normalize();

            byte[] content = Files.readAllBytes(filePath);
            String type = Files.probeContentType(filePath);

            return new FileDTO(content, type);
        } catch (IOException e) {
            throw new StorageException("Failed to read file");
        }
    }

    @Override
    public void uploadChunk(MultipartFile fileChunk, int chunkNumber, String tempDir, String uploadDirectory) {
        // Resolve the directory path properly
        Path storePath = Paths.get(rootLocation.toString(), uploadDirectory, tempDir);

        // Ensure the directory exists
        if (!Files.exists(storePath)) {
            try {
                Files.createDirectories(storePath);
            } catch (IOException e) {
                throw new StorageException("Failed to create directories: " + storePath);
            }
        }

        if (fileChunk.isEmpty()) {
            throw new StorageException("Failed to store empty material chunk");
        }

        // Construct the chunk file path
        Path chunkFilePath = storePath.resolve(String.valueOf(chunkNumber));
        File chunkFile = chunkFilePath.toFile();

        try {
            if (!chunkFile.exists() && !chunkFile.createNewFile()) {
                throw new IOException("Could not create file: " + chunkFilePath);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to create new file at: " + chunkFilePath);
        }

        try (FileOutputStream fos = new FileOutputStream(chunkFile, true)) {
            fos.write(fileChunk.getBytes());
        } catch (IOException ex) {
            throw new StorageException("Failed to store chunk in file: " + chunkFilePath);
        }
    }

    @Override
    public String reassembleFile(int totalChunks, String tempUploadDir, String fileExtension, String materialUploadFolder) {
        // Ensure the main storage directory exists
        Path storePath = Paths.get(rootLocation.toString(), materialUploadFolder);

        try {
            Files.createDirectories(storePath);
        } catch (IOException e) {
            throw new StorageException("Failed to create storage directory: " + storePath);
        }

        // Generate a unique file name
        final String fileName = String.format("%s.%s", UUID.randomUUID().toString(), fileExtension);
        Path assembledFilePath = storePath.resolve(fileName);
        File assembledFile = assembledFilePath.toFile();

        // Reassemble chunks
        try (FileOutputStream fos = new FileOutputStream(assembledFile, true)) {
            for (int i = 1; i <= totalChunks; i++) {
                Path chunkPath = Paths.get(rootLocation.toString(), tempUploadDir, String.valueOf(i));

                if (!Files.exists(chunkPath)) {
                    throw new StorageException("Chunk file not found: " + chunkPath);
                }

                byte[] chunkData = Files.readAllBytes(chunkPath);
                fos.write(chunkData);
                Files.delete(chunkPath); // Delete chunk after appending
            }
        } catch (IOException e) {
            throw new StorageException("Failed to reassemble file: " + assembledFilePath);
        }

        Path tempDirPath = Paths.get(rootLocation.toString(), tempUploadDir);

        try {
            Files.delete(tempDirPath);
        } catch (IOException e) {
            throw new StorageException("Failed to delete temp upload directory: " + tempDirPath);
        }

        return assembledFile.getAbsolutePath();
    }

    private String getFileExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new StorageException("Failed to store file with empty file name");
        }

        int index = file.getOriginalFilename().lastIndexOf(".");

        return file.getOriginalFilename().substring(index);
    }
}
