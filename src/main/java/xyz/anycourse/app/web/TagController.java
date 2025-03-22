package xyz.anycourse.app.web;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import xyz.anycourse.app.domain.dto.PaginatedDTO;
import xyz.anycourse.app.domain.dto.TagCreationDTO;
import xyz.anycourse.app.domain.dto.TagDTO;
import xyz.anycourse.app.service.contract.TagService;

@RequestMapping("/api/tag")
@RestController
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/all")
    public PaginatedDTO<TagDTO> getTags(
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "15") Integer size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return tagService.getTags(pageRequest);
    }

    @GetMapping("/search")
    public PaginatedDTO<TagDTO> searchTags(
        @RequestParam(name = "search") String search,
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "15") Integer size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return tagService.getTags(search, pageRequest);
    }

    @PostMapping("/create")
    public TagDTO createTag(@RequestBody @Valid TagCreationDTO tagCreationDTO) {
        return tagService.createTag(tagCreationDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTag(@PathVariable String id) {
        tagService.deleteTag(id);
    }
}
