package backend.rest_api.gestion_facturation.gestionDevis.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDetailDTO;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;

@Component
public class DevisDetailMapper {

    public DevisDetailMapper() {
    }

    public static DevisDetailMapper getInstance() {
        return new DevisDetailMapper();
    }

    public DevisDetailEntity convertToEntity(DevisDetailDTO dto) {

        DevisDetailEntity entity = new DevisDetailEntity();

        entity.setId(dto.getId());
        if (dto.getService() != null) {
            entity.setIdService(dto.getService().getId());
        }
        entity.setDesignation(dto.getDesignation());
        entity.setQuantite(dto.getQuantite());
        entity.setPrixUnitHt(dto.getPrixUnitHt());

        return entity;

    }

    public DevisDetailDTO convertToDto(DevisDetailEntity entity) {

        DevisDetailDTO dto = new DevisDetailDTO();

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
