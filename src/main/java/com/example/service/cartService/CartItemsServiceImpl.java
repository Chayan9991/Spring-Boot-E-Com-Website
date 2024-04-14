package com.example.service.cartService;

import com.example.entity.CartItems;
import com.example.repository.CartItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemsServiceImpl implements CartItemsService{
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Override
    public CartItems saveCartItems(CartItems cartItems) {
        return cartItemsRepository.save(cartItems);
    }

    @Override
    public Optional<CartItems> getById(int id) {
        return cartItemsRepository.findById(id);
    }

    @Override
    public List<CartItems> getAllCartItems() {
        return cartItemsRepository.findAll();
    }


}
