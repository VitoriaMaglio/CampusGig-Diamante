package com.diamante.campusgigs.service;


import com.diamante.campusgigs.entity.Contract;
import com.diamante.campusgigs.entity.Gig;
import com.diamante.campusgigs.entity.User;
import com.diamante.campusgigs.entity.dto.CreateContractRequest;
import com.diamante.campusgigs.entity.enuns.ContractStatus;
import com.diamante.campusgigs.entity.enuns.GigStatus;
import com.diamante.campusgigs.exception.CannotHireOwnGigException;
import com.diamante.campusgigs.exception.GigNotActiveException;
import com.diamante.campusgigs.exception.GigNotFoundException;
import com.diamante.campusgigs.repository.ContractRepository;
import com.diamante.campusgigs.repository.GigRepository;
import com.diamante.campusgigs.security.AuthenticatedUserProvider;
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
