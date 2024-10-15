package de.sieberss.backend.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsvRepo extends MongoRepository<Usv, String> {
}
