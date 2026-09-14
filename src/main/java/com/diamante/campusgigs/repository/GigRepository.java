package com.diamante.campusgigs.repository;

import com.diamante.campusgigs.entity.Gig;
import com.diamante.campusgigs.entity.enuns.GigStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GigRepository extends JpaRepository<Gig, Long> {

    List<Gig> findByStatus(GigStatus status);

    List<Gig> findByProviderId(Long providerId);
}