package de.sieberss.backend.service;

import de.sieberss.backend.model.*;
import de.sieberss.backend.repo.ServerRepo;
import de.sieberss.backend.repo.UpsRepo;
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
    private final CredentialsService credentialsService;
    private final UpsRepo upsRepo;

    Server getServerFromDTO(ServerDTO dto) {
        Ups ups;
        if (dto == null)
            return null;
        if (dto.upsId() == null || dto.upsId().isEmpty())
            ups = null;
        else
            ups = upsRepo.findById(dto.upsId()).orElse(null);
        Credentials credentials = credentialsService.getEncryptedCredentials(dto.credentials());
        return new Server(dto.id(), dto.name(), dto.address(), credentials, ups, dto.shutdownTime());
    }

    ServerDTO getDTOFromServer(Server server) {
        if (server == null)
            return null;
        String upsId =
                server.ups() == null ? "" : server.ups().id();
        CredentialsDTO decryptedCredentials = credentialsService.decryptCredentials(server.credentials());
        return new ServerDTO(server.id(), server.name(), server.address(), decryptedCredentials, upsId, server.shutdownTime());
    }

    public List<ServerDTO> getServerDTOList() {
        return repo.findAll().stream()
                .map(this::getDTOFromServer).toList();
    }

    /**
     *
     * @param id ID of the requested database object
     *           NoSuchElementException thrown if ID not in database
     * @return DTO with data from database
     */
    public ServerDTO getServerDTOById(String id) {
         Server found = repo.findById(id).orElseThrow(()-> new NoSuchElementException(id));
         return getDTOFromServer(found);
    }

    /**
     *
     * @param serverDTO DTO received from client. Any ID provided within will be ignored
     * @return submitted DTO completed with random ID, when successfully stored in database
     */
    public ServerDTO createServer(ServerDTO serverDTO) {
         ServerDTO completed = new ServerDTO(idService.generateId(), serverDTO.name(), serverDTO.address(), serverDTO.credentials(), serverDTO.upsId(), serverDTO.shutdownTime());
         Server toStore = getServerFromDTO(completed);
         repo.save(toStore);
         return getDTOFromServer(toStore);
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
        ServerDTO completed = new ServerDTO(id, submitted.name(), submitted.address(), submitted.credentials(), submitted.upsId(), submitted.shutdownTime());
        Server toStore = getServerFromDTO(completed);
        repo.save(toStore);
        return getDTOFromServer(toStore);
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


    public ServerDTO createServerWithNewLocalCredentials(ServerDTOWithoutCredentialsId dto) {
        CredentialsDTO credentials
                = credentialsService.createCredentials(new CredentialsDTO("", dto.user(), dto.password(), false));
        return createServer(new ServerDTO("", dto.name(), dto.address(), credentials, dto.upsId(), dto.shutdownTime()));
    }


    public ServerDTO updateServerWithNewLocalCredentials(String id, ServerDTOWithoutCredentialsId dto) {
        CredentialsDTO credentials
                = credentialsService.createCredentials(new CredentialsDTO("", dto.user(), dto.password(), false));
        return updateServer(id,
                new ServerDTO("", dto.name(), dto.address(), credentials, dto.upsId(), dto.shutdownTime()));
    }
}
