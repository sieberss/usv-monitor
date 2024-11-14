package de.sieberss.backend.utils;

import de.sieberss.backend.model.*;
import de.sieberss.backend.repo.UpsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DTOConverter {

    private final UpsRepo upsRepo;

    public Server getServerFromDTO(ServerDTO dto) {
        Ups ups;
        if (dto == null)
            return null;
        if (dto.upsId() == null || dto.upsId().isEmpty())
            ups = null;
        else
            ups = upsRepo.findById(dto.upsId()).orElse(null);
        Credentials credentials = EncryptionService.encryptCredentials(dto.credentials());
        return new Server(dto.id(), dto.name(), dto.address(), credentials, ups, dto.shutdownTime());
    }

    public ServerDTO getDTOFromServer(Server server) {
        if (server == null)
            return null;
        String upsId =
                server.ups() == null ? "" : server.ups().id();
        CredentialsDTO decryptedCredentials = EncryptionService.decryptCredentials(server.credentials());
        return new ServerDTO(server.id(), server.name(), server.address(), decryptedCredentials, upsId, server.shutdownTime());
    }
}
