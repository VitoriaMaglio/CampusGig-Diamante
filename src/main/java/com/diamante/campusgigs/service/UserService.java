package com.diamante.campusgigs.service;


import com.diamante.campusgigs.client.CepClient;
import com.diamante.campusgigs.client.CepResponse;
import com.diamante.campusgigs.entity.User;
import com.diamante.campusgigs.entity.dto.RegisterRequest;
import com.diamante.campusgigs.entity.enuns.Role;
import com.diamante.campusgigs.exception.CepNotFoundException;
import com.diamante.campusgigs.exception.CepServiceUnavailableException;
import com.diamante.campusgigs.exception.EmailAlreadyInUseException;
import com.diamante.campusgigs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    public User updateZipCode(Long userId, String zipCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        var address = lookupAddress(zipCode);

        user.setZipCode(zipCode);
        user.setCity(address.getLocalidade());
        user.setState(address.getUf());

        return userRepository.save(user);
    }
}