package com.bms.LiveShowBookingApplication.dao;

import com.bms.LiveShowBookingApplication.enums.Genre;
import com.bms.LiveShowBookingApplication.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByGenre(Genre genre);
    Optional<Show> findByName(String name);
}
