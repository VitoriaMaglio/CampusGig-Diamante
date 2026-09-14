package com.projeto.campusgigs.service;

import com.projeto.campusgigs.entity.Gig;
import com.projeto.campusgigs.entity.User;
import com.projeto.campusgigs.entity.enuns.GigStatus;
import com.projeto.campusgigs.entity.enuns.Role;
import com.projeto.campusgigs.entity.dto.CreateGigRequest;
import com.projeto.campusgigs.exception.ForbiddenOperationException;
import com.projeto.campusgigs.exception.GigNotFoundException;
import com.projeto.campusgigs.repository.GigRepository;
import com.projeto.campusgigs.security.AuthenticatedUserProvider;
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