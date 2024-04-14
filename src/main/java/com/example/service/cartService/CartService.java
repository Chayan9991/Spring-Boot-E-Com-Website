package com.example.service.cartService;

import com.example.entity.Cart;

import java.util.List;

public interface CartService {
    public Cart addToCart(Cart cart);

    List<Cart> getCart();
}
