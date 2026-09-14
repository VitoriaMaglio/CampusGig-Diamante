package com.diamante.campusgigs.entity.dto;


import com.diamante.campusgigs.entity.Gig;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class GigResponse {

    private Long id;
    private Long providerId;
    private String providerName;
    private String title;
    private String description;
    private String category;
    private BigDecimal price;
    private String status;

    public static GigResponse from(Gig gig) {
        return GigResponse.builder()
                .id(gig.getId())
                .providerId(gig.getProvider().getId())
                .providerName(gig.getProvider().getName())
                .title(gig.getTitle())
                .description(gig.getDescription())
                .category(gig.getCategory())
                .price(gig.getPrice())
                .status(gig.getStatus().name())
                .build();
    }
}