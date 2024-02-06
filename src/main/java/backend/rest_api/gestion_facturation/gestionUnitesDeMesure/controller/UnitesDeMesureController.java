package backend.rest_api.gestion_facturation.gestionUnitesDeMesure.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.dto.UnitesDeMesureDTO;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.entity.UnitesDeMesureEntity;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.mapper.UnitesDeMesureMapper;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.repository.UnitesDeMesureRepository;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.service.UnitesDeMesureService;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/unites_de_mesure")
public class UnitesDeMesureController {

    @Autowired
    private UnitesDeMesureService service;

    @Autowired
    UnitesDeMesureRepository repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getUniteAchatVenteController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Map<String, Object> unite = service.getAll(title, page - 1, size, sort);

        if (unite.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), unite, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getByIdUniteAchatVenteController(@PathVariable(name = "id", required = true) Long id) {
        UnitesDeMesureDTO dto = service.getById(id);
        if (dto != null) {
            return new ResponseEntity<>(new ResponseHelper(dto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterUniteAchatVenteController(@RequestBody UnitesDeMesureDTO uniteDto) {

        UnitesDeMesureEntity familleArticleEntity = UnitesDeMesureMapper.getInstance()
                .convertToEntity(uniteDto);

        if (repository.existsByCode(familleArticleEntity.getCode())) {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.dataExist("code"), true),
                    HttpStatus.BAD_REQUEST);
        } else {
            UnitesDeMesureDTO familleArticleDtos = service.ajoutUniteAchatVenteService(uniteDto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), familleArticleDtos, true),
                    HttpStatus.CREATED);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> modifierUniteAchatVenteController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody UnitesDeMesureDTO dto) {

        Optional<UnitesDeMesureEntity> idOptional = repository.findById(id);

        Optional<UnitesDeMesureEntity> codeExist = repository.verificationCode(id,
                dto.getCode());

        if (idOptional.isPresent()) {

            if (codeExist.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseHelper(("code " + dto.getCode() + " exist"), true),
                        HttpStatus.BAD_REQUEST);
            } else {
                UnitesDeMesureDTO dto2 = service.updateUniteAchatVenteService(id,
                        dto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Unité d'Achat et de Vente"),
                                dto2,
                                true),
                        HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.notFound("Unité d'Achat et de Vente avec id: " + id), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerUniteAchatVenteController(@PathVariable("id") Long id) {
        Optional<UnitesDeMesureEntity> idOptional = repository.findById(id);

        try {
            if (idOptional.isPresent()) {
                repository.deleteById(id);
                return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseHelper(
                        MessageHelper.notFound("ID"), false), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper
                    .internalServer(), false),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
