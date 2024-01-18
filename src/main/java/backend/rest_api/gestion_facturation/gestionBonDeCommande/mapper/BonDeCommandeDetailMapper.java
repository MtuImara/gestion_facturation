package backend.rest_api.gestion_facturation.gestionBonDeCommande.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;

@Component
public class BonDeCommandeDetailMapper {

    public BonDeCommandeDetailMapper() {
    }

    public static BonDeCommandeDetailMapper getInstance() {
        return new BonDeCommandeDetailMapper();
    }

    public BonDeCommandeDetailEntity convertToEntity(BonDeCommandeDetailDTO dto) {

        BonDeCommandeDetailEntity entity = new BonDeCommandeDetailEntity();

        entity.setId(dto.getId());
        if (dto.getService() != null) {
            entity.setIdService(dto.getService().getId());
        }
        entity.setDesignation(dto.getDesignation());
        entity.setQuantite(dto.getQuantite());
        entity.setPrixUnitHt(dto.getPrixUnitHt());

        return entity;

    }

    public BonDeCommandeDetailDTO convertToDto(BonDeCommandeDetailEntity entity) {

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

}
