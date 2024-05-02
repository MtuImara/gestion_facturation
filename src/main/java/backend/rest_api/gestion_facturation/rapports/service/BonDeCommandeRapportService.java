package backend.rest_api.gestion_facturation.rapports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeDetailEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.repository.BonDeCommandeDetailRepository;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.repository.BonDeCommandeRepository;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BonDeCommandeRapportService {

    private final BonDeCommandeRepository bonDeCommandeRepository;
    private final BonDeCommandeDetailRepository bonDeCommandeDetailRepository;

    private List<LinkedHashMap<String, Object>> getlistesDetailBonDeCommande(
            List<BonDeCommandeDetailEntity> entityList) {
        LinkedHashMap<String, Object> hashMap = null;
        List<BonDeCommandeDetailEntity> entities = entityList;
        List<LinkedHashMap<String, Object>> hashMaps = null;
        hashMaps = new ArrayList<>();

        for (BonDeCommandeDetailEntity each : entities) {

            hashMap = new LinkedHashMap<>();
            hashMap.put("bonDeCommande", each.getBonDeCommande());
            hashMap.put("client", ClientMapper.getInstance()
                    .convertToDto(each.getBonDeCommande().getClient()));
            hashMap.put("detailService", each.getServiceDetail().getDesignation());
            hashMap.put("quantite", each.getQuantite());
            hashMap.put("prix", each.getPrixUnitHt());
            hashMap.put("tva", each.getTauxTva());
            hashMap.put("prixTotal", each.getPrixTotal());

            if (bonDeCommandeDetailRepository
                    .montantTotalBonDeCommandeHT(each.getIdBonDeCommande()) != null
                    || bonDeCommandeDetailRepository.montantTotalBonDeCommandeHT(
                            each.getIdBonDeCommande()) == 0) {
                hashMap.put("montantTotal", new BigDecimal(bonDeCommandeDetailRepository
                        .montantTotalBonDeCommandeHT(each.getIdBonDeCommande())));
            } else {
                hashMap.put("montantTotal", 0.0);
            }

            hashMaps.add(hashMap);
        }
        return hashMaps;
    }

    public LinkedHashMap<String, Object> getEtatBonDeCommandeService() {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<BonDeCommandeEntity> saisieInventaire = bonDeCommandeRepository
                .findAll();

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (BonDeCommandeEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<BonDeCommandeDetailEntity> saisieInventaireDetail = bonDeCommandeDetailRepository
                    .getByBonDeCommande(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("bonDeCommandeDetail", getlistesDetailBonDeCommande(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportBonDeCommande", maps);

        return hashMap;
    }

    public LinkedHashMap<String, Object> getEtatBonDeCommandeByDateOperationService(Date dateOperation) {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<BonDeCommandeEntity> saisieInventaire = bonDeCommandeRepository
                .getAllByDateOperation(dateOperation);

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (BonDeCommandeEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<BonDeCommandeDetailEntity> saisieInventaireDetail = bonDeCommandeDetailRepository
                    .getByBonDeCommande(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("bonDeCommandeDetail", getlistesDetailBonDeCommande(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportBonDeCommande", maps);

        return hashMap;
    }

    public LinkedHashMap<String, Object> getEtatBonDeCommandeEntreDateOperationService(Date dateOperationDebut,
            Date dateOperationFin) {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<BonDeCommandeEntity> saisieInventaire = bonDeCommandeRepository
                .getAllByEntreDateOperation(dateOperationDebut, dateOperationFin);

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (BonDeCommandeEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<BonDeCommandeDetailEntity> saisieInventaireDetail = bonDeCommandeDetailRepository
                    .getByBonDeCommande(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("bonDeCommandeDetail", getlistesDetailBonDeCommande(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportBonDeCommande", maps);

        return hashMap;
    }

}
