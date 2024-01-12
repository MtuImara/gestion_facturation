package backend.rest_api.gestion_facturation.gestionTVA.mapper;

import backend.rest_api.gestion_facturation.gestionTVA.dto.TauxTvaDto;
import backend.rest_api.gestion_facturation.gestionTVA.entity.TauxTvaEntity;

public class TauxTvaMapper {

  public TauxTvaMapper(){};

  public static TauxTvaMapper getInstance(){
    return new TauxTvaMapper();
  }

  public TauxTvaEntity toEntity(TauxTvaDto dto){

    TauxTvaEntity entity = new TauxTvaEntity();
    entity.setId(dto.getId());
    entity.setCode(dto.getCode());
    entity.setLibelle(dto.getLibelle());
    entity.setTaux(dto.getTaux());
    entity.setTypeTva(dto.getTypeTva());
    return entity;
  }

  public TauxTvaDto toDto(TauxTvaEntity entity){
    TauxTvaDto dto = new TauxTvaDto();

    dto.setId(entity.getId());
    dto.setCode(entity.getCode());
    dto.setLibelle(entity.getLibelle());
    dto.setTaux(entity.getTaux());
    dto.setTypeTva(entity.getTypeTva());
    return dto;
  }

}
