package com.example.controller;

import com.example.entity.Category;
import com.example.entity.Product;
import com.example.repository.ProductRepository;
import com.example.service.CategoryService;
import com.example.service.CategoryServiceImpl;
import com.example.service.CommonService;
import com.example.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommonService commonService;

    @GetMapping("")
    public String admin() {
        return "./admin_dashboard/admin";
    }


    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "./admin_dashboard/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();

        String categoryFile = category.getName().toLowerCase();

        // Replace spaces with underscores
        String formattedCategoryName = categoryFile.replaceAll(" ", "_");
        formattedCategoryName = formattedCategoryName.replaceAll("[^a-zA-Z0-9_]", "");

        String imageName = file != null ? formattedCategoryName.concat(extension) : "NA";

        category.setImageName(imageName);

        // Clear any existing messages before processing the form submission
        session.removeAttribute("successMsg");
        session.removeAttribute("errorMsg");
        if (!categoryService.isCategoryExist(category.getName())) {
            Category savedCategory = categoryService.saveCategory(category);


            if (savedCategory == null) {
                session.setAttribute("errorMsg", "Not Saved ! Internal Server Error");
            } else {
                try {
                    // Get the directory path
                    Path directoryPath = Paths.get("src/main/resources/static/images/category_images");

                    // Create the directory if it doesn't exist
                    if (!Files.exists(directoryPath)) {
                        Files.createDirectories(directoryPath);
                    }
                    // Get the destination file path
                    Path destinationPath = directoryPath.resolve(imageName);

                    // Copy the file to the destination path
                    Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied successfully to: " + destinationPath);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                session.setAttribute("successMsg", "Saved Successfully");
            }
        } else {
            session.setAttribute("errorMsg", "Category Already Exist!!!");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        Boolean delete = categoryService.deleteCategory(id);

        if (delete) {
            session.setAttribute("successMsg", "Deleted Successfully");
        } else {
            session.setAttribute("errorMsg", "Something Went Wrong");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/updateCategory/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Category singleCategory = categoryService.getCategoryById(id);
        model.addAttribute("category", singleCategory);
        return "./admin_dashboard/updateCategory";
    }

    @PostMapping("/updateCategory")
    public String handleUpdateCategory(@ModelAttribute Category category, HttpSession session, @RequestParam("file") MultipartFile file) {
        Category singleCategory = categoryService.getCategoryById(category.getId());

        String imageName;

        if (file != null && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
            String categoryFile = category.getName().toLowerCase();

            // Replace spaces with underscores
            String formattedCategoryName = categoryFile.replaceAll(" ", "_");
            String finalString = formattedCategoryName.replaceAll("[^a-zA-Z0-9_]", "");

            imageName = finalString.concat(extension);
        } else {
            imageName = singleCategory.getImageName();
        }

        System.out.println(imageName);
        // Update the category attributes with the new data
        singleCategory.setImageName(imageName);
        singleCategory.setIsActive(category.getIsActive());
        singleCategory.setName(category.getName());

        Category updateCategory = categoryService.saveCategory(singleCategory);

        if (updateCategory == null) {
            session.setAttribute("errorMsg", "Not Saved ! Internal Server Error");
        } else {
            try {
                // Get the directory path
                Path directoryPath = Paths.get("src/main/resources/static/images/category_images");

                // Create the directory if it doesn't exist
                if (!Files.exists(directoryPath)) {
                    Files.createDirectories(directoryPath);
                }
                // Get the destination file path
                Path destinationPath = directoryPath.resolve(imageName);

                // Copy the file to the destination path
                Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File copied successfully to: " + destinationPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
            session.setAttribute("successMsg", "Updated Successfully");
        }

        return "redirect:/admin/category";

    }

    //Product Related Code
    @GetMapping("/add_product")
    public String getAddProduct(Model model) {
        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("categoryList", categoryList);
        return "./admin_dashboard/add_product";
    }

    @PostMapping("/add_product")
    public String addProduct(@ModelAttribute Product product, HttpSession session, @RequestParam("file") MultipartFile file) {

        String originalProductName = file.getOriginalFilename();
        assert originalProductName != null;
        String fileExtension = originalProductName.substring(originalProductName.lastIndexOf(".")).toLowerCase();
        String productName = product.getName().toLowerCase();
        // Replace spaces with underscores
        String formattedProductName = productName.replaceAll(" ", "_");
        formattedProductName = formattedProductName.replaceAll("[^a-zA-Z0-9_]", "");
        String finalFileName = formattedProductName + fileExtension;

        String productImageName = file != null ? finalFileName : "NA";

        product.setProductImageName(productImageName);
        product.setDiscount(product.getDiscount());
        product.setDiscountedPrice(product.getPrice());
        // Clear any existing messages before processing the form submission
        session.removeAttribute("successMsg");
        session.removeAttribute("errorMsg");

        if (!productService.isProductPresent(product.getId())) {

            Product savedProduct = productService.saveProduct(product);

            if (savedProduct == null) {
                session.setAttribute("errorMsg", "Not Saved ! Internal Server Error");
            } else {
                try {
                    // Get the directory path
                    Path directoryPath = Paths.get("src/main/resources/static/images/product_images");

                    // Create the directory if it doesn't exist
                    if (!Files.exists(directoryPath)) {
                        Files.createDirectories(directoryPath);
                    }
                    // Get the destination file path
                    Path destinationPath = directoryPath.resolve(productImageName);
                    System.out.println(destinationPath);
                    // Copy the file to the destination path
                    Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied successfully to: " + destinationPath);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                session.setAttribute("successMsg", "Saved Successfully");
            }
        } else {
            session.setAttribute("errorMsg", "Category Already Exist!!!");
        }

        return "redirect:/admin/add_product";
    }

    @GetMapping("/view_products")
    public String viewProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        List<Category> getProductCategory = categoryService.getAllCategory();
        model.addAttribute("getAllProducts", productList);
        model.addAttribute("getProductCategory", getProductCategory);
        return "./admin_dashboard/view_products";
    }

    @GetMapping("/update_product/{id}")
    public String getUpdateProduct(@PathVariable int id, Model model) {
        Product getSingleProduct = productService.getProductById(id);
        model.addAttribute("getProdDtls", getSingleProduct);

        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("categoryList", categoryList);

        return "./admin_dashboard/updateProduct";
    }

    @PostMapping("/update_product")
    public String updateProduct(@ModelAttribute Product product, @RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
        Product singleProduct = productService.getProductById(product.getId());

        String imageName = singleProduct.getProductImageName(); // Default to the existing image name

        if (file != null && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null) {
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
                String productName = singleProduct.getName().toLowerCase();
                // Replace spaces with underscores
                String formattedProductName = productName.replaceAll(" ", "_");
                formattedProductName = formattedProductName.replaceAll("[^a-zA-Z0-9_]", "");

                imageName = formattedProductName + fileExtension;

                try {
                    // Get the directory path
                    Path directoryPath = Paths.get("src/main/resources/static/images/product_images");

                    // Create the directory if it doesn't exist
                    if (!Files.exists(directoryPath)) {
                        Files.createDirectories(directoryPath);
                    }
                    // Get the destination file path
                    Path destinationPath = directoryPath.resolve(imageName);

                    // Copy the file to the destination path
                    Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied successfully to: " + destinationPath);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Update other product attributes
        singleProduct.setProductImageName(imageName);
        singleProduct.setName(product.getName());
        singleProduct.setDescription(product.getDescription());
        singleProduct.setPrice(product.getPrice());
        singleProduct.setStock(product.getStock());
        singleProduct.setCategoryId(product.getCategoryId());
        singleProduct.setDiscountedPrice(product.getPrice()); // Assuming discounted price is set to regular price if no discount is provided
        singleProduct.setDiscount(product.getDiscount());

        Product updatedProduct = productService.saveProduct(singleProduct);

        if (updatedProduct != null) {
            session.setAttribute("successMsg", "Updated Successfully");
        } else {
            session.setAttribute("errorMsg", "Something Went Wrong");
        }
        return "redirect:/admin/view_products";
    }


    @GetMapping("/delete_product/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        boolean delete = productService.deleteProduct(id);

        if (delete) {
            session.setAttribute("successMsg", "Deleted Successfully");
        } else {
            session.setAttribute("errorMsg", "Something Went Wrong");
        }
        return "redirect:/admin/view_products";
    }

    @GetMapping("/add_discount/{id}")
    public String addDiscount(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("discountProduct", product);
        return "./admin_dashboard/add_discount";
    }

    @PostMapping("/handleDiscount")
    public String addDiscount(@RequestParam("productId") int id, @RequestParam("originalPrice") double originalPrice, @RequestParam("discount") int discount, Model model) {
        double discountedPrice = (originalPrice - (originalPrice * discount / 100));
        Product product = productService.getProductById(id);
        product.setDiscount(discount);
        product.setDiscountedPrice(discountedPrice);
        productService.saveProduct(product);

        return "redirect:/admin/view_products";
    }

    @GetMapping("/orders")
    public String updateCategory() {

        return "./admin_dashboard/view_orders";
    }


}
