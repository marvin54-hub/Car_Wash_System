package com.witbank.carwash.repository;

import com.witbank.carwash.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByServiceType(String serviceType);
}
