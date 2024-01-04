package backend.rest_api.gestion_facturation.gestionServices.mapper;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDetailDTO;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;

@Component
public class ServiceDetailMapper {

    public ServiceDetailMapper() {
    }

    public static ServiceDetailMapper getInstance() {
        return new ServiceDetailMapper();
    }

    public ServiceDetailEntity convertToEntity(ServiceDetailDTO dto) {

        ServiceDetailEntity entity = new ServiceDetailEntity();

        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDesignation(dto.getDesignation());
        entity.setUniteMesure(dto.getUniteMesure());
        if (dto.getService() != null) {
            entity.setIdService(dto.getService().getId());
        }

        return entity;
    }

    public ServiceDetailDTO convertToDto(ServiceDetailEntity entity) {

        ServiceDetailDTO dto = new ServiceDetailDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDesignation(entity.getDesignation());
        dto.setUniteMesure(entity.getUniteMesure());

        return dto;

    }
}
