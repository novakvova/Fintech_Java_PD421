package org.example.mappers;

import org.example.dtos.category.CategoryItemDTO;
import org.example.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryItemDTO toDto(CategoryEntity category);
}
