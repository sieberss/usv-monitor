package de.sieberss.backend.security;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.model.CredentialsDTO;
import de.sieberss.backend.repo.CredentialsRepo;
import de.sieberss.backend.utils.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final CredentialsRepo repo;
    private final IdService idService;

    private final Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

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
            Credentials newCredentials = Credentials.builder()
                    .id(idService.generateId())
                    .user(submitted.user())
                    .password(encoder.encode(submitted.password()))
                    .global(false)
                    .build();
            repo.save(newCredentials);
        }
    }
}