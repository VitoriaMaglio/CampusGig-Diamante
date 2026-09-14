package com.projeto.campusgigs.service;


import com.projeto.campusgigs.entity.Contract;
import com.projeto.campusgigs.entity.Gig;
import com.projeto.campusgigs.entity.User;
import com.projeto.campusgigs.entity.enuns.ContractStatus;
import com.projeto.campusgigs.entity.enuns.GigStatus;
import com.projeto.campusgigs.entity.dto.CreateContractRequest;
import com.projeto.campusgigs.exception.CannotHireOwnGigException;
import com.projeto.campusgigs.exception.GigNotActiveException;
import com.projeto.campusgigs.exception.GigNotFoundException;
import com.projeto.campusgigs.repository.ContractRepository;
import com.projeto.campusgigs.repository.GigRepository;
import com.projeto.campusgigs.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final GigRepository gigRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public Contract hire(CreateContractRequest request) {
        Gig gig = gigRepository.findById(request.getGigId())
                .orElseThrow(() -> new GigNotFoundException(request.getGigId()));

        if (gig.getStatus() != GigStatus.ACTIVE) {
            throw new GigNotActiveException(gig.getId());
        }

        User hirer = authenticatedUserProvider.getCurrentUser();

        if (gig.getProvider().getId().equals(hirer.getId())) {
            throw new CannotHireOwnGigException();
        }

        Contract contract = Contract.builder()
                .gig(gig)
                .hirer(hirer)
                .status(ContractStatus.REQUESTED)
                .build();

        return contractRepository.save(contract);
    }
}
