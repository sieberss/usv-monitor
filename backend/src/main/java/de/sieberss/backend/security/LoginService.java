package de.sieberss.backend.security;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsWithoutEncryption;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.service.CredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final CredentialsRepo repo;
    private final CredentialsService credentialsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials user = repo.findByUser(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        User u = new User(user.user(), user.password(), Collections.emptyList());
        System.out.println(u);
        return u;
    }

    public void register(CredentialsWithoutEncryption submitted) {
        // App users receive a prefix in their username to distinguish from server relating credentials
        credentialsService.createCredentials(
                new CredentialsWithoutEncryption("", submitted.user(), submitted.password(), false));
    }
}