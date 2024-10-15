package de.sieberss.backend.service;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvListResponse;
import de.sieberss.backend.model.UsvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UsvService {
    private final UsvRepo repo;

    public List<Usv> getUsvList() {
        List<Usv> usvs = repo.findAll();
        UsvListResponse response = new UsvListResponse(false, usvs);
        System.out.println(response);
        return usvs;
    }

    public Usv getUsvById(String id) {
        return repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
    }
}
