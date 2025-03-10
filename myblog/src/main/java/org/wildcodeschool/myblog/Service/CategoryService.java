package org.wildcodeschool.myblog.Service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.CategoryDTO;
import org.wildcodeschool.myblog.exception.CategoryNotFoundException;
import org.wildcodeschool.myblog.exception.ExceededMaxLengthException;
import org.wildcodeschool.myblog.mapper.CategoryMapper;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    //DTO for get all categories
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::convertToDTO).collect(Collectors.toList());
    }

    //DTO for get category by id
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("La catégorie avec l'id " + id +" n'existe pas :("));
        return categoryMapper.convertToDTO(category);
    }

    //DTO for create a category
    public CategoryDTO createCategory(Category category) {
        if (category.getName().length() > 50) {
            throw new ExceededMaxLengthException("Le nom ne peux pas dépasser 50 caractères");
        }
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(savedCategory);
    }

    //DTO for update a category
    public CategoryDTO updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("La catégorie avec l'id " + id +" n'existe pas :("));
        category.setName(categoryDetails.getName());
        if (categoryDetails.getName().length() > 50) {
            throw new ExceededMaxLengthException("Le nom ne peux pas dépasser 50 caractères");
        }
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(savedCategory);
    }

    //DTO for delete category
    public boolean deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("La catégorie avec l'id " + id + " n'existe pas :("));
        categoryRepository.delete(category);
        return true;
    }
}
