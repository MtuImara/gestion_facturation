package backend.rest_api.gestion_facturation.gestionDevis.contoller;

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
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDTO;
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDetailDTO;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisEntity;
import backend.rest_api.gestion_facturation.gestionDevis.mapper.DevisDetailMapper;
import backend.rest_api.gestion_facturation.gestionDevis.mapper.DevisMapper;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisDetailRepository;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisRepository;
import backend.rest_api.gestion_facturation.gestionDevis.service.DevisService;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDetailDTO;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gestion_de_devis")
public class DevisController {

    private final DevisService devisService;
    private final DevisRepository devisRepository;
    private final DevisDetailRepository devisDetailRepository;

    @GetMapping(value = "/type_de_statut_du_devis")
    public ResponseEntity<?> getTypeStatut() {
        List<StaticValue> types = devisService.typeStatut();
        if (types.isEmpty()) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent(), false), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), types, true), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllDervisController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @Or({
                    @Spec(path = "code", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "denominationClient", params = "name", spec = LikeIgnoreCase.class) }) Specification<DevisEntity> specDevis) {

        Map<String, Object> devis = devisService.getAll(title, page - 1, size, sort, specDevis);

        if (devis.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), devis, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getByIdDevisController(@PathVariable(name = "id", required = true) Long id) {
        DevisDTO devisDto = devisService.getById(id);
        if (devisDto != null) {
            return new ResponseEntity<>(new ResponseHelper(devisDto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<?> getByIdDevisDetailController(@PathVariable(name = "id", required = true) Long id) {
        DevisDetailDTO devisDetailDto = devisService.getDevisDetailById(id);
        if (devisDetailDto != null) {
            return new ResponseEntity<>(new ResponseHelper(devisDetailDto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> ajouterDevisController(@RequestBody DevisDTO devisDto) {

        DevisEntity devisEntity = DevisMapper.getInstance()
                .convertToEntity(devisDto);

        if (devisRepository.existsByCode(devisEntity.getCode())) {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.dataExist("code"), true),
                    HttpStatus.BAD_REQUEST);
        } else {
            DevisDTO dtos = devisService
                    .ajouter(devisDto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), dtos, true),
                    HttpStatus.CREATED);
        }

    }

    @PostMapping(value = "/ajout_detail")
    public ResponseEntity<?> ajouterDetailDevisController(@RequestBody DevisDetailDTO dto) {

        DevisDetailEntity entity = DevisDetailMapper.getInstance()
                .convertToEntity(dto);

        // if (serviceRepository.existsByCode(entity.getCode())) {
        //     return new ResponseEntity<>(
        //             new ResponseHelper(MessageHelper.dataExist("code"), false),
        //             HttpStatus.BAD_REQUEST);
        // } else {
            DevisDetailDTO devisDetail = devisService
                    .ajoutDevisDetailService(dto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), devisDetail, true),
                    HttpStatus.CREATED);
        // }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> modifierDevisController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody DevisDTO devisDto) {

        Optional<DevisEntity> idOptional = devisRepository.findById(id);

        Optional<DevisEntity> codeExist = devisRepository.verificationCode(id,
                devisDto.getCode());

        if (idOptional.isPresent()) {

            if (codeExist.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseHelper(("code " + devisDto.getCode() + " exist"), true),
                        HttpStatus.BAD_REQUEST);
            } else {
                DevisDTO devisDto2 = devisService.update(id,
                        devisDto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Devis"), devisDto2,
                                true),
                        HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.notFound("Devis avec id: " + id), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerDevisController(@PathVariable("id") Long id) {
        Optional<DevisEntity> idOptional = devisRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                devisRepository.deleteById(id);
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

    @PutMapping(value = "/modefier_detail/{id}")
    public ResponseEntity<?> modifierDevisDetailController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody DevisDetailDTO dto) {

        // Optional<ServiceEntity> serviceIdOptional = serviceRepository.findById(id);

        // Optional<ServiceEntity> codeExist = serviceRepository.verificationCode(id,
        //         dto.getCode());

        // if (serviceIdOptional.isPresent()) {

        //     if (codeExist.isPresent()) {
        //         return new ResponseEntity<>(
        //                 new ResponseHelper(("code " + dto.getCode() + " exist"), false),
        //                 HttpStatus.BAD_REQUEST);
        //     } else {
            DevisDetailDTO serviceDto = devisService.updateDevisDetail(id,
                        dto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Detail Service"),
                                serviceDto,
                                true),
                        HttpStatus.OK);
    //         }

    //     } else {
    //         return new ResponseEntity<>(
    //                 new ResponseHelper(MessageHelper.notFound("id: " + id), false),
    //                 HttpStatus.NOT_FOUND);
    //     }
    }

    @RequestMapping(value = "/delete_detail/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerDevisDetailController(@PathVariable("id") Long id) {
        Optional<DevisDetailEntity> idOptional = devisDetailRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                devisDetailRepository.deleteById(id);
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
