package xyz.anycourse.app.domain.dto;

public class FileDTO {
    private byte[] content;
    private String fileType;

    public FileDTO() {
    }

    public FileDTO(byte[] content, String fileType) {
        this.content = content;
        this.fileType = fileType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getType() {
        return fileType != null ? fileType : "application/octet-stream";
    }
}
