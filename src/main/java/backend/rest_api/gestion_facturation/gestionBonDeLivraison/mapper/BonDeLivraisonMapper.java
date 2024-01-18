package backend.rest_api.gestion_facturation.gestionBonDeLivraison.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.constantes.StaticListOfValues;
import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto.BonDeLivraisonDTO;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto.BonDeLivraisonDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonDetailEntity;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonEntity;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.repository.BonDeLivraisonDetailRepository;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;

@Component
public class BonDeLivraisonMapper {

    public BonDeLivraisonMapper() {
    }

    public static BonDeLivraisonMapper getInstance() {
        return new BonDeLivraisonMapper();
    }

    @Autowired
    BonDeLivraisonDetailRepository bonDeLivraisonDetailRepository;

    public BonDeLivraisonEntity convertToEntity(BonDeLivraisonDTO dto) {

        BonDeLivraisonEntity entity = new BonDeLivraisonEntity();

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

    public BonDeLivraisonDetailDTO bonDeLivraisonDetailConvertToDto(BonDeLivraisonDetailEntity entity) {

        BonDeLivraisonDetailDTO dto = new BonDeLivraisonDetailDTO();

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

    public BonDeLivraisonDTO convertToDto(BonDeLivraisonEntity entity) {

        BonDeLivraisonDTO dto = new BonDeLivraisonDTO();

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
        if (entity.getBonDeLivraisonDetail() != null) {
            dto.setBonDeLivraisonDetail(
                    entity.getBonDeLivraisonDetail().stream().map(this::bonDeLivraisonDetailConvertToDto)
                            .collect(Collectors.toList()));
        } else {
            dto.setBonDeLivraisonDetail(null);
        }
        if (entity.getClient().getAssujettiTva() == true) {
            dto.setTauxTva(entity.getTauxTva());
        } else {
            dto.setTauxTva(null);
        }
        if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
            dto.setMontantTotalTTC(
                    new BigDecimal((dto.getTauxTva()
                            * bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId())) / 100));
        } else {
            dto.setMontantTotalHT(
                    new BigDecimal(bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId())));
        }

        return dto;
    }

}
