package de.sieberss.backend.service;

import de.sieberss.backend.model.Ups;
import de.sieberss.backend.repo.UpsRepo;
import de.sieberss.backend.utils.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UpsService {
    private final UpsRepo repo;
    private final IdService idService;

    public List<Ups> getUpsList() {
        return repo.findAll();
    }

    public Ups getUpsById(String id) {
        return repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
    }

    public Ups createUps(Ups ups) {
        return repo.save(
                new Ups(idService.generateId(), ups.name(), ups.address(), ups.community()));
    }

    public Ups updateUps(String id, Ups ups) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        return repo.save(new Ups(id, ups.name(), ups.address(), ups.community()));
    }

    public void deleteUps(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }
}
