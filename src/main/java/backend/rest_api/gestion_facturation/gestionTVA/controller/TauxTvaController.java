package backend.rest_api.gestion_facturation.gestionTVA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.rest_api.gestion_facturation.gestionTVA.dto.TauxTvaDto;
import backend.rest_api.gestion_facturation.gestionTVA.entity.TauxTva;
import backend.rest_api.gestion_facturation.gestionTVA.repository.TauxTvaRepository;
import backend.rest_api.gestion_facturation.gestionTVA.service.TauxTvaServices;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/taux_tvas")
public class TauxTvaController {

  @Autowired
  private TauxTvaServices tauxTvaService;
  @Autowired
  private TauxTvaRepository tauxTvaRepository;


  @GetMapping("/")
  public ResponseEntity<Object> getAll(@RequestParam(name = "statut", required = false) String statut) {

    List<TauxTvaDto> searchDts = tauxTvaService.search(statut);
    List<TauxTvaDto> tauxTvaDts = tauxTvaService.getAll();
    try {
      if (tauxTvaDts != null) {
        if (statut != null)
          return new ResponseEntity<>(new ResponseHelper(null, searchDts, true), HttpStatus.OK);
        else {
          return new ResponseEntity<>(new ResponseHelper(null, tauxTvaDts, true), HttpStatus.OK);
        }
      }else {
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.NO_CONTENT);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseHelper(MessageHelper.internalServer(), false),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(@PathVariable(name = "id", required = true) Long id) {
    TauxTvaDto tauxTvaDto = tauxTvaService.getById(id);
    if (tauxTvaDto != null) {
      return new ResponseEntity<>(new ResponseHelper(null, tauxTvaDto, true), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.NO_CONTENT);
    }

  }
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable(name = "id", required = true) Long id) {

    if (tauxTvaService.deleteById(id)) {
      return new ResponseEntity<>(new ResponseHelper(MessageHelper.deletedSuccessFully(), true), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.NO_CONTENT);
    }
  }
  @PostMapping(value = "/")
  public ResponseEntity<Object> create(@RequestBody TauxTvaDto dto){

    try {
      if (tauxTvaRepository.existsByCode(dto.getCode()))
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.dataExist("code"), false),
                HttpStatus.BAD_REQUEST);
      TauxTvaDto tauxTvadto = tauxTvaService.create(dto);
      if (tauxTvadto != null) {
        return new ResponseEntity<>(new ResponseHelper(tauxTvadto, true), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseHelper(MessageHelper.internalServer(e.getMessage())),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody TauxTvaDto dto) throws Exception {
    Optional<TauxTva> codeExist = tauxTvaRepository.verificationCode(id, dto.getCode());
    if (codeExist.isPresent())
      return new ResponseEntity<>(new ResponseHelper(("code " + dto.getCode() + " exist"), true),
              HttpStatus.BAD_REQUEST);
    try {
      TauxTvaDto tauxTvaDto = tauxTvaService.update(id, dto);

      if (tauxTvaDto != null) {
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.updatedSuccessFully(), tauxTvaDto, true),
                HttpStatus.OK);
      } else {
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.NO_CONTENT);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(new ResponseHelper(MessageHelper.dataExist(e.getMessage())),
              HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/filtrage")
  public ResponseEntity<Object> getFiltrage(@RequestParam(name = "code", required = false) String code,
                                       @RequestParam(name = "libelle", required = false) String libelle,
                                       @RequestParam(name = "taux", required = false) Double taux,
                                       @RequestParam(name = "typeTva", required = false) Long typeTva) {
    List<TauxTvaDto> listDtos = tauxTvaService.filtrage(code, libelle, taux, typeTva);
    if (listDtos != null)
      return new ResponseEntity<>(new ResponseHelper(null, listDtos, true), HttpStatus.OK);
    else
      return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.NO_CONTENT);
  }
}
