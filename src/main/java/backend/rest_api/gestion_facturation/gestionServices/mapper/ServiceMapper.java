package backend.rest_api.gestion_facturation.gestionServices.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDetailDTO;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceEntity;

@Component
public class ServiceMapper {

    public ServiceMapper() {
    }

    public static ServiceMapper getInstance() {
        return new ServiceMapper();
    }

    public ServiceEntity convertToEntity(ServiceDTO dto) {

        ServiceEntity entity = new ServiceEntity();

        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDesignation(dto.getDesignation());
        entity.setCommentaire(dto.getCommentaire());

        return entity;

    }

    public ServiceDetailDTO serviceDetailConvertToDto(ServiceDetailEntity entity) {

        ServiceDetailDTO dto = new ServiceDetailDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDesignation(entity.getDesignation());
        dto.setUniteMesure(entity.getUniteMesure());

        return dto;

    }

    public ServiceDTO convertToDto(ServiceEntity entity) {

        ServiceDTO dto = new ServiceDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDesignation(entity.getDesignation());
        dto.setCommentaire(entity.getCommentaire());

        if (entity.getServiceDetail() != null) {
            dto.setServiceDetail(entity.getServiceDetail().stream().map(this::serviceDetailConvertToDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setServiceDetail(null);
        }

        return dto;

    }

}
