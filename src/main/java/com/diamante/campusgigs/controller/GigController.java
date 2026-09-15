package com.diamante.campusgigs.controller;


import com.diamante.campusgigs.entity.Gig;
import com.diamante.campusgigs.entity.dto.CreateGigRequest;
import com.diamante.campusgigs.entity.dto.GigResponse;
import com.diamante.campusgigs.service.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gigs")
@RequiredArgsConstructor
public class GigController {

    private final GigService gigService;

    @PostMapping
    public ResponseEntity<GigResponse> create(@Valid @RequestBody CreateGigRequest request) {
        Gig gig = gigService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(GigResponse.from(gig));
    }

    @GetMapping
    public ResponseEntity<List<GigResponse>> list() {
        List<GigResponse> gigs = gigService.listActive().stream()
                .map(GigResponse::from)
                .toList();
        return ResponseEntity.ok(gigs);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> close(@PathVariable Long id) {
        gigService.close(id);
        return ResponseEntity.noContent().build();
    }
}