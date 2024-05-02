package backend.rest_api.gestion_facturation.rapports.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisEntity;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisDetailRepository;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DevisRapportService {

    private final DevisRepository devisRepository;
    private final DevisDetailRepository devisDetailRepository;

    private List<LinkedHashMap<String, Object>> getlistesDetailDevis(
            List<DevisDetailEntity> entityList) {
        LinkedHashMap<String, Object> hashMap = null;
        List<DevisDetailEntity> entities = entityList;
        List<LinkedHashMap<String, Object>> hashMaps = null;
        hashMaps = new ArrayList<>();

        for (DevisDetailEntity each : entities) {

            hashMap = new LinkedHashMap<>();
            hashMap.put("devis", each.getDevis());
            hashMap.put("client", ClientMapper.getInstance()
                    .convertToDto(each.getDevis().getClient()));
            hashMap.put("detailService", each.getServiceDetail().getDesignation());
            hashMap.put("quantite", each.getQuantite());
            hashMap.put("prix", each.getPrixUnitHt());
            hashMap.put("tva", each.getTauxTva());
            hashMap.put("prixTotal", each.getPrixTotal());

            if (devisDetailRepository
                    .montantTotalDevisHT(each.getIdDevis()) != null
                    || devisDetailRepository.montantTotalDevisHT(
                            each.getIdDevis()) == 0) {
                hashMap.put("montantTotal", new BigDecimal(devisDetailRepository
                        .montantTotalDevisHT(each.getIdDevis())));
            } else {
                hashMap.put("montantTotal", 0.0);
            }

            hashMaps.add(hashMap);
        }
        return hashMaps;
    }

    public LinkedHashMap<String, Object> getEtatDevisService() {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<DevisEntity> saisieInventaire = devisRepository
                .findAll();

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (DevisEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<DevisDetailEntity> saisieInventaireDetail = devisDetailRepository
                    .getByDevis(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("devisDetail", getlistesDetailDevis(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportDevis", maps);

        return hashMap;
    }

    public LinkedHashMap<String, Object> getEtatDevisByDateOperationService(Date dateOperation) {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<DevisEntity> saisieInventaire = devisRepository
                .getAllByDateOperation(dateOperation);

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (DevisEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<DevisDetailEntity> saisieInventaireDetail = devisDetailRepository
                    .getByDevis(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("devisDetail", getlistesDetailDevis(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportDevis", maps);

        return hashMap;
    }

    public LinkedHashMap<String, Object> getEtatDevisEntreDateOperationService(Date dateOperationDebut,
            Date dateOperationFin) {

        LinkedHashMap<String, Object> hashMap = new LinkedHashMap<>();

        List<DevisEntity> saisieInventaire = devisRepository
                .getAllByEntreDateOperation(dateOperationDebut, dateOperationFin);

        List<LinkedHashMap<String, Object>> maps = null;
        maps = new ArrayList<>();

        for (DevisEntity saisieInventaireEntity : saisieInventaire) {

            LinkedHashMap<String, Object> hashMap1 = new LinkedHashMap<>();

            List<DevisDetailEntity> saisieInventaireDetail = devisDetailRepository
                    .getByDevis(saisieInventaireEntity.getId());

            if (saisieInventaireDetail.size() > 0) {

                hashMap1.put("devisDetail", getlistesDetailDevis(saisieInventaireDetail));

                maps.add(hashMap1);
            }
        }

        hashMap.put("rapportDevis", maps);

        return hashMap;
    }

}
