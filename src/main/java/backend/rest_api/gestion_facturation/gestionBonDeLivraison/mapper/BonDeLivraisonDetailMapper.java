package backend.rest_api.gestion_facturation.gestionBonDeLivraison.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto.BonDeLivraisonDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonDetailEntity;
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
        if (dto.getService() != null) {
            entity.setIdService(dto.getService().getId());
        }
        entity.setDesignation(dto.getDesignation());
        entity.setQuantite(dto.getQuantite());
        entity.setPrixUnitHt(dto.getPrixUnitHt());

        return entity;

    }

    public BonDeLivraisonDetailDTO convertToDto(BonDeLivraisonDetailEntity entity) {

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

}
