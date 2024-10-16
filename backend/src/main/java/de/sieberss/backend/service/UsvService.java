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
    private final IdService idService;

    public List<Usv> getUsvList() {
        List<Usv> usvs = repo.findAll();
        UsvListResponse response = new UsvListResponse(false, usvs);
        System.out.println(response);
        return usvs;
    }

    public Usv getUsvById(String id) {
        return repo.findById(id).orElseThrow(()->new NoSuchElementException(id));
    }

    public Usv createUsv(Usv usv) {
        return repo.save(new Usv(idService.generateId(), usv.name(), usv.address(), usv.community()));
    }

    public Usv updateUsv(String id, Usv usv) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        return repo.save(new Usv(id, usv.name(), usv.address(), usv.community()));
    }

    public void deleteUsv(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }
}
