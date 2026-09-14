package com.diamante.campusgigs.entity.dto;


import com.diamante.campusgigs.entity.Contract;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractResponse {

    private Long id;
    private Long gigId;
    private String gigTitle;
    private Long hirerId;
    private String hirerName;
    private String status;

    public static ContractResponse from(Contract contract) {
        return ContractResponse.builder()
                .id(contract.getId())
                .gigId(contract.getGig().getId())
                .gigTitle(contract.getGig().getTitle())
                .hirerId(contract.getHirer().getId())
                .hirerName(contract.getHirer().getName())
                .status(contract.getStatus().name())
                .build();
    }
}