package xyz.anycourse.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.anycourse.app.domain.dto.PaginatedDTO;
import xyz.anycourse.app.domain.dto.TagCreationDTO;
import xyz.anycourse.app.domain.dto.TagDTO;
import xyz.anycourse.app.domain.entity.Tag;
import xyz.anycourse.app.repository.TagRepository;
import xyz.anycourse.app.service.contract.TagService;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public PaginatedDTO<TagDTO> getTags(Pageable pageable) {
        Page<Tag> tagsPage = tagRepository.findAll(pageable);

        List<TagDTO> content = tagsPage.getContent().stream()
                .map(TagDTO::new)
                .toList();

        return new PaginatedDTO<>(content, tagsPage.getTotalPages(), tagsPage.getTotalElements());
    }

    @Override
    public PaginatedDTO<TagDTO> getTags(String search, Pageable pageable) {
        Page<Tag> tagsPage = tagRepository.findByNameContaining(search, pageable);

        List<TagDTO> content = tagsPage.getContent().stream()
                .map(TagDTO::new)
                .toList();

        return new PaginatedDTO<>(content, tagsPage.getTotalPages(), tagsPage.getTotalElements());
    }

    @Override
    public TagDTO createTag(TagCreationDTO tagCreationDTO) {
        Optional<Tag> tagOptional = tagRepository.findByName(tagCreationDTO.getName());

        if (tagOptional.isPresent()) {
            return new TagDTO(tagOptional.get());
        }

        Tag tag = new Tag();
        tag.setName(tagCreationDTO.getName());

        return new TagDTO(tagRepository.save(tag));
    }

    @Override
    public void deleteTag(String id) {
        tagRepository.deleteById(id);
    }
}
