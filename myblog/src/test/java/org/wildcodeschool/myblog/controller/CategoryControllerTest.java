package org.wildcodeschool.myblog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.wildcodeschool.myblog.Service.CategoryService;
import org.wildcodeschool.myblog.Service.JwtService;
import org.wildcodeschool.myblog.dto.CategoryDTO;
import org.wildcodeschool.myblog.exception.CategoryNotFoundException;
import org.wildcodeschool.myblog.exception.GlobalExceptionHandler;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    @MockBean
    JwtService jwtService;

    @Test
    void testGetAllCategories() throws Exception {
        // Arrange
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setName("Category 1");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setName("Category 2");

        when(categoryService.getAllCategories()).thenReturn(List.of(categoryDTO1, categoryDTO2));

        // Act & Assert

        mockMvc
                .perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    @Test
    void testGetCategoryById_CategoryExists() throws Exception {
        // Arrange
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1L);
        categoryDTO1.setName("Category 1");

        when(categoryService.getCategoryById(1L)).thenReturn(categoryDTO1);

        // Act & Assert

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    void testGetCategoryById_CategoryNotFound() throws Exception {
        // Arrange
        when(categoryService.getCategoryById(99L)).thenThrow(new CategoryNotFoundException("La catégorie avec l'id 99 n'existe pas :("));

        // Act & Assert
        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isNotFound());
    }
}
