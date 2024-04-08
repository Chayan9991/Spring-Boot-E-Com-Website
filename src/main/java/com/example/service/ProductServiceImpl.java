package com.example.service;

import com.example.entity.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean isProductPresent(int id){
        Product product =  productRepository.findById(id);
        if(product != null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean deleteProduct(int id) {
        Product product = productRepository.findById(id);
        if(product != null){
            productRepository.delete(product);
            return true;
        }
        else{
            return false;
        }
    }
}
