package com.borrowbay.borrowbay.controller;

import com.borrowbay.borrowbay.entity.Booking;
import com.borrowbay.borrowbay.entity.Product;
import com.borrowbay.borrowbay.repository.BookingRepository;
import com.borrowbay.borrowbay.repository.ProductRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ProductRepository productRepository;


    public BookingController(
            BookingRepository bookingRepository,
            ProductRepository productRepository) {

        this.bookingRepository = bookingRepository;
        this.productRepository = productRepository;
    }


    /*
     * CREATE BOOKING
     */
    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody Booking booking) {

        if (booking.getProductId() == null) {

            return ResponseEntity.badRequest()
                    .body("Product ID is required");
        }


        Product product = productRepository
                .findById(booking.getProductId())
                .orElse(null);


        if (product == null) {

            return ResponseEntity.badRequest()
                    .body("Product not found");
        }


        if (!"available".equalsIgnoreCase(
                product.getAvailability())) {

            return ResponseEntity.badRequest()
                    .body("Product is no longer available");
        }


        /*
         * Get owner details from the actual product
         */
        booking.setProductName(
                product.getName()
        );

        booking.setOwnerName(
                product.getOwnerName()
        );

        booking.setOwnerEmail(
                product.getOwnerEmail()
        );

        booking.setOwnerPhone(
                product.getOwnerPhone()
        );

        booking.setPrice(
                product.getPrice()
        );


        /*
         * New booking status
         */
        booking.setStatus("CONFIRMED");


        booking.setBookingDate(
                LocalDateTime.now().toString()
        );


        /*
         * Save booking in MySQL
         */
        Booking savedBooking =
                bookingRepository.save(booking);


        /*
         * Product is now rented.
         */
        product.setAvailability("rented");

        productRepository.save(product);


        return ResponseEntity.ok(
                savedBooking
        );
    }


    /*
     * OWNER BOOKINGS
     */
    @GetMapping("/owner")
    public List<Booking> getOwnerBookings(
            @RequestParam String email) {

        return bookingRepository
                .findByOwnerEmail(email);
    }


    /*
     * CUSTOMER BOOKINGS
     */
    @GetMapping("/renter")
    public List<Booking> getRenterBookings(
            @RequestParam String email) {

        return bookingRepository
                .findByRenterEmail(email);
    }


    /*
     * BOOKINGS FOR ONE PRODUCT
     */
    @GetMapping("/product/{productId}")
    public List<Booking> getProductBookings(
            @PathVariable Long productId) {

        return bookingRepository
                .findByProductId(productId);
    }


    /*
     * MARK PRODUCT AS RETURNED
     */
    @PutMapping("/return/{productId}")
    public ResponseEntity<?> returnProduct(
            @PathVariable Long productId) {


        /*
         * Find product
         */
        Product product = productRepository
                .findById(productId)
                .orElse(null);


        if (product == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        /*
         * Find all bookings for this product
         */
        List<Booking> bookings =
                bookingRepository
                        .findByProductId(productId);


        /*
         * Mark the active/confirmed
         * rental as RETURNED.
         */
        boolean bookingFound = false;


        for (Booking booking : bookings) {

            String status =
                    booking.getStatus();


            if (
                    status != null &&
                            (
                                    status.equalsIgnoreCase(
                                            "CONFIRMED"
                                    )
                                            ||
                                            status.equalsIgnoreCase(
                                                    "ACTIVE"
                                            )
                            )
            ) {

                booking.setStatus(
                        "RETURNED"
                );


                bookingRepository.save(
                        booking
                );


                bookingFound = true;
            }
        }


        /*
         * Make product available again.
         */
        product.setAvailability(
                "available"
        );


        productRepository.save(
                product
        );


        /*
         * Return success response.
         */
        if (bookingFound) {

            return ResponseEntity.ok(
                    "Booking marked as returned and product is available again"
            );

        }


        /*
         * Product was returned but
         * no active booking was found.
         */
        return ResponseEntity.ok(
                "Product is available again"
        );
    }
}