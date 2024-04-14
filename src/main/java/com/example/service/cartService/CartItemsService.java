package com.example.service.cartService;

import com.example.entity.CartItems;

import java.util.List;
import java.util.Optional;

public interface CartItemsService {
    public CartItems saveCartItems(CartItems cartItems);

    public Optional<CartItems> getById(int id);

    public List<CartItems> getAllCartItems();
}
