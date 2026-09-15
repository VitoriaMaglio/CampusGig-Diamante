package com.diamante.campusgigs.controller;

import com.diamante.campusgigs.entity.User;
import com.diamante.campusgigs.entity.dto.UpdateZipCodeRequest;
import com.diamante.campusgigs.entity.dto.UserResponse;
import com.diamante.campusgigs.security.AuthenticatedUserProvider;
import com.diamante.campusgigs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        return ResponseEntity.ok(UserResponse.from(currentUser));
    }

    @PatchMapping("/me/zip-code")
    public ResponseEntity<UserResponse> updateZipCode(@Valid @RequestBody UpdateZipCodeRequest request) {
        User currentUser = authenticatedUserProvider.getCurrentUser();
        User updated = userService.updateZipCode(currentUser.getId(), request.getZipCode());
        return ResponseEntity.ok(UserResponse.from(updated));
    }
}

