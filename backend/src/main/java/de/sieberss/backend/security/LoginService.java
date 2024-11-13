package de.sieberss.backend.security;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
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
        return new User(user.user(), user.password(), Collections.emptyList());
    }

    public void register(CredentialsDTO submitted) {
        try {
            loadUserByUsername(submitted.user());
            throw new IllegalArgumentException(submitted.user() + " already exists");
        }
        catch (UsernameNotFoundException e) {
            credentialsService.createCredentials(
                    new CredentialsDTO("", submitted.user(), submitted.password(), false));
        }
    }
}