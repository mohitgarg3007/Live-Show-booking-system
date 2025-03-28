package com.bms.LiveShowBookingApplication.model;

import com.bms.LiveShowBookingApplication.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Data
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ElementCollection
    @CollectionTable(name = "show_slots", joinColumns = @JoinColumn(name = "show_id"))
    @MapKeyColumn(name = "time_slot")
    @Column(name = "capacity")
    private Map<String, Integer> availableSlots = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "show_waitlist", joinColumns = @JoinColumn(name = "show_id"))
    @MapKeyColumn(name = "time_slot")
    @Column(name = "waitlist")
    private Map<String, List<String>> waitlist = new HashMap<>();

    public Show() {}
    public Show(String name, Genre genre) {
        this.name = name;
        this.genre = genre;
    }

    public boolean addSlot(String timeSlot, int capacity) {
        if (availableSlots.containsKey(timeSlot) || !timeSlot.matches("\\d{1,2}:00-\\d{1,2}:00")) return false;
        this.availableSlots.put(timeSlot, capacity);
        this.waitlist.put(timeSlot, new ArrayList<>());
        return true;
    }

    public boolean isSlotAvailable(String timeSlot, int seats) {
        return availableSlots.getOrDefault(timeSlot, 0) >= seats;
    }

    public void bookSlot(String timeSlot, int seats) {
        availableSlots.put(timeSlot, availableSlots.get(timeSlot) - seats);
    }

    public void cancelSlot(String timeSlot, int seats) {
        availableSlots.put(timeSlot, availableSlots.get(timeSlot) + seats);
    }

    public void addToWaitlist(String timeSlot, String user) {
        waitlist.get(timeSlot).add(user);
    }

    public String getNextWaitlistUser(String timeSlot) {
        return waitlist.get(timeSlot).isEmpty() ? null : waitlist.get(timeSlot).remove(0);
    }
}
