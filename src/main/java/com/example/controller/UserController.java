package com.example.controller;

import com.example.entity.*;
import com.example.service.cartService.CartItemsService;
import com.example.service.cartService.CartService;
import com.example.service.CategoryService;
import com.example.service.ProductService;
import com.example.service.userService.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/shop")
    public String shop(Model model, HttpSession session) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("getAllProducts", productList);
        List<Category> category = categoryService.getAllCategory();
        model.addAttribute("getAllCategory", category);
        return "shop";
    }

    @GetMapping("/view-product/{id}")
    public String product(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        Category category = categoryService.getCategoryById(product.getCategoryId());
        model.addAttribute("product", product);
        model.addAttribute("category", category);
        return "view-product";
    }

    @GetMapping("/add_to_cart/{id}")
    public String addToCart(@PathVariable int id, HttpSession session, Model model) {
        Product product = productService.getProductById(id);

        //Dummy User is already present in db for now
        Optional<User> loggedUser = userService.getUserById(2);
        session.setAttribute("currentUser", loggedUser.get());

        // Retrieve the currently logged-in user from the session
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            // Handle case where user is not logged in
            return "redirect:/login"; // Redirect to the login page or handle appropriately
        }

        // Create a cart if the user doesn't have one
        if (currentUser.getCart() == null) {
            System.out.println("Creating a Cart for: " + currentUser.getFullname());
            Cart cart = new Cart();
            cart.setUser(currentUser);
            cart.setCartItems(new ArrayList<>());
            cartService.addToCart(cart);
        }

        // Retrieve all carts from the database
        List<Cart> allCarts = cartService.getCart();

        // Find the cart associated with the current user
        Cart currentUserCart = allCarts.stream()
                .filter(cart -> cart.getUser().getUserId() == currentUser.getUserId())
                .findFirst()
                .orElse(null);

        if (currentUserCart != null) {
            // <---- Current User's Cart Found Now Add Items to Cart ---->

            // Cart Items Must be Unique Else Items would not be stored into Cart

            CartItems cartItems = new CartItems();

            //Check Whether Product is Present in the CartItems List
            boolean isProductAlreadyInCart = currentUserCart.getCartItems().stream()
                    .anyMatch(item -> item.getProduct().getId() == product.getId());

            if (isProductAlreadyInCart) {
                System.out.println("Product is Already Added...");
            } else {
                cartItems.setCart(currentUserCart);
                cartItems.setProduct(product);
                cartItems.setQuantity(1);  // By default, Prod Quantity is 1.
                CartItems savedItem = cartItemsService.saveCartItems(cartItems);
                System.out.println("Product Is Added Successfully...");
            }

        } else {
            // Handle case where the cart for the current user was not found
            System.out.println("Cart not found for user: " + currentUser.getFullname());
        }


        return "redirect:/view-product/" + product.getId();

    }

    @GetMapping("/view_cart")
    public String viewCart(HttpSession session, Model model) {

        return "view_cart";
    }
}
