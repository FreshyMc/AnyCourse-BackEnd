package xyz.anycourse.app.domain.dto;

public class MaterialUploadSuccessDTO {
    private String id;
    private int chunkNumber;
    private int totalChunks;

    public MaterialUploadSuccessDTO(String id, int chunkNumber, int totalChunks) {
        this.id = id;
        this.chunkNumber = chunkNumber;
        this.totalChunks = totalChunks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }
}
