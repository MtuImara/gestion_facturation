package backend.rest_api.gestion_facturation.rapports.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import backend.rest_api.gestion_facturation.rapports.service.FactureRapportService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rapport_facture")
public class FactureRapportController {

    private final FactureRapportService factureRapportService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getEtatSaisieInventaireStandard(@RequestParam(required = false) Integer idArticle) {

        Map<String, Object> data = factureRapportService.getEtatFactureService();

        if (data.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(),
                    data, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(
                    MessageHelper.noContent(), false), HttpStatus.OK);
        }

    }

}
