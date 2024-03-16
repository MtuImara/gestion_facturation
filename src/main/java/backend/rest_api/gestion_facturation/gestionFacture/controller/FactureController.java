package backend.rest_api.gestion_facturation.gestionFacture.controller;

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
import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDTO;
import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDetailDTO;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureDetailEntity;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureEntity;
import backend.rest_api.gestion_facturation.gestionFacture.mapper.FactureDetailMapper;
import backend.rest_api.gestion_facturation.gestionFacture.mapper.FactureMapper;
import backend.rest_api.gestion_facturation.gestionFacture.repository.FactureDetailRepository;
import backend.rest_api.gestion_facturation.gestionFacture.repository.FactureRepository;
import backend.rest_api.gestion_facturation.gestionFacture.service.FactureService;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gestion_de_facture")
public class FactureController {

    private final FactureService factureService;
    private final FactureRepository factureRepository;
    private final FactureDetailRepository factureDetailRepository;

    @GetMapping(value = "/type_de_statut_du_facture")
    public ResponseEntity<?> getTypeStatut() {
        List<StaticValue> types = factureService.typeStatut();
        if (types.isEmpty()) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent(), false), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), types, true), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllFactureController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @Or({
                    @Spec(path = "code", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "denominationClient", params = "name", spec = LikeIgnoreCase.class) }) Specification<FactureEntity> specFacture) {

        Map<String, Object> facture = factureService.getAllFactureService(title, page - 1, size, sort, specFacture);

        if (facture.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), facture, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getByIdFactureController(@PathVariable(name = "id", required = true) Long id) {
        FactureDTO factureDto = factureService.getById(id);
        if (factureDto != null) {
            return new ResponseEntity<>(new ResponseHelper(factureDto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> ajouterFactureController(@RequestBody FactureDTO factureDto) {

        FactureEntity factureEntity = FactureMapper.getInstance()
                .convertToEntity(factureDto);

        if (factureRepository.existsByCode(factureEntity.getCode())) {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.dataExist("code"), true),
                    HttpStatus.BAD_REQUEST);
        } else {
            FactureDTO dtos = factureService
                    .ajouter(factureDto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), dtos, true),
                    HttpStatus.CREATED);
        }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> modifierFactureController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody FactureDTO factureDto) {

        Optional<FactureEntity> idOptional = factureRepository.findById(id);

        Optional<FactureEntity> codeExist = factureRepository.verificationCode(id,
                factureDto.getCode());

        if (idOptional.isPresent()) {

            if (codeExist.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseHelper(("code " + factureDto.getCode() + " exist"), true),
                        HttpStatus.BAD_REQUEST);
            } else {
                FactureDTO factureDto2 = factureService.update(id,
                        factureDto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Facture"), factureDto2,
                                true),
                        HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.notFound("Facture avec id: " + id), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerFactureController(@PathVariable("id") Long id) {
        Optional<FactureEntity> idOptional = factureRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                factureRepository.deleteById(id);
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

    @PostMapping(value = "/ajout_detail")
    public ResponseEntity<?> ajouterDetailFactureController(@RequestBody FactureDetailDTO dto) {

        FactureDetailEntity entity = FactureDetailMapper.getInstance()
                .convertToEntity(dto);

        // if (serviceRepository.existsByCode(entity.getCode())) {
        // return new ResponseEntity<>(
        // new ResponseHelper(MessageHelper.dataExist("code"), false),
        // HttpStatus.BAD_REQUEST);
        // } else {
        FactureDetailDTO factureDetail = factureService
                .ajoutFactureDetailService(dto);
        return new ResponseEntity<>(
                new ResponseHelper(MessageHelper.createdSuccessfully(), factureDetail, true),
                HttpStatus.CREATED);
        // }
    }

    @PutMapping(value = "/modefier_detail/{id}")
    public ResponseEntity<?> modifierFactureDetailController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody FactureDetailDTO dto) {

        // Optional<ServiceEntity> serviceIdOptional = serviceRepository.findById(id);

        // Optional<ServiceEntity> codeExist = serviceRepository.verificationCode(id,
        // dto.getCode());

        // if (serviceIdOptional.isPresent()) {

        // if (codeExist.isPresent()) {
        // return new ResponseEntity<>(
        // new ResponseHelper(("code " + dto.getCode() + " exist"), false),
        // HttpStatus.BAD_REQUEST);
        // } else {
        FactureDetailDTO factureDto = factureService.updateFactureDetail(id,
                dto);

        return new ResponseEntity<>(
                new ResponseHelper(MessageHelper.updatedSuccessfully("Detail Facture"),
                        factureDto,
                        true),
                HttpStatus.OK);
        // }

        // } else {
        // return new ResponseEntity<>(
        // new ResponseHelper(MessageHelper.notFound("id: " + id), false),
        // HttpStatus.NOT_FOUND);
        // }
    }

    @RequestMapping(value = "/delete_detail/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerFactureDetailController(@PathVariable("id") Long id) {
        Optional<FactureDetailEntity> idOptional = factureDetailRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                factureDetailRepository.deleteById(id);
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
