package backend.rest_api.gestion_facturation.gestionBonDeLivraison.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto.BonDeLivraisonDTO;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonEntity;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.mapper.BonDeLivraisonMapper;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.repository.BonDeLivraisonRepository;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.service.BonDeLivraisonService;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gestion_de_bon_de_livraison")
public class BonDeLivraisonController {

    private final BonDeLivraisonService bonDeLivraisonService;
    private final BonDeLivraisonRepository bonDeLivraisonRepository;

    @GetMapping(value = "/type_de_statut_de_bon_de_livraison")
    public ResponseEntity<?> getTypeStatut() {
        List<StaticValue> types = bonDeLivraisonService.typeStatut();
        if (types.isEmpty()) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent(), false), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), types, true), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllBonDeLivraisonController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @Or({
                    @Spec(path = "code", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "denominationClient", params = "name", spec = LikeIgnoreCase.class) }) Specification<BonDeLivraisonEntity> specBonDeLivraison) {

        Map<String, Object> bonDeLivraison = bonDeLivraisonService.getAllBonDeLivraison(title, page - 1, size, sort,
                specBonDeLivraison);

        if (bonDeLivraison.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), bonDeLivraison, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getByIdBonDeLivraisonController(@PathVariable(name = "id", required = true) Long id) {
        BonDeLivraisonDTO bonDeLivraisonDto = bonDeLivraisonService.getById(id);
        if (bonDeLivraisonDto != null) {
            return new ResponseEntity<>(new ResponseHelper(bonDeLivraisonDto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> ajouterBonDeLivraisonController(@RequestBody BonDeLivraisonDTO bonDeLivraisonDto) {

        BonDeLivraisonEntity bonDeLivraisonEntity = BonDeLivraisonMapper.getInstance()
                .convertToEntity(bonDeLivraisonDto);

        if (bonDeLivraisonRepository.existsByCode(bonDeLivraisonEntity.getCode())) {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.dataExist("code"), true),
                    HttpStatus.BAD_REQUEST);
        } else {
            BonDeLivraisonDTO dtos = bonDeLivraisonService
                    .ajoutBonDeLivraisonService(bonDeLivraisonDto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), dtos, true),
                    HttpStatus.CREATED);
        }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> modifierBonDeLivraisonController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody BonDeLivraisonDTO bonDeLivraisonDto) {

        Optional<BonDeLivraisonEntity> idOptional = bonDeLivraisonRepository.findById(id);

        Optional<BonDeLivraisonEntity> codeExist = bonDeLivraisonRepository.verificationCode(id,
                bonDeLivraisonDto.getCode());

        if (idOptional.isPresent()) {

            if (codeExist.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseHelper(("code " + bonDeLivraisonDto.getCode() + " exist"), true),
                        HttpStatus.BAD_REQUEST);
            } else {
                BonDeLivraisonDTO bonDeLivraisonDto2 = bonDeLivraisonService.modificationBonDeLivraisonService(id,
                        bonDeLivraisonDto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Bon De Livraison"), bonDeLivraisonDto2,
                                true),
                        HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.notFound("Bon De Livraison avec id: " + id), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerBonDeLivraisonController(@PathVariable("id") Long id) {
        Optional<BonDeLivraisonEntity> idOptional = bonDeLivraisonRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                bonDeLivraisonRepository.deleteById(id);
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
