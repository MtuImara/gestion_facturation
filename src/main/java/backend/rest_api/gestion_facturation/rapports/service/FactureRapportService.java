package backend.rest_api.gestion_facturation.rapports.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureDetailEntity;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureEntity;
import backend.rest_api.gestion_facturation.gestionFacture.repository.FactureDetailRepository;
import backend.rest_api.gestion_facturation.gestionFacture.repository.FactureRepository;
import backend.rest_api.gestion_facturation.gestionFacture.service.FactureService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FactureRapportService {

        private final FactureRepository factureRepository;
        private final FactureDetailRepository factureDetailRepository;
        private final FactureService factureService;

        public LinkedHashMap<String, Object> getEtatInventaireService() {

                LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

                List<FactureEntity> saisieInventaire = factureRepository
                                .findAll();

                List<LinkedHashMap<String, Object>> maps = null;
                maps = new ArrayList<>();

                for (FactureEntity saisieInventaireEntity : saisieInventaire) {

                        LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

                        List<FactureDetailEntity> saisieInventaireDetail = factureDetailRepository
                                        .getByFacture(saisieInventaireEntity.getId());

                        if (saisieInventaireDetail.size() > 0) {
                                hashMap1.put("code", saisieInventaireEntity.getCode());
                                hashMap1.put("service", saisieInventaireEntity.getService().getDesignation());
                                hashMap1.put("etat", saisieInventaireEntity.getEtat());
                                hashMap1.put("commentaire", saisieInventaireEntity.getCommentaire());
                                hashMap1.put("dateOperation", saisieInventaireEntity.getDateOperation());
                                hashMap1.put("dateEcheance", saisieInventaireEntity.getDateEcheance());
                                // hashMap1.put("dateSaisieInventaire",
                                // saisieInventaireEntity.getDateSaisieInventaire());

                                // hashMap1.put("factureDetail",
                                // getlistesDetailSaisieInventaire(saisieInventaireDetail));

                                maps.add(hashMap1);
                        }
                }

                hashMap.put("mouvementsDeStock", maps);

                return hashMap;
        }

}
