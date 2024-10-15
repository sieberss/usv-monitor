package de.sieberss.backend.service;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvListResponse;
import de.sieberss.backend.model.UsvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsvService {
    private final UsvRepo repo;

    public UsvListResponse getAllUsvs() {
        List<Usv> usvs = repo.findAll();
        return new UsvListResponse(false, usvs);
    }
}
