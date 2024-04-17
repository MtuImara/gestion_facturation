package backend.rest_api.gestion_facturation.gestionBonDeCommande.service;

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
import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeDetailEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeEntity;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.mapper.BonDeCommandeDetailMapper;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.mapper.BonDeCommandeMapper;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.repository.BonDeCommandeDetailRepository;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.repository.BonDeCommandeRepository;
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;
import backend.rest_api.gestion_facturation.helpers.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BonDeCommandeService {

        private final BonDeCommandeRepository bonDeCommandeRepository;
        private final BonDeCommandeDetailRepository bonDeCommandeDetailRepository;

        public List<StaticValue> typeStatut() {

                StaticListOfValues StaticListOfValues = new StaticListOfValues();
                List<StaticValue> list_of_values = StaticListOfValues.getTypeStatut();

                return list_of_values;
        }

        public BonDeCommandeDetailDTO bonDeCommandeDetailConvertToDto(BonDeCommandeDetailEntity entity) {

                BonDeCommandeDetailDTO dto = new BonDeCommandeDetailDTO();

                dto.setId(entity.getId());
                if (entity.getServiceDetail() != null) {
                        dto.setServiceDetail(ServiceDetailMapper.getInstance()
                                        .convertToDto(entity.getServiceDetail()));
                }
                dto.setIdServiceDetail(entity.getIdServiceDetail());
                dto.setIdBonDeCommande(entity.getIdBonDeCommande());
                dto.setDesignation(entity.getDesignation());
                dto.setQuantite(entity.getQuantite());
                dto.setPrixUnitHt(entity.getPrixUnitHt());
                dto.setTauxTva(entity.getTauxTva());
                dto.setMontantHt(entity.getPrixTotal());

                return dto;

        }

        public Map<String, Object> getBonDeCommandeAll(String title, int page, int size, String[] sort,
                        Specification<BonDeCommandeEntity> spec) {

                Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

                Page<BonDeCommandeEntity> bonDeCommandeEntity = null;

                if (title == null || title.equals("")) {
                        bonDeCommandeEntity = bonDeCommandeRepository.findAll(spec, pagingSort);
                } else {
                }

                List<BonDeCommandeDTO> dtos = new ArrayList<>();

                bonDeCommandeEntity.forEach(entity -> {

                        BonDeCommandeDTO dto = new BonDeCommandeDTO();

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
                        dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
                        dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
                        if (entity.getClient() != null) {
                                dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
                        }
                        dto.setIdClient(entity.getIdClient());
                        if (entity.getBonDeCommandeDetail() != null) {
                                dto.setBonDeCommandeDetail(
                                                entity.getBonDeCommandeDetail().stream()
                                                                .map(this::bonDeCommandeDetailConvertToDto)
                                                                .collect(Collectors.toList()));
                        } else {
                                dto.setBonDeCommandeDetail(null);
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
                        if (bonDeCommandeDetailRepository.montantTotalBonDeCommandeHT(dto.getId()) != null
                                        || bonDeCommandeDetailRepository
                                                        .montantTotalBonDeCommandeHT(dto.getId()) == 0) {
                                dto.setMontantTotalHT(
                                                new BigDecimal(bonDeCommandeDetailRepository
                                                                .montantTotalBonDeCommandeHT(dto.getId())));
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
                                bonDeCommandeEntity.getNumber(),
                                bonDeCommandeEntity.getTotalElements(),
                                bonDeCommandeEntity.getTotalPages(),
                                dtos);

                return data;
        }

        public BonDeCommandeDTO getById(Long id) {

                BonDeCommandeEntity entity = null;
                try {
                        entity = bonDeCommandeRepository.getReferenceById(id);

                        BonDeCommandeDTO dto = new BonDeCommandeDTO();

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
                        dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
                        dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
                        if (entity.getClient() != null) {
                                dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
                        }
                        dto.setIdClient(entity.getIdClient());
                        if (entity.getBonDeCommandeDetail() != null) {
                                dto.setBonDeCommandeDetail(
                                                entity.getBonDeCommandeDetail().stream()
                                                                .map(this::bonDeCommandeDetailConvertToDto)
                                                                .collect(Collectors.toList()));
                        } else {
                                dto.setBonDeCommandeDetail(null);
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
                        if (bonDeCommandeDetailRepository.montantTotalBonDeCommandeHT(dto.getId()) != null
                                        || bonDeCommandeDetailRepository
                                                        .montantTotalBonDeCommandeHT(dto.getId()) == 0) {
                                dto.setMontantTotalHT(
                                                new BigDecimal(bonDeCommandeDetailRepository
                                                                .montantTotalBonDeCommandeHT(dto.getId())));
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

        public BonDeCommandeDetailDTO getBonDeCommandeDetailById(Long id) {

                BonDeCommandeDetailEntity bonDeCommandeDetailEntity = null;
                try {
                        bonDeCommandeDetailEntity = bonDeCommandeDetailRepository.getReferenceById(id);
                        BonDeCommandeDetailDTO bonDeCommandeDetailDto = BonDeCommandeDetailMapper
                                        .getInstance()
                                        .convertToDto(bonDeCommandeDetailEntity);
                        return bonDeCommandeDetailDto;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return null;
                }
        }

        public BonDeCommandeDTO ajoutBonDeCommandeService(BonDeCommandeDTO dto) {

                try {
                        BonDeCommandeEntity entity = new BonDeCommandeEntity();
                        entity = BonDeCommandeMapper.getInstance()
                                        .convertToEntity(dto);

                        BonDeCommandeEntity creation = bonDeCommandeRepository.save(entity);

                        /* Enregistrement automatique des détails du Bon De Commande */

                        List<BonDeCommandeDetailDTO> services_details = dto.getBonDeCommandeDetail();

                        for (BonDeCommandeDetailDTO service_detail : services_details) {

                                BonDeCommandeDetailEntity detailEntity = BonDeCommandeDetailMapper.getInstance()
                                                .convertToEntity(service_detail);

                                detailEntity.setIdBonDeCommande(entity.getId());

                                bonDeCommandeDetailRepository.save(detailEntity);
                        }

                        /* Fin Enregistrement automatique des détails du Bon De Commande */

                        dto = creation != null
                                        ? getById(dto.getId())
                                        : null;
                } catch (Exception ex) {
                        dto = null;
                        System.out.println("null" + ex.getMessage());
                }

                return dto;
        }

        public BonDeCommandeDTO modificationBonDeCommandeService(Long id,
                        BonDeCommandeDTO updated) {
                BonDeCommandeEntity converted_Entity, updated_Entity = null;
                try {

                        BonDeCommandeDTO getById = getById(id);
                        converted_Entity = BonDeCommandeMapper.getInstance()
                                        .convertToEntity(getById.modifyValues(updated));

                        updated_Entity = bonDeCommandeRepository.save(converted_Entity);

                        /* modification automatique des détails du Bon De Commande */

                        List<BonDeCommandeDetailDTO> services_details = updated.getBonDeCommandeDetail();

                        for (BonDeCommandeDetailDTO service_detail : services_details) {

                                BonDeCommandeDetailEntity rfd = BonDeCommandeDetailMapper.getInstance()
                                                .convertToEntity(service_detail);

                                rfd.setIdBonDeCommande(updated_Entity.getId());

                                bonDeCommandeDetailRepository.save(rfd);
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

        public BonDeCommandeDTO ajouter(BonDeCommandeDTO bonDeCommandeDto) {

                try {
                        BonDeCommandeEntity bonDeCommandeEntity = new BonDeCommandeEntity();
                        bonDeCommandeEntity = BonDeCommandeMapper.getInstance()
                                        .convertToEntity(bonDeCommandeDto);
                        bonDeCommandeEntity.setDateCreation(DateHelper.now());
                        BonDeCommandeEntity creationBonDeCommande = bonDeCommandeRepository.save(bonDeCommandeEntity);

                        bonDeCommandeDto = creationBonDeCommande != null
                                        ? BonDeCommandeMapper.getInstance().convertToDto(creationBonDeCommande)
                                        : null;
                } catch (Exception ex) {
                        bonDeCommandeDto = null;
                        System.out.println("null" + ex.getMessage());
                }

                return bonDeCommandeDto;
        }

        public BonDeCommandeDetailDTO ajoutBonDeCommandeDetailService(BonDeCommandeDetailDTO bonDeCommandeDetailDto) {

                try {
                        BonDeCommandeDetailEntity bonDeCommandeDetailEntity = new BonDeCommandeDetailEntity();
                        bonDeCommandeDetailEntity = BonDeCommandeDetailMapper.getInstance()
                                        .convertToEntity(bonDeCommandeDetailDto);
                        // serviceDetailEntity.setDateCreation(DateHelper.now());
                        BonDeCommandeDetailEntity creationBonDeCommandeDetail = bonDeCommandeDetailRepository
                                        .save(bonDeCommandeDetailEntity);

                        bonDeCommandeDetailDto = creationBonDeCommandeDetail != null
                                        ? BonDeCommandeDetailMapper.getInstance()
                                                        .convertToDto(creationBonDeCommandeDetail)
                                        : null;
                } catch (Exception ex) {
                        bonDeCommandeDetailDto = null;
                        System.out.println("null" + ex.getMessage());
                }

                return bonDeCommandeDetailDto;
        }

        public BonDeCommandeDTO update(Long id, BonDeCommandeDTO updated) {
                BonDeCommandeEntity converted_BonDeCommandeEntity, updated_BonDeCommandeEntity = null;
                try {

                        BonDeCommandeDTO bonDeCommandeDto = getById(id);
                        converted_BonDeCommandeEntity = BonDeCommandeMapper.getInstance()
                                        .convertToEntity(bonDeCommandeDto.modifyValues(updated));
                        // converted_FactureEntity.setDateModification(DateHelper.now());
                        updated_BonDeCommandeEntity = bonDeCommandeRepository.save(converted_BonDeCommandeEntity);
                        updated = BonDeCommandeMapper.getInstance().convertToDto(updated_BonDeCommandeEntity);

                } catch (Exception e) {
                        System.out.println("Erreur lors du BonDeCommande: " + e.getMessage());
                        updated = null;
                }

                return updated;
        }

        public BonDeCommandeDetailDTO updateBonDeCommandeDetail(Long id, BonDeCommandeDetailDTO updated) {
                BonDeCommandeDetailEntity converted_BonDeCommandeDetailEntity, updated_BonDeCommandeDetailEntity = null;
                try {

                        BonDeCommandeDetailDTO bonDeCommandeDetailDto = getBonDeCommandeDetailById(id);
                        converted_BonDeCommandeDetailEntity = BonDeCommandeDetailMapper.getInstance()
                                        .convertToEntity(bonDeCommandeDetailDto.modifyValues(updated));
                        // converted_ClientEntity.setDateModification(DateHelper.now());
                        updated_BonDeCommandeDetailEntity = bonDeCommandeDetailRepository
                                        .save(converted_BonDeCommandeDetailEntity);
                        updated = BonDeCommandeDetailMapper.getInstance()
                                        .convertToDto(updated_BonDeCommandeDetailEntity);

                } catch (Exception e) {
                        System.out.println("Erreur lors du BonDeCommandeDetail: " + e.getMessage());
                        updated = null;
                }

                return updated;
        }

}
