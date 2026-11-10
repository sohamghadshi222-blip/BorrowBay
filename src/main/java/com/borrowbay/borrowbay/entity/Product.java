package com.borrowbay.borrowbay.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String ownerEmail;

    @Column(length = 15)
    private String ownerPhone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @Column(nullable = false)
    private String availability;

    @Column(nullable = false)
    private String pickupAddress;

    @Column(nullable = false)
    private String returnAddress;

    @Column(nullable = false)
    private String upiId;


    public Product() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }


    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }


    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }


    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }


    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }


    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}