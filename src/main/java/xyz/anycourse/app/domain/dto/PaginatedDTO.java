package xyz.anycourse.app.domain.dto;

import java.util.List;

public class PaginatedDTO<T> {
    private int page;
    private long total;
    private List<T> content;

    public PaginatedDTO() {
    }

    public PaginatedDTO(List<T> content, int page, long total) {
        this.page = page;
        this.total = total;
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
