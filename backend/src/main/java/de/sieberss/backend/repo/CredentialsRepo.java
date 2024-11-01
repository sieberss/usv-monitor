package de.sieberss.backend.repo;

import de.sieberss.backend.model.Credentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepo extends MongoRepository<Credentials, String> {
    Optional<Credentials> findByUser(String username);
}
