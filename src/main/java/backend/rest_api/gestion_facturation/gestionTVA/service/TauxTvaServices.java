package backend.rest_api.gestion_facturation.gestionTVA.service;

import backend.rest_api.gestion_facturation.gestionTVA.dto.TauxTvaDto;
import backend.rest_api.gestion_facturation.gestionTVA.entity.TauxTva;
import backend.rest_api.gestion_facturation.gestionTVA.mapper.TauxTvaMapper;
import backend.rest_api.gestion_facturation.gestionTVA.repository.TauxTvaRepository;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TauxTvaServices {
  private static final Logger LOG = LoggerFactory
          .getLogger(TauxTvaServices.class);

  @Autowired
  private TauxTvaRepository tauxTvaRepository;

  public TauxTvaDto getById(Long id) {
    TauxTvaDto tauxTvaDto = null;
    TauxTva tauxTva = null;
    try {
      tauxTva = tauxTvaRepository.findById(id).
              orElseThrow(()->new IllegalArgumentException(MessageHelper.dataExist(""+id)));
      tauxTvaDto = TauxTvaMapper.getInstance().toDto(tauxTva);
      
    } catch (Exception e) {
      tauxTvaDto = null;
      LOG.error(e.getMessage());
    }
    return tauxTvaDto;
  }

  public List<TauxTvaDto> getAll() {

    TauxTvaDto tauxTvaDto = null;
    List<TauxTvaDto> tauxTvaDtos = new ArrayList<>();
    List<TauxTva> tauxTvaEntities = tauxTvaRepository.findAll();
    try {
      for (TauxTva entity : tauxTvaEntities) {
        tauxTvaDto = TauxTvaMapper.getInstance().toDto(entity);        
        tauxTvaDtos.add(tauxTvaDto);
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
    }
    return tauxTvaDtos;
  }
  public TauxTvaDto create(TauxTvaDto dto) {
    try {
      TauxTva tauxEntity = TauxTvaMapper.getInstance()
              .toEntity(dto);
      TauxTva fromBd = tauxTvaRepository.save(tauxEntity);
      TauxTvaDto tauxTvaDto = TauxTvaMapper.getInstance().toDto(fromBd);
    } catch (Exception e) {
      dto = null;
      LOG.error(e.getMessage());
    }
    return dto;
  }



  public TauxTvaDto update(Long id, TauxTvaDto dto) {

    TauxTva tauxTva = null;
    try {
      tauxTva = tauxTvaRepository.findById(id).
              orElseThrow(()->new IllegalArgumentException(MessageHelper.dataExist(""+id)));
      dto.setId(tauxTva.getId());
      tauxTva = TauxTvaMapper.getInstance().toEntity(dto);
      TauxTva updated = tauxTvaRepository.save(tauxTva);
      dto = TauxTvaMapper.getInstance().toDto(updated);
    } catch (Exception e) {
      dto = null;
    }
    return dto;
  }

  public boolean deleteById(Long id) {
    boolean result = false;
    try {
      TauxTvaDto tauxTvaDto = getById(id);
      if (tauxTvaDto != null) {
        tauxTvaRepository.deleteById(id);
        result = true;
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
    }
    return result;
  }

  public List<TauxTvaDto> filtrage(String code, String libelle, Double taux, Long typeTva){
    TauxTvaDto dto = null;
    List<TauxTvaDto> listDto = new ArrayList<>();
    List<TauxTva> listEntities = new ArrayList<>();
    try {
      if (code != null && libelle == null && taux == null && typeTva == null)
        listEntities = tauxTvaRepository.findByCodeContainingKeywordAnywhere(code);
      else if(code == null && libelle != null && taux == null && typeTva == null)
        listEntities = tauxTvaRepository.findByLibelleContainingKeywordAnywhere(libelle);
      else if(code == null && libelle == null && taux != null && typeTva == null)
        listEntities = tauxTvaRepository.findByTauxContainingKeywordAnywhere(taux);
      else if(code == null && libelle == null && taux == null && typeTva != null)
        listEntities = tauxTvaRepository.findByTypeTvaContainingKeywordAnywhere(typeTva);
      else if(code == null && libelle == null && taux == null && typeTva == null)
        listEntities = tauxTvaRepository.findAll();
      for(TauxTva en : listEntities) {
        dto = TauxTvaMapper.getInstance().toDto(en);
        listDto.add(dto);
      }
    } catch (Exception e) {
      listDto = null;
      LOG.error(e.getMessage());
    }
    return listDto;
  }


  public List<TauxTvaDto> search(String statut){
    List<TauxTva> entities = tauxTvaRepository.search(statut);
    List<TauxTvaDto> tvaDtos = new ArrayList<>();
    TauxTvaDto dto = null;
    try {
      if (statut != null) {
        for(TauxTva en : entities) {
          dto = TauxTvaMapper.getInstance().toDto(en);
          
          tvaDtos.add(dto);
        }
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
      tvaDtos = null;
    }
    return tvaDtos;

  }
}
