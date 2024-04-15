package backend.rest_api.gestion_facturation.gestionFacture.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDTO;
import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDetailDTO;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureDetailEntity;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureEntity;
import backend.rest_api.gestion_facturation.gestionFacture.repository.FactureDetailRepository;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;

@Component
public class FactureMapper {

    public FactureMapper() {
    }

    public static FactureMapper getInstance() {
        return new FactureMapper();
    }

    @Autowired
    FactureDetailRepository factureDetailRepository;

    public FactureEntity convertToEntity(FactureDTO dto) {

        FactureEntity entity = new FactureEntity();

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
        entity.setDateEcheance(DateHelper.toDate(dto.getDateEcheance()));
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

    public FactureDetailDTO factureDetailConvertToDto(FactureDetailEntity entity) {

        FactureDetailDTO dto = new FactureDetailDTO();

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

    public FactureDTO convertToDto(FactureEntity entity) {

        FactureDTO dto = new FactureDTO();

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
        dto.setDateEcheance(DateHelper.toText(entity.getDateEcheance(), "time"));
        dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
        dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
        if (entity.getClient() != null) {
            dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
        }
        if (entity.getFactureDetail() != null) {
            dto.setFactureDetail(entity.getFactureDetail().stream().map(this::factureDetailConvertToDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setFactureDetail(null);
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
