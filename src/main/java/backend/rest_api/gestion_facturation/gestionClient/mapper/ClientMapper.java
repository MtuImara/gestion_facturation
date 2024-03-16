package backend.rest_api.gestion_facturation.gestionClient.mapper;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionClient.dto.ClientDTO;
import backend.rest_api.gestion_facturation.gestionClient.entity.ClientEntity;
import backend.rest_api.gestion_facturation.helpers.DateHelper;

@Component
public class ClientMapper {

    public ClientMapper() {
    }

    public static ClientMapper getInstance() {
        return new ClientMapper();
    }

    public ClientEntity convertToEntity(ClientDTO dto) {

        ClientEntity entity = new ClientEntity();

        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setTelephone(dto.getTelephone());
        entity.setFax(dto.getFax());
        entity.setAdresse(dto.getAdresse());
        entity.setEmail(dto.getEmail());
        entity.setNif(dto.getNif());
        entity.setRegistreCommerce(dto.getRegistreCommerce());
        entity.setAssujettiTva(dto.getAssujettiTva());
        entity.setNomUtilisateurCreation(dto.getNomUtilisateurCreation());
        entity.setDateCreation(DateHelper.toDate(dto.getDateCreation()));

        return entity;
    }

    public ClientDTO convertToDto(ClientEntity entity) {

        ClientDTO dto = new ClientDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setTelephone(entity.getTelephone());
        dto.setFax(entity.getFax());
        dto.setAdresse(entity.getAdresse());
        dto.setEmail(entity.getEmail());
        dto.setNif(entity.getNif());
        dto.setRegistreCommerce(entity.getRegistreCommerce());
        dto.setAssujettiTva(entity.getAssujettiTva());
        dto.setNomUtilisateurCreation(entity.getNomUtilisateurCreation());
        dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));

        return dto;
    }

}
