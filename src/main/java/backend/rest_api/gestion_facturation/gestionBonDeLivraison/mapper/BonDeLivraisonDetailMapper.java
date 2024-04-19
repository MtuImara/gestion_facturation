package backend.rest_api.gestion_facturation.gestionBonDeLivraison.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto.BonDeLivraisonDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;

@Component
public class BonDeLivraisonDetailMapper {

    public BonDeLivraisonDetailMapper() {
    }

    public static BonDeLivraisonDetailMapper getInstance() {
        return new BonDeLivraisonDetailMapper();
    }

    public BonDeLivraisonDetailEntity convertToEntity(BonDeLivraisonDetailDTO dto) {

        BonDeLivraisonDetailEntity entity = new BonDeLivraisonDetailEntity();

        entity.setId(dto.getId());
        if (dto.getServiceDetail() != null) {
            entity.setIdServiceDetail(dto.getServiceDetail().getId());
        }
        entity.setIdBonDeLivraison(dto.getIdBonDeLivraison());
        entity.setIdServiceDetail(dto.getIdServiceDetail());
        entity.setDesignation(dto.getDesignation());
        entity.setQuantite(dto.getQuantite());
        entity.setPrixUnitHt(dto.getPrixUnitHt());
        entity.setTauxTva(dto.getTauxTva());
        if (dto.getTauxTva() != null || dto.getTauxTva() == 0) {
            entity.setPrixTotal(new BigDecimal((dto.getQuantite() * dto.getPrixUnitHt())
                    + ((dto.getQuantite() * dto.getPrixUnitHt()) * dto.getTauxTva() / 100)));
        } else {
            entity.setPrixTotal(new BigDecimal((dto.getQuantite() * dto.getPrixUnitHt())));
        }

        return entity;

    }

    public BonDeLivraisonDetailDTO convertToDto(BonDeLivraisonDetailEntity entity) {

        BonDeLivraisonDetailDTO dto = new BonDeLivraisonDetailDTO();

        dto.setId(entity.getId());
        if (entity.getServiceDetail() != null) {
            dto.setServiceDetail(ServiceDetailMapper.getInstance()
                    .convertToDto(entity.getServiceDetail()));
        }
        dto.setIdServiceDetail(entity.getIdServiceDetail());
        dto.setIdBonDeLivraison(entity.getIdBonDeLivraison());
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixUnitHt(entity.getPrixUnitHt());
        if (entity.getTauxTva() != null) {
            dto.setTauxTva(entity.getTauxTva());
        } else {
            dto.setTauxTva(0.0);
        }
        dto.setMontantHt(entity.getPrixTotal());

        return dto;

    }

}
