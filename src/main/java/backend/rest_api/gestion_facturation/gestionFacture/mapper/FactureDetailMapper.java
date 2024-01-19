package backend.rest_api.gestion_facturation.gestionFacture.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDetailDTO;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;

@Component
public class FactureDetailMapper {

    public FactureDetailMapper() {
    }

    public static FactureDetailMapper getInstance() {
        return new FactureDetailMapper();
    }

    public FactureDetailEntity convertToEntity(FactureDetailDTO dto) {

        FactureDetailEntity entity = new FactureDetailEntity();

        entity.setId(dto.getId());
        if (dto.getService() != null) {
            entity.setIdService(dto.getService().getId());
        }
        entity.setDesignation(dto.getDesignation());
        entity.setQuantite(dto.getQuantite());
        entity.setPrixUnitHt(dto.getPrixUnitHt());

        return entity;

    }

    public FactureDetailDTO convertToDto(FactureDetailEntity entity) {

        FactureDetailDTO dto = new FactureDetailDTO();

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
