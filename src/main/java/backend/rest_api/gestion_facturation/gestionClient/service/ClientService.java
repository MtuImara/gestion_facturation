package backend.rest_api.gestion_facturation.gestionClient.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionClient.dto.ClientDTO;
import backend.rest_api.gestion_facturation.gestionClient.entity.ClientEntity;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionClient.repository.ClientRepository;
import backend.rest_api.gestion_facturation.helpers.DateHelper;
import backend.rest_api.gestion_facturation.helpers.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Map<String, Object> getAll(String title, int page, int size, String[] sort,
            Specification<ClientEntity> spec) {

        Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

        Page<ClientEntity> clientEntity = null;

        if (title == null || title.equals("")) {
            clientEntity = clientRepository.findAll(spec, pagingSort);
        } else {
        }

        List<ClientDTO> dtos = new ArrayList<>();

        for (ClientEntity entities : clientEntity) {
            dtos.add(ClientMapper.getInstance().convertToDto(entities));
        }

        Map<String, Object> data = PagingAndSortingHelper.filteredAndSortedResult(
                clientEntity.getNumber(),
                clientEntity.getTotalElements(),
                clientEntity.getTotalPages(),
                dtos);

        return data;
    }

    public ClientDTO getById(Long id) {

        ClientEntity clientEntity = null;
        try {
            clientEntity = clientRepository.getReferenceById(id);
            ClientDTO clientDto = ClientMapper.getInstance()
                    .convertToDto(clientEntity);
            return clientDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ClientDTO ajoutClientService(ClientDTO clientDto) {

        try {
            ClientEntity clientEntity = new ClientEntity();
            clientEntity = ClientMapper.getInstance()
                    .convertToEntity(clientDto);
            clientEntity.setDateCreation(DateHelper.now());
            ClientEntity creationClient = clientRepository.save(clientEntity);

            clientDto = creationClient != null
                    ? ClientMapper.getInstance().convertToDto(creationClient)
                    : null;
        } catch (Exception ex) {
            clientDto = null;
            System.out.println("null" + ex.getMessage());
        }

        return clientDto;
    }

    public ClientDTO updateClientService(Long id, ClientDTO updated) {
        ClientEntity converted_ClientEntity, updated_ClientEntity = null;
        try {

            ClientDTO clientDto = getById(id);
            converted_ClientEntity = ClientMapper.getInstance()
                    .convertToEntity(clientDto.modifyValues(updated));
            // converted_ClientEntity.setDateModification(DateHelper.now());
            updated_ClientEntity = clientRepository.save(converted_ClientEntity);
            updated = ClientMapper.getInstance().convertToDto(updated_ClientEntity);

        } catch (Exception e) {
            System.out.println("Erreur lors du Client: " + e.getMessage());
            updated = null;
        }

        return updated;
    }

}
