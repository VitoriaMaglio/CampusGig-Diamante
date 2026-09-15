package com.diamante.campusgigs.service;

import com.diamante.campusgigs.entity.Gig;
import com.diamante.campusgigs.entity.User;
import com.diamante.campusgigs.entity.dto.CreateGigRequest;
import com.diamante.campusgigs.entity.enuns.GigStatus;
import com.diamante.campusgigs.entity.enuns.Role;
import com.diamante.campusgigs.exception.ForbiddenOperationException;
import com.diamante.campusgigs.exception.GigNotFoundException;
import com.diamante.campusgigs.repository.GigRepository;
import com.diamante.campusgigs.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GigService {

    private final GigRepository gigRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public Gig create(CreateGigRequest request) {
        User provider = authenticatedUserProvider.getCurrentUser();

        Gig gig = Gig.builder()
                .provider(provider)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .status(GigStatus.ACTIVE)
                .build();

        return gigRepository.save(gig);
    }

    public List<Gig> listActive() {
        return gigRepository.findByStatus(GigStatus.ACTIVE);
    }

    public void close(Long gigId) {
        Gig gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new GigNotFoundException(gigId));

        User currentUser = authenticatedUserProvider.getCurrentUser();
        boolean isOwner = gig.getProvider().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenOperationException("Você só pode encerrar os próprios serviços");
        }

        gig.setStatus(GigStatus.CLOSED);
        gigRepository.save(gig);
    }
}