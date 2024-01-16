package backend.rest_api.gestion_facturation.gestionClient.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.rest_api.gestion_facturation.gestionClient.dto.ClientDTO;
import backend.rest_api.gestion_facturation.gestionClient.entity.ClientEntity;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionClient.repository.ClientRepository;
import backend.rest_api.gestion_facturation.gestionClient.service.ClientService;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gestion_client")
public class ClientController {

    private final ClientService clientService;

    private final ClientRepository clientRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getAllClientController(@RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            @Or({
                    @Spec(path = "code", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "nom", params = "name", spec = LikeIgnoreCase.class) }) Specification<ClientEntity> specClient) {

        Map<String, Object> client = clientService.getAll(title, page - 1, size, sort, specClient);

        if (client.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(), client, true),
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.noContent()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getByIdClientController(@PathVariable(name = "id", required = true) Long id) {
        ClientDTO clientDto = clientService.getById(id);
        if (clientDto != null) {
            return new ResponseEntity<>(new ResponseHelper(clientDto, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.notFound(), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> ajouterClientController(@RequestBody ClientDTO clientDto) {

        ClientEntity clientEntity = ClientMapper.getInstance()
                .convertToEntity(clientDto);

        if (clientRepository.existsByCode(clientEntity.getCode())) {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.dataExist("code"), true),
                    HttpStatus.BAD_REQUEST);
        } else {
            ClientDTO dtos = clientService
                    .ajoutClientService(clientDto);
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.createdSuccessfully(), dtos, true),
                    HttpStatus.CREATED);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> modifierClientController(@PathVariable(name = "id", required = true) Long id,
            @RequestBody ClientDTO clientDto) {

        Optional<ClientEntity> idOptional = clientRepository.findById(id);

        Optional<ClientEntity> codeExist = clientRepository.verificationCode(id,
                clientDto.getCode());

        if (idOptional.isPresent()) {

            if (codeExist.isPresent()) {
                return new ResponseEntity<>(
                        new ResponseHelper(("code " + clientDto.getCode() + " exist"), true),
                        HttpStatus.BAD_REQUEST);
            } else {
                ClientDTO clientDto2 = clientService.updateClientService(id,
                        clientDto);

                return new ResponseEntity<>(
                        new ResponseHelper(MessageHelper.updatedSuccessfully("Client"), clientDto2,
                                true),
                        HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>(
                    new ResponseHelper(MessageHelper.notFound("Client avec id: " + id), false),
                    HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> supprimerClientController(@PathVariable("id") Long id) {
        Optional<ClientEntity> idOptional = clientRepository.findById(id);

        try {
            if (idOptional.isPresent()) {
                clientRepository.deleteById(id);
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
