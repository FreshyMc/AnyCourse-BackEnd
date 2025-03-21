package xyz.anycourse.app.service.contract;

import org.springframework.data.domain.Pageable;
import xyz.anycourse.app.domain.dto.PaginatedDTO;
import xyz.anycourse.app.domain.dto.TagCreationDTO;
import xyz.anycourse.app.domain.dto.TagDTO;
import xyz.anycourse.app.domain.entity.Tag;

import java.util.Set;

public interface TagService {
    PaginatedDTO<TagDTO> getTags(Pageable pageable);

    PaginatedDTO<TagDTO> getTags(String search, Pageable pageable);

    TagDTO createTag(TagCreationDTO tagCreationDTO);

    void deleteTag(String id);
}
