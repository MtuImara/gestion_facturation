package backend.rest_api.gestion_facturation.gestionBonDeCommande.controller;

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
import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeDetailEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.mapper.BonDeCommandeDetailMapper;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.mapper.BonDeCommandeMapper;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.repository.BonDeCommandeDetailRepository;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.repository.BonDeCommandeRepository;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.service.BonDeCommandeService;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gestion_de_bon_de_commande")
public class BonDeCommandeController {

    private final BonDeCommandeService bonDeCommandeService;
    private final BonDeCommandeRepository bonDeCommandeRepository;
    private final BonDeCommandeDetailRepository bonDeCommandeDetailRepository;

    @GetMapping(value = "/type_de_statut_du_bon_de_commande")
    public ResponseEntity<?> getTypeStatut() {
        List<StaticValue> types = bonDeCommandeService.typeStatut();
        if (types.isEmpty()) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent(), false), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), types, true), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllBonDeCommandeController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @Or({
                    @Spec(path = "code", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "denominationClient", params = "name", spec = LikeIgnoreCase.class) }) Specification<BonDeCommandeEntity> specBonDeCommande) {

        Map<String, Object> bonDeCommande = bonDeCommandeService.getBonDeCommandeAll(title, page - 1, size, sort,
                specBonDeCommande);

        if (bonDeCommande.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), bonDeCommande, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getByIdBonDeCommandeController(@PathVariable(name = "id", required = true) Long id) {
        BonDeCommandeDTO bonDeCommandeDto = bonDeCommandeService.getById(id);
        if (bonDeCommandeDto != null) {
            return new ResponseEntity<>(new ResponseHelper(bonDeCommandeDto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> ajouterBonDeCommandeController(@RequestBody BonDeCommandeDTO bonDeCommandeDto) {

        BonDeCommandeEntity bonDeCommandeEntity = BonDeCommandeMapper.getInstance()
                .convertToEntity(bonDeCommandeDto);

        if (bonDeCommandeRepository.existsByCode(bonDeCommandeEntity.getCode())) {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.dataExist("code"), true),
                    HttpStatus.BAD_REQUEST);
        } else {
            BonDeCommandeDTO dtos = bonDeCommandeService
                    .ajouter(bonDeCommandeDto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), dtos, true),
                    HttpStatus.CREATED);
        }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> modifierBonDeCommandeController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody BonDeCommandeDTO bonDeCommandeDto) {

        Optional<BonDeCommandeEntity> idOptional = bonDeCommandeRepository.findById(id);

        Optional<BonDeCommandeEntity> codeExist = bonDeCommandeRepository.verificationCode(id,
                bonDeCommandeDto.getCode());

        if (idOptional.isPresent()) {

            if (codeExist.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseHelper(("code " + bonDeCommandeDto.getCode() + " exist"), true),
                        HttpStatus.BAD_REQUEST);
            } else {
                BonDeCommandeDTO bonDeCommandeDto2 = bonDeCommandeService.update(id,
                        bonDeCommandeDto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Bon De Commande"), bonDeCommandeDto2,
                                true),
                        HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.notFound("Bon De Commande avec id: " + id), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerBonDeCommandeController(@PathVariable("id") Long id) {
        Optional<BonDeCommandeEntity> idOptional = bonDeCommandeRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                bonDeCommandeRepository.deleteById(id);
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
    public ResponseEntity<?> ajouterDetailBonDeCommandeController(@RequestBody BonDeCommandeDetailDTO dto) {

        BonDeCommandeDetailEntity entity = BonDeCommandeDetailMapper.getInstance()
                .convertToEntity(dto);

        // if (serviceRepository.existsByCode(entity.getCode())) {
        // return new ResponseEntity<>(
        // new ResponseHelper(MessageHelper.dataExist("code"), false),
        // HttpStatus.BAD_REQUEST);
        // } else {
        BonDeCommandeDetailDTO bonDeCommandeDetail = bonDeCommandeService
                .ajoutBonDeCommandeDetailService(dto);
        return new ResponseEntity<>(
                new ResponseHelper(MessageHelper.createdSuccessfully(), bonDeCommandeDetail, true),
                HttpStatus.CREATED);
        // }
    }

    @PutMapping(value = "/modefier_detail/{id}")
    public ResponseEntity<?> modifierBonDeCommandeDetailController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody BonDeCommandeDetailDTO dto) {

        // Optional<ServiceEntity> serviceIdOptional = serviceRepository.findById(id);

        // Optional<ServiceEntity> codeExist = serviceRepository.verificationCode(id,
        // dto.getCode());

        // if (serviceIdOptional.isPresent()) {

        // if (codeExist.isPresent()) {
        // return new ResponseEntity<>(
        // new ResponseHelper(("code " + dto.getCode() + " exist"), false),
        // HttpStatus.BAD_REQUEST);
        // } else {
        BonDeCommandeDetailDTO bonDeCommandeDto = bonDeCommandeService.updateBonDeCommandeDetail(id,
                dto);

        return new ResponseEntity<>(
                new ResponseHelper(MessageHelper.updatedSuccessfully("Detail BonDeCommande"),
                        bonDeCommandeDto,
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
    public ResponseEntity<?> supprimerBonDeCommandeDetailController(@PathVariable("id") Long id) {
        Optional<BonDeCommandeDetailEntity> idOptional = bonDeCommandeDetailRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                bonDeCommandeDetailRepository.deleteById(id);
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

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public long countRecords() {
        return bonDeCommandeRepository.count();
    }

}
