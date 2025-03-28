package com.bms.LiveShowBookingApplication.controller;

import com.bms.LiveShowBookingApplication.enums.Genre;
import com.bms.LiveShowBookingApplication.model.Show;
import com.bms.LiveShowBookingApplication.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shows")
public class ShowController {
    @Autowired
    private ShowService showService;

    @PostMapping("/register")
    public String registerShow(@RequestParam String name, @RequestParam Genre genre) {
        return showService.registerShow(name, genre);
    }

    @GetMapping("/all")
    public List<Show> getAllShows() {
        return showService.getAllShows();
    }

    @PostMapping("/book")
    public String bookTicket(@RequestParam String user, @RequestParam String showName, @RequestParam String timeSlot, @RequestParam int seats) {
        return showService.bookTicket(user, showName, timeSlot, seats);
    }

    @PostMapping("/slots")
    public String onboardShowSlots(@RequestParam String showName, @RequestParam String timeSlot, @RequestParam int capacity) {
        return showService.onboardShowSlots(showName, timeSlot, capacity);
    }

    @DeleteMapping("/cancel")
    public String cancelBooking(@RequestParam UUID bookingId) {
        return showService.cancelBooking(bookingId);
    }
}
