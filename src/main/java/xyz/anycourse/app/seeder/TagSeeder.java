package xyz.anycourse.app.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xyz.anycourse.app.domain.entity.Tag;
import xyz.anycourse.app.domain.enumeration.MaterialTag;
import xyz.anycourse.app.repository.TagRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class TagSeeder implements CommandLineRunner {

    private final TagRepository tagRepository;

    public TagSeeder(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedTags();
    }

    private void seedTags() {
        if (tagRepository.count() == 0) {
            List<Tag> tags = Arrays.stream(MaterialTag.values()).map(tag -> {
                Tag tagEntity = new Tag();
                tagEntity.setName(tag.name());

                return tagEntity;
            }).toList();

            tagRepository.saveAll(tags);
        }
    }
}
