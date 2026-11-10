package com.borrowbay.borrowbay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 15)
    private String phone;


    // =========================
    // CONSTRUCTOR
    // =========================

    public User() {
    }


    // =========================
    // ID
    // =========================

    public Long getId() {
        return id;
    }


    // =========================
    // NAME
    // =========================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // =========================
    // EMAIL
    // =========================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =========================
    // PASSWORD
    // =========================

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // =========================
    // PHONE
    // =========================

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}