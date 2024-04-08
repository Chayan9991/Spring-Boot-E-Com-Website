package com.example.service;

import com.example.entity.Category;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface CategoryService {
    public Category saveCategory(Category category);

    public Boolean isCategoryExist(String name);
    public List<Category> getAllCategory();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

}
