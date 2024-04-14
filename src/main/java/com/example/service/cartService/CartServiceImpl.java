package com.example.service.cartService;

import com.example.entity.Cart;
import com.example.repository.CartRepository;
import com.example.service.cartService.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart addToCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCart() {
        return cartRepository.findAll();
    }
}
