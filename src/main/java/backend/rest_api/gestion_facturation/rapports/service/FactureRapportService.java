package backend.rest_api.gestion_facturation.rapports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
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

        private List<LinkedHashMap<String, Object>> getlistesDetailFacture(
                        List<FactureDetailEntity> entityList) {
                LinkedHashMap<String, Object> hashMap = null;
                List<FactureDetailEntity> entities = entityList;
                List<LinkedHashMap<String, Object>> hashMaps = null;
                hashMaps = new ArrayList<>();

                for (FactureDetailEntity each : entities) {

                        hashMap = new LinkedHashMap<>();
                        hashMap.put("facture", each.getFacture());
                        hashMap.put("client", ClientMapper.getInstance()
                                        .convertToDto(each.getFacture().getClient()));
                        hashMap.put("detailService", each.getServiceDetail().getDesignation());
                        hashMap.put("quantite", each.getQuantite());
                        hashMap.put("prix", each.getPrixUnitHt());
                        hashMap.put("tva", each.getTauxTva());
                        hashMap.put("prixTotal", each.getPrixTotal());

                        if (factureDetailRepository
                                        .montantTotalFactureHT(each.getIdFacture()) != null
                                        || factureDetailRepository.montantTotalFactureHT(
                                                        each.getIdFacture()) == 0) {
                                hashMap.put("montantTotal", new BigDecimal(factureDetailRepository
                                                .montantTotalFactureHT(each.getIdFacture())));
                        } else {
                                hashMap.put("montantTotal", 0.0);
                        }

                        hashMaps.add(hashMap);
                }
                return hashMaps;
        }

        public LinkedHashMap<String, Object> getEtatFactureService() {

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
                                // hashMap1.put("code", saisieInventaireEntity.getCode());
                                // hashMap1.put("client", ClientMapper.getInstance()
                                // .convertToDto(saisieInventaireEntity.getClient()));
                                // hashMap1.put("service",
                                // saisieInventaireEntity.getService().getDesignation());
                                // hashMap1.put("etat", saisieInventaireEntity.getEtat());
                                // hashMap1.put("commentaire", saisieInventaireEntity.getCommentaire());
                                // hashMap1.put("dateOperation", saisieInventaireEntity.getDateOperation());
                                // hashMap1.put("dateEcheance", saisieInventaireEntity.getDateEcheance());

                                hashMap1.put("factureDetail", getlistesDetailFacture(saisieInventaireDetail));

                                maps.add(hashMap1);
                        }
                }

                hashMap.put("rapportFacture", maps);

                return hashMap;
        }

        public LinkedHashMap<String, Object> getEtatFactureByDateOperationService(Date dateOperation) {

                LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

                List<FactureEntity> saisieInventaire = factureRepository
                                .getAllByDateOperation(dateOperation);

                List<LinkedHashMap<String, Object>> maps = null;
                maps = new ArrayList<>();

                for (FactureEntity saisieInventaireEntity : saisieInventaire) {

                        LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

                        List<FactureDetailEntity> saisieInventaireDetail = factureDetailRepository
                                        .getByFacture(saisieInventaireEntity.getId());

                        if (saisieInventaireDetail.size() > 0) {

                                hashMap1.put("factureDetail", getlistesDetailFacture(saisieInventaireDetail));

                                maps.add(hashMap1);
                        }
                }

                hashMap.put("rapportFacture", maps);

                return hashMap;
        }

        public LinkedHashMap<String, Object> getEtatFactureEntreDateOperationService(Date dateOperationDebut,
                        Date dateOperationFin) {

                LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

                List<FactureEntity> saisieInventaire = factureRepository
                                .getAllByEntreDateOperation(dateOperationDebut, dateOperationFin);

                List<LinkedHashMap<String, Object>> maps = null;
                maps = new ArrayList<>();

                for (FactureEntity saisieInventaireEntity : saisieInventaire) {

                        LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

                        List<FactureDetailEntity> saisieInventaireDetail = factureDetailRepository
                                        .getByFacture(saisieInventaireEntity.getId());

                        if (saisieInventaireDetail.size() > 0) {

                                hashMap1.put("factureDetail", getlistesDetailFacture(saisieInventaireDetail));

                                maps.add(hashMap1);
                        }
                }

                hashMap.put("rapportFacture", maps);

                return hashMap;
        }

}
