package de.sieberss.backend.repo;

import de.sieberss.backend.model.Credentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepo extends MongoRepository<Credentials, String> {
}
