package backend.rest_api.gestion_facturation.rapports.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.rest_api.gestion_facturation.helpers.DateHelper;
import backend.rest_api.gestion_facturation.helpers.MessageHelper;
import backend.rest_api.gestion_facturation.helpers.ResponseHelper;
import backend.rest_api.gestion_facturation.rapports.service.BonDeLivraisonRapportService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rapport_bon_de_livraison")
public class BonDeLivraisonRapportController {

    private final BonDeLivraisonRapportService bonDeLivraisonRapportService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getEtatBonDeLivraison(@RequestParam(required = false) String dateOperationDebut,
            @RequestParam(required = false) String dateOperationFin) {

        if (dateOperationDebut != null && dateOperationFin != null) {

            Map<String, Object> data = bonDeLivraisonRapportService
                    .getEtatBonDeLivraisonEntreDateOperationService(DateHelper.toDate(dateOperationDebut),
                            DateHelper.toDate(dateOperationFin));

            if (data.size() > 0) {
                return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(),
                        data, true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseHelper(
                        MessageHelper.noContent(), false), HttpStatus.OK);
            }

        } else if (dateOperationDebut != null && dateOperationFin == null) {
            Map<String, Object> data = bonDeLivraisonRapportService
                    .getEtatBonDeLivraisonByDateOperationService(DateHelper.toDate(dateOperationDebut));

            if (data.size() > 0) {
                return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(),
                        data, true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseHelper(
                        MessageHelper.noContent(), false), HttpStatus.OK);
            }
        }

        Map<String, Object> data = bonDeLivraisonRapportService.getEtatBonDeLivraisonService();

        if (data.size() > 0) {
            return new ResponseEntity<>(new ResponseHelper(MessageHelper.success(),
                    data, true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseHelper(
                    MessageHelper.noContent(), false), HttpStatus.OK);
        }

    }

}
