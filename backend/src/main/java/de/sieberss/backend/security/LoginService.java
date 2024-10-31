package de.sieberss.backend.security;

import de.sieberss.backend.model.Credentials;
import de.sieberss.backend.repo.CredentialsRepo;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials user = repo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(user.user(), user.password(), Collections.emptyList());
    }

    public void addNewAdminUser(String password) {
        repo.save(new Credentials("admin", "admin", password, false));
    }
}