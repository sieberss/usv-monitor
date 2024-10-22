package de.sieberss.backend.service;

import de.sieberss.backend.model.Server;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.utils.IdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepo repo;
    private final IdService idService;


    public List<Server> getServerList() {
        return repo.findAll();
    }

    public Server getServerById(String id) {
        return repo.findById(id).orElseThrow(()-> new NoSuchElementException(id));
    }

    public Server createServer(Server server) {
        return repo.save(
                new Server(idService.generateId(), server.name(), server.address(), server.credentials(), server.ups()));
    }

    public Server updateServer(String id, Server server) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        return repo.save(server);
    }

    public void deleteServer(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }
}
