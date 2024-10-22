package de.sieberss.backend.repo;

import de.sieberss.backend.model.Ups;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpsRepo extends MongoRepository<Ups, String> {
}
