package com.sportslot.repository;

import com.sportslot.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findBySportIgnoreCase(String sport);

    List<Venue> findByOwnerId(Long ownerId);

    @Query("SELECT v FROM Venue v WHERE " +
           "LOWER(v.name) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(v.location) LIKE LOWER(CONCAT('%',:q,'%'))")
    List<Venue> searchVenues(@Param("q") String query);

    @Query("SELECT v FROM Venue v WHERE LOWER(v.sport) = LOWER(:sport) AND (" +
           "LOWER(v.name) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(v.location) LIKE LOWER(CONCAT('%',:q,'%')))")
    List<Venue> searchBySportAndQuery(@Param("sport") String sport,
                                      @Param("q") String query);
}
