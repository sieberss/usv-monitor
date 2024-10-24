package de.sieberss.backend.repo;

import de.sieberss.backend.model.Server;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepo extends MongoRepository<Server, String> {
}
