package com.bms.LiveShowBookingApplication.service;

import com.bms.LiveShowBookingApplication.dao.BookingRepository;
import com.bms.LiveShowBookingApplication.dao.ShowRepository;
import com.bms.LiveShowBookingApplication.enums.Genre;
import com.bms.LiveShowBookingApplication.model.Booking;
import com.bms.LiveShowBookingApplication.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private Map<String, Integer> trendingShows = new ConcurrentHashMap<>();

    public String registerShow(String name, Genre genre) {
        if (showRepository.findByName(name).isPresent()) {
            return "Show already exists.";
        }
        showRepository.save(new Show(name, genre));
        return name + " show is registered !!";
    }

    public String onboardShowSlots(String showName, String timeSlot, int capacity) {
        Show show = showRepository.findByName(showName).orElse(null);
        if (show == null) return "Show not found.";
        if (!show.addSlot(timeSlot, capacity)) return "Invalid or duplicate slot.";
        showRepository.save(show);
        return "Slot " + timeSlot + " added for " + showName;
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public String bookTicket(String username, String showName, String timeSlot, int seats) {
        Show show = showRepository.findByName(showName).orElse(null);
        if (show == null) return "Show not found.";

        List<Booking> userBookings = bookingRepository.findByUsername(username);
        for (Booking booking : userBookings) {
            if (booking.getTimeSlot().equals(timeSlot)) {
                return "User already has a booking in this time slot.";
            }
        }

        if (!show.isSlotAvailable(timeSlot, seats)) {
            show.addToWaitlist(timeSlot, username);
            return "Slot full. User added to waitlist.";
        }
        show.bookSlot(timeSlot, seats);
        bookingRepository.save(new Booking(username, showName, timeSlot, seats));
        trendingShows.put(showName, trendingShows.getOrDefault(showName, 0) + 1);
        return "Booked successfully!";
    }

    public String cancelBooking(UUID bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) return "Booking not found.";
        Booking booking = bookingOpt.get();
        Show show = showRepository.findByName(booking.getShowName()).orElse(null);
        if (show == null) return "Show not found.";
        show.cancelSlot(booking.getTimeSlot(), booking.getSeats());
        bookingRepository.delete(booking);
        String nextUser = show.getNextWaitlistUser(booking.getTimeSlot());
        if (nextUser != null) {
            bookTicket(nextUser, booking.getShowName(), booking.getTimeSlot(), booking.getSeats());
        }
        return "Booking canceled.";
    }
}
