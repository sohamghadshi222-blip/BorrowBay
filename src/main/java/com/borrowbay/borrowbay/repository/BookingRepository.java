package com.borrowbay.borrowbay.repository;

import com.borrowbay.borrowbay.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findByOwnerEmail(String ownerEmail);

    List<Booking> findByRenterEmail(String renterEmail);

    List<Booking> findByProductId(Long productId);
}