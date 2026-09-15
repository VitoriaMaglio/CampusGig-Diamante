package com.diamante.campusgigs.repository;

import com.diamante.campusgigs.entity.Gig;
import com.diamante.campusgigs.entity.enuns.GigStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GigRepository extends JpaRepository<Gig, Long> {

    @Query("SELECT g FROM Gig g JOIN FETCH g.provider WHERE g.status = :status")
    List<Gig> findByStatusWithProvider(@Param("status") GigStatus status);

    List<Gig> findByProviderId(Long providerId);
}