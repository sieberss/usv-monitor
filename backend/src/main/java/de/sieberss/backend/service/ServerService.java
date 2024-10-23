package de.sieberss.backend.service;

import de.sieberss.backend.model.Server;
import de.sieberss.backend.model.ServerDTO;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.utils.DTOConverter;
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
    private final DTOConverter converter;


    public List<ServerDTO> getServerDTOList() {
        return repo.findAll().stream()
                .map(converter::getDTOFromServer).toList();
    }

    public ServerDTO getServerDTOById(String id) {
         Server found = repo.findById(id).orElseThrow(()-> new NoSuchElementException(id));
         return converter.getDTOFromServer(found);
    }

    public ServerDTO createServer(ServerDTO serverDTO) {
         ServerDTO completed = new ServerDTO(idService.generateId(), serverDTO.name(), serverDTO.address(), serverDTO.credentials(), serverDTO.upsId());
         Server dbObject = converter.getServerFromDTO(completed);
         repo.save(dbObject);
         return completed;
    }

    public ServerDTO updateServer(String id, ServerDTO serverDTO) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        Server toStore = converter.getServerFromDTO(serverDTO);
        Server stored = repo.save(toStore);
        return converter.getDTOFromServer(stored);
    }

    public void deleteServer(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }
}
