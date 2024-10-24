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

    /**
     *
     * @param id ID of the requested database object
     *           NoSuchElementException thrown if ID not in database
     * @return DTO with data from database
     */
    public ServerDTO getServerDTOById(String id) {
         Server found = repo.findById(id).orElseThrow(()-> new NoSuchElementException(id));
         return converter.getDTOFromServer(found);
    }

    /**
     *
     * @param serverDTO DTO received from client. Any ID provided within will be ignored
     * @return submitted DTO completed with random ID, when successfully stored in database
     */
    public ServerDTO createServer(ServerDTO serverDTO) {
         ServerDTO completed = new ServerDTO(idService.generateId(), serverDTO.name(), serverDTO.address(), serverDTO.credentials(), serverDTO.upsId());
         Server toStore = converter.getServerFromDTO(completed);
         repo.save(toStore);
         return converter.getDTOFromServer(toStore);
    }

    /**
     * @param id ID of server to update.
     *           NoSuchElementException thrown if ID not in database
     * @param submitted DTO received from client. Need not contain ID.
     * @return Submitted DTO completed with ID, if server exists in database
     */
    public ServerDTO updateServer(String id, ServerDTO submitted) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        ServerDTO completed = new ServerDTO(id, submitted.name(), submitted.address(), submitted.credentials(), submitted.upsId());
        Server toStore = converter.getServerFromDTO(completed);
        repo.save(toStore);
        return converter.getDTOFromServer(toStore);
    }

    /**
     *
     * @param id ID of the database object to delete
     *           NoSuchElementException thrown if ID not in database
     */
    public void deleteServer(String id) {
        if (!repo.existsById(id))
            throw new NoSuchElementException(id);
        repo.deleteById(id);
    }
}
