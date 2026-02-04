package org.example.dtos.category;

import lombok.Data;

@Data
public class CategoryItemDTO {
    private Long id;
    private String name;
    private String image;
    private String slug;
}
