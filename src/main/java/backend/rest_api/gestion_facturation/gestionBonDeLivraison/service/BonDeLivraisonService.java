package backend.rest_api.gestion_facturation.gestionBonDeLivraison.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.constantes.StaticListOfValues;
import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto.BonDeLivraisonDTO;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto.BonDeLivraisonDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonDetailEntity;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonEntity;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.mapper.BonDeLivraisonDetailMapper;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.mapper.BonDeLivraisonMapper;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.repository.BonDeLivraisonDetailRepository;
import backend.rest_api.gestion_facturation.gestionBonDeLivraison.repository.BonDeLivraisonRepository;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;
import backend.rest_api.gestion_facturation.helpers.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BonDeLivraisonService {

    private final BonDeLivraisonRepository bonDeLivraisonRepository;
    private final BonDeLivraisonDetailRepository bonDeLivraisonDetailRepository;

    public List<StaticValue> typeStatut() {

        StaticListOfValues StaticListOfValues = new StaticListOfValues();
        List<StaticValue> list_of_values = StaticListOfValues.getTypeStatut();

        return list_of_values;
    }

    public BonDeLivraisonDetailDTO bonDeLivraisonDetailConvertToDto(BonDeLivraisonDetailEntity entity) {

        BonDeLivraisonDetailDTO dto = new BonDeLivraisonDetailDTO();

        dto.setId(entity.getId());
        if (entity.getService() != null) {
            dto.setService(ServiceMapper.getInstance()
                    .convertToDto(entity.getService()));
        }
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixUnitHt(entity.getPrixUnitHt());

        dto.setMontantHt(new BigDecimal(dto.getQuantite() * dto.getPrixUnitHt()));

        return dto;

    }

    public Map<String, Object> getAllBonDeLivraison(String title, int page, int size, String[] sort,
            Specification<BonDeLivraisonEntity> spec) {

        Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

        Page<BonDeLivraisonEntity> bonDeLivraisonEntity = null;

        if (title == null || title.equals("")) {
            bonDeLivraisonEntity = bonDeLivraisonRepository.findAll(spec, pagingSort);
        } else {
        }

        List<BonDeLivraisonDTO> dtos = new ArrayList<>();

        bonDeLivraisonEntity.forEach(entity -> {

            BonDeLivraisonDTO dto = new BonDeLivraisonDTO();

            StaticValue staticValStatut = new StaticValue();
            StaticListOfValues listOfValuesStatut = new StaticListOfValues();
            staticValStatut.setKey(
                    listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getKey()
                            .trim());
            staticValStatut.setValue(
                    listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getValue());

            dto.setId(entity.getId());

            dto.setCode(entity.getCode());
            dto.setReference(entity.getReference());
            dto.setDenominationClient(entity.getDenominationClient());
            dto.setCommentaire(entity.getCommentaire());
            dto.setTypeStatut(staticValStatut);
            dto.setDateOperation(DateHelper.toText(entity.getDateOperation(), "time"));
            dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
            dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
            // if (entity.getClient() != null) {
            // dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
            // }
            if (entity.getBonDeLivraisonDetail() != null) {
                dto.setBonDeLivraisonDetail(
                        entity.getBonDeLivraisonDetail().stream().map(this::bonDeLivraisonDetailConvertToDto)
                                .collect(Collectors.toList()));
            } else {
                dto.setBonDeLivraisonDetail(null);
            }
            if (entity.getClient().getAssujettiTva() == true) {
                dto.setTauxTva(entity.getTauxTva());
            } else {
                dto.setTauxTva(null);
            }
            if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
                dto.setMontantTotalTTC(
                        new BigDecimal((dto.getTauxTva() * bonDeLivraisonDetailRepository
                                .montantTotalBonDeLivraisonHT(dto.getId())) / 100));
            } else {
                dto.setMontantTotalHT(
                        new BigDecimal(bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId())));
            }

            dtos.add(dto);

        });

        Map<String, Object> data = PagingAndSortingHelper.filteredAndSortedResult(
                bonDeLivraisonEntity.getNumber(),
                bonDeLivraisonEntity.getTotalElements(),
                bonDeLivraisonEntity.getTotalPages(),
                dtos);

        return data;
    }

    public BonDeLivraisonDTO getById(Long id) {

        BonDeLivraisonEntity entity = null;
        try {
            entity = bonDeLivraisonRepository.getReferenceById(id);

            BonDeLivraisonDTO dto = new BonDeLivraisonDTO();

            StaticValue staticValStatut = new StaticValue();
            StaticListOfValues listOfValuesStatut = new StaticListOfValues();
            staticValStatut.setKey(
                    listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getKey()
                            .trim());
            staticValStatut.setValue(
                    listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getValue());

            dto.setId(entity.getId());

            dto.setCode(entity.getCode());
            dto.setReference(entity.getReference());
            dto.setDenominationClient(entity.getDenominationClient());
            dto.setCommentaire(entity.getCommentaire());
            dto.setTypeStatut(staticValStatut);
            dto.setDateOperation(DateHelper.toText(entity.getDateOperation(), "time"));
            dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
            dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
            if (entity.getClient() != null) {
                dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
            }
            if (entity.getBonDeLivraisonDetail() != null) {
                dto.setBonDeLivraisonDetail(
                        entity.getBonDeLivraisonDetail().stream().map(this::bonDeLivraisonDetailConvertToDto)
                                .collect(Collectors.toList()));
            } else {
                dto.setBonDeLivraisonDetail(null);
            }
            if (entity.getClient().getAssujettiTva() == true) {
                dto.setTauxTva(entity.getTauxTva());
            } else {
                dto.setTauxTva(null);
            }
            if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
                dto.setMontantTotalTTC(
                        new BigDecimal((dto.getTauxTva() * bonDeLivraisonDetailRepository
                                .montantTotalBonDeLivraisonHT(dto.getId())) / 100));
            } else {
                dto.setMontantTotalHT(
                        new BigDecimal(bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId())));
            }
            return dto;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public BonDeLivraisonDTO ajoutBonDeLivraisonService(BonDeLivraisonDTO dto) {

        try {
            BonDeLivraisonEntity entity = new BonDeLivraisonEntity();
            entity = BonDeLivraisonMapper.getInstance()
                    .convertToEntity(dto);

            BonDeLivraisonEntity creation = bonDeLivraisonRepository.save(entity);

            /* Enregistrement automatique des détails du Bon De Livraison */

            List<BonDeLivraisonDetailDTO> services_details = dto.getBonDeLivraisonDetail();

            for (BonDeLivraisonDetailDTO service_detail : services_details) {

                BonDeLivraisonDetailEntity detailEntity = BonDeLivraisonDetailMapper.getInstance()
                        .convertToEntity(service_detail);

                detailEntity.setIdService(entity.getId());

                bonDeLivraisonDetailRepository.save(detailEntity);
            }

            /* Fin Enregistrement automatique des détails du Bon De Livraison */

            dto = creation != null
                    ? getById(dto.getId())
                    : null;
        } catch (Exception ex) {
            dto = null;
            System.out.println("null" + ex.getMessage());
        }

        return dto;
    }

    public BonDeLivraisonDTO modificationBonDeLivraisonService(Long id,
            BonDeLivraisonDTO updated) {
        BonDeLivraisonEntity converted_Entity, updated_Entity = null;
        try {

            BonDeLivraisonDTO getById = getById(id);
            converted_Entity = BonDeLivraisonMapper.getInstance()
                    .convertToEntity(getById.modifyValues(updated));

            updated_Entity = bonDeLivraisonRepository.save(converted_Entity);

            /* modification automatique des détails du Bon De Livraison */

            List<BonDeLivraisonDetailDTO> services_details = updated.getBonDeLivraisonDetail();

            for (BonDeLivraisonDetailDTO service_detail : services_details) {

                BonDeLivraisonDetailEntity rfd = BonDeLivraisonDetailMapper.getInstance()
                        .convertToEntity(service_detail);

                rfd.setIdService(updated_Entity.getId());

                bonDeLivraisonDetailRepository.save(rfd);
            }

            /* Fin modificatio automatique des détails du service */
            updated = getById(updated.getId());

        } catch (Exception e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
            updated = null;
        }

        return updated;
    }

}
