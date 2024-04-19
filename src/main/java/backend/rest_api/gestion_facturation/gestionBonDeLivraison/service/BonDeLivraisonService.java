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
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
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
        if (entity.getServiceDetail() != null) {
            dto.setServiceDetail(ServiceDetailMapper.getInstance()
                    .convertToDto(entity.getServiceDetail()));
        }
        dto.setIdServiceDetail(entity.getIdServiceDetail());
        dto.setIdBonDeLivraison(entity.getIdBonDeLivraison());
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixUnitHt(entity.getPrixUnitHt());
        dto.setTauxTva(entity.getTauxTva());
        dto.setMontantHt(entity.getPrixTotal());

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

            // StaticValue staticValStatut = new StaticValue();
            // StaticListOfValues listOfValuesStatut = new StaticListOfValues();
            // staticValStatut.setKey(
            // listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getKey()
            // .trim());
            // staticValStatut.setValue(
            // listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() -
            // 1).getValue());

            dto.setId(entity.getId());

            dto.setCode(entity.getCode());
            dto.setReference(entity.getReference());
            dto.setDenominationClient(entity.getDenominationClient());
            dto.setCommentaire(entity.getCommentaire());
            dto.setEtat(entity.getEtat());
            // if (staticValStatut != null) {
            // dto.setTypeStatut(staticValStatut);
            // }
            dto.setDateOperation(DateHelper.toText(entity.getDateOperation(), "time"));
            dto.setDateEcheance(DateHelper.toText(entity.getDateEcheance(), "time"));
            dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
            dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
            if (entity.getClient() != null) {
                dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
            }
            dto.setIdClient(entity.getIdClient());
            if (entity.getBonDeLivraisonDetail() != null) {
                dto.setBonDeLivraisonDetail(
                        entity.getBonDeLivraisonDetail().stream().map(this::bonDeLivraisonDetailConvertToDto)
                                .collect(Collectors.toList()));
            } else {
                dto.setBonDeLivraisonDetail(null);
            }
            if (entity.getService() != null) {
                dto.setService(ServiceMapper.getInstance().convertToDto(entity.getService()));
            }
            dto.setId_service(entity.getIdService());
            if (entity.getClient().getAssujettiTva() == true) {
                dto.setTauxTva(entity.getTauxTva());
            } else {
                dto.setTauxTva(0.0);
            }
            if (bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId()) != null
                    || bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId()) == 0) {
                dto.setMontantTotalHT(
                        new BigDecimal(bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId())));
            } else {
                dto.setMontantTotalHT(new BigDecimal(0.0));
            }

            // if (entity.getClient() != null && entity.getClient().getAssujettiTva() ==
            // true) {
            // dto.setMontantTotalHT(new BigDecimal(0.0));
            // dto.setMontantTva(new BigDecimal(dto.getTauxTva() * factureDetailRepository
            // .montantTotalFactureHT(dto.getId())
            // / 100));
            // dto.setMontantTotalTTC(
            // new BigDecimal(factureDetailRepository
            // .montantTotalFactureHT(dto.getId())
            // + ((dto.getTauxTva() * factureDetailRepository
            // .montantTotalFactureHT(dto.getId()))
            // / 100)));
            // } else {
            // dto.setMontantTotalHT(
            // new BigDecimal(factureDetailRepository.montantTotalFactureHT(dto.getId())));
            // dto.setMontantTva(new BigDecimal(0.00));
            // dto.setMontantTotalTTC(new BigDecimal(0.0));
            // }

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

            // StaticValue staticValStatut = new StaticValue();
            // StaticListOfValues listOfValuesStatut = new StaticListOfValues();
            // staticValStatut.setKey(
            // listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() - 1).getKey()
            // .trim());
            // staticValStatut.setValue(
            // listOfValuesStatut.getTypeStatut().get(entity.getTypeStatut() -
            // 1).getValue());

            dto.setId(entity.getId());

            dto.setCode(entity.getCode());
            dto.setReference(entity.getReference());
            dto.setDenominationClient(entity.getDenominationClient());
            dto.setCommentaire(entity.getCommentaire());
            dto.setEtat(entity.getEtat());
            // if (staticValStatut != null) {
            // dto.setTypeStatut(staticValStatut);
            // }
            dto.setDateOperation(DateHelper.toText(entity.getDateOperation(), "time"));
            dto.setDateEcheance(DateHelper.toText(entity.getDateEcheance(), "time"));
            dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
            dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
            if (entity.getClient() != null) {
                dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
            }
            dto.setIdClient(entity.getIdClient());
            if (entity.getBonDeLivraisonDetail() != null) {
                dto.setBonDeLivraisonDetail(
                        entity.getBonDeLivraisonDetail().stream().map(this::bonDeLivraisonDetailConvertToDto)
                                .collect(Collectors.toList()));
            } else {
                dto.setBonDeLivraisonDetail(null);
            }
            if (entity.getService() != null) {
                dto.setService(ServiceMapper.getInstance().convertToDto(entity.getService()));
            }
            dto.setId_service(entity.getIdService());
            if (entity.getClient().getAssujettiTva() == true) {
                dto.setTauxTva(entity.getTauxTva());
            } else {
                dto.setTauxTva(0.0);
            }
            if (bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId()) != null
                    || bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId()) == 0) {
                dto.setMontantTotalHT(
                        new BigDecimal(bonDeLivraisonDetailRepository.montantTotalBonDeLivraisonHT(dto.getId())));
            } else {
                dto.setMontantTotalHT(new BigDecimal(0.0));
            }

            // if (entity.getClient() != null && entity.getClient().getAssujettiTva() ==
            // true) {
            // dto.setMontantTotalHT(new BigDecimal(0.0));
            // dto.setMontantTva(new BigDecimal(dto.getTauxTva() * factureDetailRepository
            // .montantTotalFactureHT(dto.getId())
            // / 100));
            // dto.setMontantTotalTTC(
            // new BigDecimal(factureDetailRepository
            // .montantTotalFactureHT(dto.getId())
            // + ((dto.getTauxTva() * factureDetailRepository
            // .montantTotalFactureHT(dto.getId()))
            // / 100)));
            // } else {
            // dto.setMontantTotalHT(
            // new BigDecimal(factureDetailRepository.montantTotalFactureHT(dto.getId())));
            // dto.setMontantTva(new BigDecimal(0.00));
            // dto.setMontantTotalTTC(new BigDecimal(0.0));
            // }
            return dto;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public BonDeLivraisonDetailDTO getBonDeLivraisonDetailById(Long id) {

        BonDeLivraisonDetailEntity bonDeLivraisonDetailEntity = null;
        try {
            bonDeLivraisonDetailEntity = bonDeLivraisonDetailRepository.getReferenceById(id);
            BonDeLivraisonDetailDTO bonDeLivraisonDetailDto = BonDeLivraisonDetailMapper
                    .getInstance().convertToDto(bonDeLivraisonDetailEntity);
            return bonDeLivraisonDetailDto;
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

                detailEntity.setIdBonDeLivraison(entity.getId());

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

                rfd.setIdBonDeLivraison(updated_Entity.getId());

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

    // Ajout et Modification des données séparées

    public BonDeLivraisonDTO ajouter(BonDeLivraisonDTO bonDeLivraisonDto) {

        try {
            BonDeLivraisonEntity bonDeLivraisonEntity = new BonDeLivraisonEntity();
            bonDeLivraisonEntity = BonDeLivraisonMapper.getInstance()
                    .convertToEntity(bonDeLivraisonDto);
            bonDeLivraisonEntity.setDateCreation(DateHelper.now());
            BonDeLivraisonEntity creationBonDeLivraison = bonDeLivraisonRepository.save(bonDeLivraisonEntity);

            bonDeLivraisonDto = creationBonDeLivraison != null
                    ? BonDeLivraisonMapper.getInstance().convertToDto(creationBonDeLivraison)
                    : null;
        } catch (Exception ex) {
            bonDeLivraisonDto = null;
            System.out.println("null" + ex.getMessage());
        }

        return bonDeLivraisonDto;
    }

    public BonDeLivraisonDetailDTO ajoutBonDeLivraisonDetailService(BonDeLivraisonDetailDTO bonDeLivraisonDetailDto) {

        try {
            BonDeLivraisonDetailEntity bonDeLivraisonDetailEntity = new BonDeLivraisonDetailEntity();
            bonDeLivraisonDetailEntity = BonDeLivraisonDetailMapper.getInstance()
                    .convertToEntity(bonDeLivraisonDetailDto);
            // serviceDetailEntity.setDateCreation(DateHelper.now());
            BonDeLivraisonDetailEntity creationBonDeLivraisonDetail = bonDeLivraisonDetailRepository
                    .save(bonDeLivraisonDetailEntity);

            bonDeLivraisonDetailDto = creationBonDeLivraisonDetail != null
                    ? BonDeLivraisonDetailMapper.getInstance().convertToDto(creationBonDeLivraisonDetail)
                    : null;
        } catch (Exception ex) {
            bonDeLivraisonDetailDto = null;
            System.out.println("null" + ex.getMessage());
        }

        return bonDeLivraisonDetailDto;
    }

    public BonDeLivraisonDTO update(Long id, BonDeLivraisonDTO updated) {
        BonDeLivraisonEntity converted_BonDeLivraisonEntity, updated_BonDeLivraisonEntity = null;
        try {

            BonDeLivraisonDTO bonDeLivraisonDto = getById(id);
            converted_BonDeLivraisonEntity = BonDeLivraisonMapper.getInstance()
                    .convertToEntity(bonDeLivraisonDto.modifyValues(updated));
            // converted_FactureEntity.setDateModification(DateHelper.now());
            updated_BonDeLivraisonEntity = bonDeLivraisonRepository.save(converted_BonDeLivraisonEntity);
            updated = BonDeLivraisonMapper.getInstance().convertToDto(updated_BonDeLivraisonEntity);

        } catch (Exception e) {
            System.out.println("Erreur lors du BonDeLivraison: " + e.getMessage());
            updated = null;
        }

        return updated;
    }

    public BonDeLivraisonDetailDTO updateBonDeLivraisonDetail(Long id, BonDeLivraisonDetailDTO updated) {
        BonDeLivraisonDetailEntity converted_BonDeLivraisonDetailEntity, updated_BonDeLivraisonDetailEntity = null;
        try {

            BonDeLivraisonDetailDTO bonDeLivraisonDetailDto = getBonDeLivraisonDetailById(id);
            converted_BonDeLivraisonDetailEntity = BonDeLivraisonDetailMapper.getInstance()
                    .convertToEntity(bonDeLivraisonDetailDto.modifyValues(updated));
            // converted_ClientEntity.setDateModification(DateHelper.now());
            updated_BonDeLivraisonDetailEntity = bonDeLivraisonDetailRepository
                    .save(converted_BonDeLivraisonDetailEntity);
            updated = BonDeLivraisonDetailMapper.getInstance().convertToDto(updated_BonDeLivraisonDetailEntity);

        } catch (Exception e) {
            System.out.println("Erreur lors du ServiceDetail: " + e.getMessage());
            updated = null;
        }

        return updated;
    }

}
