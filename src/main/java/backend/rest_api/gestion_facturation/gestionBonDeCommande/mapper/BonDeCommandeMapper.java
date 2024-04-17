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
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
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
        entity.setEtat(dto.getEtat());
        // if (dto.getTypeStatut() != null) {
        // entity.setTypeStatut(Integer.parseInt(dto.getTypeStatut().getKey()));
        // }
        entity.setDateOperation(DateHelper.toDate(dto.getDateOperation()));
        entity.setDateCreation(DateHelper.toDate(dto.getDateCreation()));
        entity.setDateModification(DateHelper.toDate(dto.getDateModification()));
        // if (dto.getClient() != null) {
        // entity.setIdClient(dto.getClient().getId());
        // }
        entity.setIdClient(dto.getIdClient());
        // if (dto.getService() != null) {
        // entity.setIdService(dto.getService().getId());
        // }
        entity.setIdService(dto.getId_service());
        // if (dto.getTva() != null) {
        // entity.setIdTva(dto.getTva().getId());
        // }
        // entity.setTauxTva(dto.getTauxTva());

        return entity;
    }

    public BonDeCommandeDetailDTO bonDeCommandeDetailConvertToDto(BonDeCommandeDetailEntity entity) {

        BonDeCommandeDetailDTO dto = new BonDeCommandeDetailDTO();

        dto.setId(entity.getId());
        if (entity.getServiceDetail() != null) {
            dto.setServiceDetail(ServiceDetailMapper.getInstance()
                    .convertToDto(entity.getServiceDetail()));
        }
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixUnitHt(entity.getPrixUnitHt());

        dto.setMontantHt(new BigDecimal(dto.getQuantite() * dto.getPrixUnitHt()));

        return dto;

    }

    public BonDeCommandeDTO convertToDto(BonDeCommandeEntity entity) {

        BonDeCommandeDTO dto = new BonDeCommandeDTO();

        // StaticValue staticValStatut = new StaticValue();
        // StaticListOfValues listOfValuesStatut = new StaticListOfValues();
        // staticValStatut.setKey(
        // listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() -
        // 1).getKey().trim());
        // staticValStatut.setValue(
        // listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() -
        // 1).getValue());

        dto.setId(entity.getId());

        dto.setCode(entity.getCode());
        dto.setReference(entity.getReference());
        dto.setDenominationClient(entity.getDenominationClient());
        dto.setCommentaire(entity.getCommentaire());
        dto.setEtat(entity.getEtat());
        // if (staticValStatut != null) {
        // dto.setTypeStatut(staticValStatut);
        // } else {
        // dto.setTypeStatut(null);
        // }
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
        // if (entity.getClient().getAssujettiTva() == true) {
        // dto.setTauxTva(entity.getTauxTva());
        // } else {
        // dto.setTauxTva(null);
        // }

        // if (entity.getClient() != null && entity.getClient().getAssujettiTva() ==
        // true) {
        // dto.setMontantTotalTTC(
        // new BigDecimal(
        // (dto.getTauxTva() *
        // factureDetailRepository.montantTotalFactureHT(dto.getId())) / 100));
        // } else {
        // dto.setMontantTotalHT(new
        // BigDecimal(factureDetailRepository.montantTotalFactureHT(dto.getId())));
        // }

        return dto;
    }

}
