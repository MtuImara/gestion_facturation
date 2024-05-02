package backend.rest_api.gestion_facturation.rapports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonDetailEntity;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonEntity;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.repository.BonDeLivraisonDetailRepository;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.repository.BonDeLivraisonRepository;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BonDeLivraisonRapportService {

    private final BonDeLivraisonRepository bonDeLivraisonRepository;
    private final BonDeLivraisonDetailRepository bonDeLivraisonDetailRepository;

    private List<LinkedHashMap<String, Object>> getlistesDetailBonDeLivraison(
            List<BonDeLivraisonDetailEntity> entityList) {
        LinkedHashMap<String, Object> hashMap = null;
        List<BonDeLivraisonDetailEntity> entities = entityList;
        List<LinkedHashMap<String, Object>> hashMaps = null;
        hashMaps = new ArrayList<>();

        for (BonDeLivraisonDetailEntity each : entities) {

            hashMap = new LinkedHashMap<>();
            hashMap.put("bonDeLivraison", each.getBonDeLivraison());
            hashMap.put("client", ClientMapper.getInstance()
                    .convertToDto(each.getBonDeLivraison().getClient()));
            hashMap.put("detailService", each.getServiceDetail().getDesignation());
            hashMap.put("quantite", each.getQuantite());
            hashMap.put("prix", each.getPrixUnitHt());
            hashMap.put("tva", each.getTauxTva());
            hashMap.put("prixTotal", each.getPrixTotal());

            if (bonDeLivraisonDetailRepository
                    .montantTotalBonDeLivraisonHT(each.getIdBonDeLivraison()) != null
                    || bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(
                            each.getIdBonDeLivraison()) == 0) {
                hashMap.put("montantTotal", new BigDecimal(bonDeLivraisonDetailRepository
                        .montantTotalBonDeLivraisonHT(each.getIdBonDeLivraison())));
            } else {
                hashMap.put("montantTotal", 0.0);
            }

            hashMaps.add(hashMap);
        }
        return hashMaps;
    }

    public LinkedHashMap<String, Object> getEtatBonDeLivraisonService() {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<BonDeLivraisonEntity> saisieInventaire = bonDeLivraisonRepository
                .findAll();

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (BonDeLivraisonEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<BonDeLivraisonDetailEntity> saisieInventaireDetail = bonDeLivraisonDetailRepository
                    .getByBonDeLivraison(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("bonDeLivraisonDetail", getlistesDetailBonDeLivraison(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportBonDeLivraison", maps);

        return hashMap;
    }

    public LinkedHashMap<String, Object> getEtatBonDeLivraisonByDateOperationService(Date dateOperation) {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<BonDeLivraisonEntity> saisieInventaire = bonDeLivraisonRepository
                .getAllByDateOperation(dateOperation);

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (BonDeLivraisonEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<BonDeLivraisonDetailEntity> saisieInventaireDetail = bonDeLivraisonDetailRepository
                    .getByBonDeLivraison(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("bonDeLivraisonDetail", getlistesDetailBonDeLivraison(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportBonDeLivraison", maps);

        return hashMap;
    }

    public LinkedHashMap<String, Object> getEtatBonDeLivraisonEntreDateOperationService(Date dateOperationDebut,
            Date dateOperationFin) {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<BonDeLivraisonEntity> saisieInventaire = bonDeLivraisonRepository
                .getAllByEntreDateOperation(dateOperationDebut, dateOperationFin);

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (BonDeLivraisonEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<BonDeLivraisonDetailEntity> saisieInventaireDetail = bonDeLivraisonDetailRepository
                    .getByBonDeLivraison(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("bonDeLivraisonDetail", getlistesDetailBonDeLivraison(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportBonDeLivraison", maps);

        return hashMap;
    }

}
