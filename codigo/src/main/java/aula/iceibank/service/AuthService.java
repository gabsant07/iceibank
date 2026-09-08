package aula.iceibank.service;

import aula.iceibank.config.BankProperties;
import aula.iceibank.dto.LoginRequest;
import aula.iceibank.dto.LoginResponse;
import aula.iceibank.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final BankProperties properties;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                       JwtService jwtService, BankProperties properties) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.properties = properties;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String token = jwtService.generate(user, properties.getAgencyId());
        return new LoginResponse(token, "Bearer", jwtService.expirationSeconds(), properties.getAgencyId());
    }
}
