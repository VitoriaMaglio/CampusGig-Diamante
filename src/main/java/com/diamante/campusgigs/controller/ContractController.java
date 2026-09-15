package com.diamante.campusgigs.controller;

import com.diamante.campusgigs.entity.Contract;
import com.diamante.campusgigs.entity.dto.ContractResponse;
import com.diamante.campusgigs.entity.dto.CreateContractRequest;
import com.diamante.campusgigs.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractResponse> hire(@Valid @RequestBody CreateContractRequest request) {
        Contract contract = contractService.hire(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContractResponse.from(contract));
    }
}