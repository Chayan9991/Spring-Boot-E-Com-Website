package com.example.service;

import com.example.entity.Category;
import com.example.entity.Product;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);
    public boolean isProductPresent(int id);

    public List<Product> getAllProducts();
    public Product getProductById(int id);
    public boolean deleteProduct(int id);
}
