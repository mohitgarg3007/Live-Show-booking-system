package com.bms.LiveShowBookingApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String showName;
    private String timeSlot;
    private int seats;

    public Booking() {}
    public Booking(String username, String showName, String timeSlot, int seats) {
        this.username = username;
        this.showName = showName;
        this.timeSlot = timeSlot;
        this.seats = seats;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getShowName() {
        return showName;
    }

}
