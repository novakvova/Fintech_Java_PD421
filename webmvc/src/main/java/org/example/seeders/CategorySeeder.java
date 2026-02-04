package org.example.seeders;

import com.github.slugify.Slugify;
import org.example.entities.CategoryEntity;
import org.example.repositories.ICategoryRepository;
import org.example.services.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategorySeeder implements CommandLineRunner {

    private final ICategoryRepository categoryRepository;
    private final Slugify slg = Slugify.builder().transliterator(true).build();
    private final FileService fileService;

    public CategorySeeder(ICategoryRepository categoryRepository, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    @Override
    public void run(String... args) {
        if(categoryRepository.count() == 0){
            CategoryEntity category1 = new CategoryEntity();
            category1.setName("Сало");
            String slug = slg.slugify(category1.getName());
            category1.setSlug(slug);
            var image = fileService.load("https://klopotenko.com/wp-content/uploads/2025/04/salo-varene-img-1000x600.jpg?v=1743596316");
            category1.setImage(image);

            CategoryEntity category2 = new CategoryEntity();
            category2.setName("Потужні праски");
            slug = slg.slugify(category2.getName());
            category2.setSlug(slug);
            image = fileService.load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJvr9OeVYWZw1sMFglb8hI-hmZ3Qe3FpX9RA&s");
            category2.setImage(image);

            categoryRepository.save(category1);
            categoryRepository.save(category2);
        }
    }
}