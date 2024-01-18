package backend.rest_api.gestion_facturation.gestionBonDeCommande.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.constantes.StaticListOfValues;
import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeDetailEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.repository.BonDeCommandeDetailRepository;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;

@Component
public class BonDeCommandeMapper {

    public BonDeCommandeMapper() {
    }

    public static BonDeCommandeMapper getInstance() {
        return new BonDeCommandeMapper();
    }

    @Autowired
    BonDeCommandeDetailRepository bonDeCommandeDetailRepository;

    public BonDeCommandeEntity convertToEntity(BonDeCommandeDTO dto) {

        BonDeCommandeEntity entity = new BonDeCommandeEntity();

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
        if (dto.getTva() != null) {
            entity.setIdTva(dto.getTva().getId());
        }
        if (dto.getClient().getAssujettiTva() == true) {
            entity.setTauxTva(dto.getTva().getTaux());
        }

        return entity;
    }

    public BonDeCommandeDetailDTO bonDeCommandeDetailConvertToDto(BonDeCommandeDetailEntity entity) {

        BonDeCommandeDetailDTO dto = new BonDeCommandeDetailDTO();

        dto.setId(entity.getId());
        if (entity.getService() != null) {
            dto.setService(ServiceMapper.getInstance()
                    .convertToDto(entity.getService()));
        }
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixUnitHt(entity.getPrixUnitHt());

        dto.setMontantHt(new BigDecimal(dto.getQuantite() * dto.getPrixUnitHt()));

        return dto;

    }

    public BonDeCommandeDTO convertToDto(BonDeCommandeEntity entity) {

        BonDeCommandeDTO dto = new BonDeCommandeDTO();

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
        if (entity.getBonDeCommandeDetail() != null) {
            dto.setBonDeCommandeDetail(
                    entity.getBonDeCommandeDetail().stream().map(this::bonDeCommandeDetailConvertToDto)
                            .collect(Collectors.toList()));
        } else {
            dto.setBonDeCommandeDetail(null);
        }
        if (entity.getClient().getAssujettiTva() == true) {
            dto.setTauxTva(entity.getTauxTva());
        } else {
            dto.setTauxTva(null);
        }
        if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
            dto.setMontantTotalTTC(
                    new BigDecimal(
                            (dto.getTauxTva() * bonDeCommandeDetailRepository.montantTotalBonDeCommandeHT(dto.getId()))
                                    / 100));
        } else {
            dto.setMontantTotalHT(
                    new BigDecimal(bonDeCommandeDetailRepository.montantTotalBonDeCommandeHT(dto.getId())));
        }

        return dto;
    }

}
