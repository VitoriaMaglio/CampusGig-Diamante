package com.projeto.campusgigs.service;


import com.projeto.campusgigs.entity.User;
import com.projeto.campusgigs.entity.enuns.Role;
import com.projeto.campusgigs.entity.dto.RegisterRequest;
import com.projeto.campusgigs.exception.EmailAlreadyInUseException;
import com.projeto.campusgigs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto.campusgigs.client.CepClient;
import com.projeto.campusgigs.client.CepResponse;
import com.projeto.campusgigs.exception.CepNotFoundException;
import com.projeto.campusgigs.exception.CepServiceUnavailableException;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CepClient cepClient;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException(request.getEmail());
        }

        User.UserBuilder builder = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .zipCode(request.getZipCode());

        if (request.getZipCode() != null && !request.getZipCode().isBlank()) {
            CepResponse address = lookupAddress(request.getZipCode());
            builder.city(address.getLocalidade());
            builder.state(address.getUf());
        }

        return userRepository.save(builder.build());
    }

    private CepResponse lookupAddress(String zipCode) {
        try {
            CepResponse response = cepClient.getByCep(zipCode);

            if (response == null || Boolean.TRUE.equals(response.getErro())) {
                throw new CepNotFoundException(zipCode);
            }

            return response;
        } catch (ResourceAccessException ex) {
            throw new CepServiceUnavailableException();
        } catch (RestClientResponseException ex) {
            throw new CepServiceUnavailableException();
        }
    }
}