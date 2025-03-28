package com.bms.LiveShowBookingApplication.dao;

import com.bms.LiveShowBookingApplication.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUsername(String username);
    Optional<Booking> findById(UUID id);
}
