package backend.rest_api.gestion_facturation.gestionUnitesDeMesure.mapper;

import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.dto.UnitesDeMesureDTO;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.entity.UnitesDeMesureEntity;

public class UnitesDeMesureMapper {

    public UnitesDeMesureMapper() {
    }

    public static UnitesDeMesureMapper getInstance() {
        return new UnitesDeMesureMapper();
    }

    public UnitesDeMesureEntity convertToEntity(UnitesDeMesureDTO dto) {

        UnitesDeMesureEntity entity = new UnitesDeMesureEntity();

        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setDesignation(dto.getDesignation());
        entity.setValeur(dto.getValeur());

        return entity;
    }

    public UnitesDeMesureDTO convertToDto(UnitesDeMesureEntity entity) {
        UnitesDeMesureDTO dto = new UnitesDeMesureDTO();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDesignation(entity.getDesignation());
        dto.setValeur(entity.getValeur());

        return dto;
    }
}
