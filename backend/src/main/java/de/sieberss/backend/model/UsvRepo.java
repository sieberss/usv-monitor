package de.sieberss.backend.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsvRepo extends MongoRepository<Usv, String> {
}
