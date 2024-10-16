package de.sieberss.backend.service;

import de.sieberss.backend.model.Usv;
import de.sieberss.backend.model.UsvListResponse;
import de.sieberss.backend.model.UsvRepo;
import de.sieberss.backend.model.UsvResponseObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UsvService {
    private final UsvRepo repo;
    private final IdService idService;
    
    @Getter
    @Setter
    private boolean monitoring = false;
    

    public UsvListResponse getUsvListResponse() {
        return buildUsvListResponseFromDatabaseResults(getAllUsvs().findAll());
    }

    private UsvListResponse buildUsvListResponseFromDatabaseResults(List<Usv> usvs) {
        /* Code for distinction comes later. For now assume isMonitorin()==false

        if (isMonitoring()) {
   }
        else {*/
            List<UsvResponseObject> responseObjects =
                usvs.stream()
                    .map(usv -> new UsvResponseObject(usv, null))
                    .toList();
            return new UsvListResponse(false, responseObjects);
        //}
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
