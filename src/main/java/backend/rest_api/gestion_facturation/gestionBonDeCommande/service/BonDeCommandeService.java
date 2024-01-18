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
                        if (entity.getBonDeCommandeDetail() != null) {
                                dto.setBonDeCommandeDetail(entity.getBonDeCommandeDetail().stream()
                                                .map(this::bonDeCommandeDetailConvertToDto)
                                                .collect(Collectors.toList()));
                        } else {
                                dto.setBonDeCommandeDetail(null);
                        }
                        if (entity.getClient().getAssujettiTva() == true) {
                                dto.setTauxTva(entity.getTauxTva());
                        } else {
                                dto.setTauxTva(null);
                        }
                        if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
                                dto.setMontantTotalTTC(
                                                new BigDecimal((dto.getTauxTva() * bonDeCommandeDetailRepository
                                                                .montantTotalBonDeCommandeHT(dto.getId())) / 100));
                        } else {
                                dto.setMontantTotalHT(
                                                new BigDecimal(bonDeCommandeDetailRepository
                                                                .montantTotalBonDeCommandeHT(dto.getId())));
                        }

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
                        if (entity.getBonDeCommandeDetail() != null) {
                                dto.setBonDeCommandeDetail(entity.getBonDeCommandeDetail().stream()
                                                .map(this::bonDeCommandeDetailConvertToDto)
                                                .collect(Collectors.toList()));
                        } else {
                                dto.setBonDeCommandeDetail(null);
                        }
                        if (entity.getClient().getAssujettiTva() == true) {
                                dto.setTauxTva(entity.getTauxTva());
                        } else {
                                dto.setTauxTva(null);
                        }
                        if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
                                dto.setMontantTotalTTC(
                                                new BigDecimal((dto.getTauxTva() * bonDeCommandeDetailRepository
                                                                .montantTotalBonDeCommandeHT(dto.getId())) / 100));
                        } else {
                                dto.setMontantTotalHT(
                                                new BigDecimal(bonDeCommandeDetailRepository
                                                                .montantTotalBonDeCommandeHT(dto.getId())));
                        }
                        return dto;

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

                                detailEntity.setIdService(entity.getId());

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

                                rfd.setIdService(updated_Entity.getId());

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

}
