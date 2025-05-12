package org.wildcodeschool.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wildcodeschool.myblog.Service.CategoryService;
import org.wildcodeschool.myblog.dto.CategoryDTO;
import org.wildcodeschool.myblog.mapper.CategoryMapper;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setName("Category 2");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // Convert to DTO
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setName("Category 1");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setName("Category 2");

        when(categoryMapper.convertToDTO(category1)).thenReturn(categoryDTO1);
        when(categoryMapper.convertToDTO(category2)).thenReturn(categoryDTO2);

        // Act
        List<CategoryDTO> categories = categoryService.getAllCategories();

        // Assert
        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getName()).isEqualTo("Category 1");
        assertThat(categories.get(1).getName()).isEqualTo("Category 2");
    }

    @Test
    void testGetCategoryById_CategoryExists() {
        // Arrange
        Category category = new Category();
        category.setName("Category 1");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Convert to DTO
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setName("Category 1");

        when(categoryMapper.convertToDTO(category)).thenReturn(categoryDTO1);

        // Act
        CategoryDTO result = categoryService.getCategoryById(1L);

        // Assert
        assertThat(result.getName()).isEqualTo("Category 1");
    }

    @Test
    void testGetCategoryById_CategoryNotFound() {

        // Arrange
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoryService.getCategoryById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("La catégorie avec l'id 99 n'existe pas :(");
    }
}


