package com.borrowbay.borrowbay.controller;

import com.borrowbay.borrowbay.entity.Product;
import com.borrowbay.borrowbay.entity.User;
import com.borrowbay.borrowbay.repository.ProductRepository;
import com.borrowbay.borrowbay.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductRepository productRepository;
    private final UserService userService;

    public ProductController(
            ProductRepository productRepository,
            UserService userService) {

        this.productRepository = productRepository;
        this.userService = userService;
    }


    // =========================
    // GET ALL PRODUCTS
    // =========================

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> products =
                productRepository.findAll();

        updateOwnerPhones(products);

        return ResponseEntity.ok(products);
    }


    // =========================
    // GET MY LISTINGS
    // =========================

    @GetMapping("/owner")
    public ResponseEntity<List<Product>> getOwnerProducts(
            @RequestParam String email) {

        List<Product> products =
                productRepository.findByOwnerEmail(email);

        updateOwnerPhones(products);

        return ResponseEntity.ok(products);
    }


    // =========================
    // GET PRODUCT BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(
            @PathVariable Long id) {

        Product product =
                productRepository.findById(id)
                        .orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        updateOwnerPhone(product);

        return ResponseEntity.ok(product);
    }


    // =========================
    // ADD PRODUCT
    // =========================

    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestBody Product product) {

        if (product.getOwnerEmail() == null ||
                product.getOwnerEmail().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Owner email is required");
        }

        User owner =
                userService.findByEmail(
                        product.getOwnerEmail()
                );

        if (owner == null) {

            return ResponseEntity.badRequest()
                    .body("Owner account not found");
        }

        /*
         * Owner details always come
         * from the database.
         */

        product.setOwnerName(
                owner.getName()
        );

        product.setOwnerEmail(
                owner.getEmail()
        );

        product.setOwnerPhone(
                owner.getPhone()
        );

        Product savedProduct =
                productRepository.save(product);

        return ResponseEntity.ok(savedProduct);
    }


    // =========================
    // UPDATE PRODUCT
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody Product updatedProduct) {

        Product existingProduct =
                productRepository.findById(id)
                        .orElse(null);

        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        /*
         * Keep original owner.
         */

        String ownerEmail =
                existingProduct.getOwnerEmail();

        User owner =
                userService.findByEmail(ownerEmail);

        if (owner == null) {

            return ResponseEntity.badRequest()
                    .body("Owner account not found");
        }

        existingProduct.setOwnerName(
                owner.getName()
        );

        existingProduct.setOwnerEmail(
                owner.getEmail()
        );

        existingProduct.setOwnerPhone(
                owner.getPhone()
        );


        /*
         * Update product details.
         */

        existingProduct.setName(
                updatedProduct.getName()
        );

        existingProduct.setCategory(
                updatedProduct.getCategory()
        );

        existingProduct.setDescription(
                updatedProduct.getDescription()
        );

        existingProduct.setPrice(
                updatedProduct.getPrice()
        );

        existingProduct.setImage(
                updatedProduct.getImage()
        );

        existingProduct.setAvailability(
                updatedProduct.getAvailability()
        );

        existingProduct.setPickupAddress(
                updatedProduct.getPickupAddress()
        );

        existingProduct.setReturnAddress(
                updatedProduct.getReturnAddress()
        );

        existingProduct.setUpiId(
                updatedProduct.getUpiId()
        );


        Product savedProduct =
                productRepository.save(
                        existingProduct
                );

        return ResponseEntity.ok(savedProduct);
    }


    // =========================
    // DELETE PRODUCT
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id) {

        if (!productRepository.existsById(id)) {

            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(id);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );
    }


    // =========================
    // UPDATE ONE OWNER PHONE
    // =========================

    private void updateOwnerPhone(Product product) {

        if (product.getOwnerEmail() == null) {
            return;
        }

        User owner =
                userService.findByEmail(
                        product.getOwnerEmail()
                );

        if (owner != null) {

            product.setOwnerPhone(
                    owner.getPhone()
            );
        }
    }


    // =========================
    // UPDATE OWNER PHONES
    // =========================

    private void updateOwnerPhones(
            List<Product> products) {

        for (Product product : products) {

            updateOwnerPhone(product);
        }
    }
}