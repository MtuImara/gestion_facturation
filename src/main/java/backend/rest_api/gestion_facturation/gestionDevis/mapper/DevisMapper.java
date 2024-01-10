package backend.rest_api.gestion_facturation.gestionDevis.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.constantes.StaticListOfValues;
import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionClient.dto.ClientDTO;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDTO;
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDetailDTO;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisEntity;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;

@Component
public class DevisMapper {

    public DevisMapper() {
    }

    public static DevisMapper getInstance() {
        return new DevisMapper();
    }

    public DevisEntity convertToEntity(DevisDTO dto) {

        DevisEntity entity = new DevisEntity();

        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setReference(dto.getReference());
        entity.setDenominationClient(dto.getDenominationClient());
        entity.setCommentaire(dto.getCommentaire());
        if (dto.getTypeStatut() != null) {
            entity.setTypeStatut(Integer.parseInt(dto.getTypeStatut().getKey()));
        }
        entity.setDateOperation(DateHelper.toDate(dto.getDateOperation()));
        entity.setDateCreation(DateHelper.toDate(dto.getDateCreation()));
        entity.setDateModification(DateHelper.toDate(dto.getDateModification()));
        if (dto.getClient() != null) {
            entity.setIdClient(dto.getClient().getId());
        }

        return entity;
    }

    public DevisDTO convertToDto(DevisEntity entity) {

        DevisDTO dto = new DevisDTO();

        StaticValue staticValStatut = new StaticValue();
        StaticListOfValues listOfValuesStatut = new StaticListOfValues();
        staticValStatut.setKey(
                listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getKey().trim());
        staticValStatut.setValue(
                listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getValue());

        dto.setId(entity.getId());

        dto.setCode(entity.getCode());
        dto.setReference(entity.getReference());
        dto.setDenominationClient(entity.getDenominationClient());
        dto.setCommentaire(entity.getCommentaire());
        dto.setTypeStatut(staticValStatut);
        dto.setDateOperation(DateHelper.toText(entity.getDateOperation(), "time"));
        dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
        dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
        if (entity.getClient() != null) {
            dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
        }
        if (entity.getDevisDetail() != null) {
            dto.setDevisDetail(entity.getDevisDetail().stream().map(this::devisDetailConvertToDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setDevisDetail(null);
        }

        return dto;
    }

}
