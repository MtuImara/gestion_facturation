package backend.rest_api.gestion_facturation.gestionServices.controller;

import java.util.Map;
import java.util.Optional;

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

import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDetailDTO;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.gestionServices.repository.ServiceDetailRepository;
import backend.rest_api.gestion_facturation.gestionServices.repository.ServiceRepository;
import backend.rest_api.gestion_facturation.gestionServices.service.GestionServicesService;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gestion_de_services")
public class GestionServicesController {

    private final GestionServicesService gestionServicesService;
    private final ServiceRepository serviceRepository;
    private final ServiceDetailRepository serviceDetailRepository;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllServicesController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Map<String, Object> mouvementsDeStock = gestionServicesService.getAll(title, page - 1, size,
                sort);

        if (mouvementsDeStock.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), mouvementsDeStock, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getByIdServiceController(
            @PathVariable(name = "id", required = true) Long id) {
        ServiceDTO dto = gestionServicesService.getById(id);
        if (dto != null) {
            return new ResponseEntity<>(new ResponseHelper(dto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "detail/{id}")
    public ResponseEntity<?> getByIdDetailServiceController(
            @PathVariable(name = "id", required = true) Long id) {
        ServiceDetailDTO dto = gestionServicesService.getServiceDetailById(id);
        if (dto != null) {
            return new ResponseEntity<>(new ResponseHelper(dto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> ajouterServicesController(@RequestBody ServiceDTO dto) {

        ServiceEntity entity = ServiceMapper.getInstance()
                .convertToEntity(dto);

        if (serviceRepository.existsByCode(entity.getCode())) {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.dataExist("code"), false),
                    HttpStatus.BAD_REQUEST);
        } else {
            ServiceDTO mouvementsDeStockDtos = gestionServicesService
                    .ajouter(dto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), mouvementsDeStockDtos, true),
                    HttpStatus.CREATED);
        }

    }

    @PostMapping(value = "/ajout_detail")
    public ResponseEntity<?> ajouterDetailServicesController(@RequestBody ServiceDetailDTO dto) {

        ServiceDetailEntity entity = ServiceDetailMapper.getInstance()
                .convertToEntity(dto);

        // if (serviceRepository.existsByCode(entity.getCode())) {
        // return new ResponseEntity<>(
        // new ResponseHelper(MessageHelper.dataExist("code"), false),
        // HttpStatus.BAD_REQUEST);
        // } else {
        ServiceDetailDTO serviceDetail = gestionServicesService
                .ajoutServiceDetailService(dto);
        return new ResponseEntity<>(
                new ResponseHelper(MessageHelper.createdSuccessfully(), serviceDetail, true),
                HttpStatus.CREATED);
        // }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> modifierServicesController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody ServiceDTO dto) {

        Optional<ServiceEntity> serviceIdOptional = serviceRepository.findById(id);

        Optional<ServiceEntity> codeExist = serviceRepository.verificationCode(id,
                dto.getCode());

        if (serviceIdOptional.isPresent()) {

            if (codeExist.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseHelper(("code " + dto.getCode() + " exist"), false),
                        HttpStatus.BAD_REQUEST);
            } else {
                ServiceDTO serviceDto = gestionServicesService.update(id,
                        dto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Service"),
                                serviceDto,
                                true),
                        HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.notFound("id: " + id), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/modefier_detail/{id}")
    public ResponseEntity<?> modifierServicesDetailController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody ServiceDetailDTO dto) {

        // Optional<ServiceEntity> serviceIdOptional = serviceRepository.findById(id);

        // Optional<ServiceEntity> codeExist = serviceRepository.verificationCode(id,
        // dto.getCode());

        // if (serviceIdOptional.isPresent()) {

        // if (codeExist.isPresent()) {
        // return new ResponseEntity<>(
        // new ResponseHelper(("code " + dto.getCode() + " exist"), false),
        // HttpStatus.BAD_REQUEST);
        // } else {
        ServiceDetailDTO serviceDto = gestionServicesService.updateServiceDetail(id,
                dto);

        return new ResponseEntity<>(
                new ResponseHelper(MessageHelper.updatedSuccessfully("Service"),
                        serviceDto,
                        true),
                HttpStatus.OK);
        // }

        // } else {
        // return new ResponseEntity<>(
        // new ResponseHelper(MessageHelper.notFound("id: " + id), false),
        // HttpStatus.NOT_FOUND);
        // }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerServicesController(@PathVariable("id") Long id) {
        Optional<ServiceEntity> idOptional = serviceRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                serviceRepository.deleteById(id);
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

    @RequestMapping(value = "/delete_detail/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerServicesDetailController(@PathVariable("id") Long id) {
        Optional<ServiceDetailEntity> idOptional = serviceDetailRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                serviceDetailRepository.deleteById(id);
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

    @GetMapping(value = "/service_detail")
    public ResponseEntity<?> getAllServiceDetailController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Map<String, Object> facture = gestionServicesService.getAllServiceDetail(title, page - 1, size, sort);

        if (facture.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), facture, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public long countRecords() {
        return serviceDetailRepository.count();
    }
}
