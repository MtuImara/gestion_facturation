package backend.rest_api.gestion_facturation.gestionDevis.service;

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
import backend.rest_api.gestion_facturation.gestionClient.mapper.ClientMapper;
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDTO;
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDetailDTO;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisEntity;
import backend.rest_api.gestion_facturation.gestionDevis.mapper.DevisDetailMapper;
import backend.rest_api.gestion_facturation.gestionDevis.mapper.DevisMapper;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisDetailRepository;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisRepository;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;
import backend.rest_api.gestion_facturation.helpers.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DevisService {

        private final DevisRepository devisRepository;
        private final DevisDetailRepository devisDetailRepository;

        public List<StaticValue> typeStatut() {

                StaticListOfValues StaticListOfValues = new StaticListOfValues();
                List<StaticValue> list_of_values = StaticListOfValues.getTypeStatut();

                return list_of_values;
        }

        public DevisDetailDTO devisDetailConvertToDto(DevisDetailEntity entity) {

                DevisDetailDTO dto = new DevisDetailDTO();

                dto.setId(entity.getId());
                if (entity.getServiceDetail() != null) {
                        dto.setServiceDetail(ServiceDetailMapper.getInstance()
                                        .convertToDto(entity.getServiceDetail()));
                }
                dto.setIdServiceDetail(entity.getIdServiceDetail());
                dto.setIdDevis(entity.getIdDevis());
                dto.setDesignation(entity.getDesignation());
                dto.setQuantite(entity.getQuantite());
                dto.setPrixUnitHt(entity.getPrixUnitHt());
                dto.setTauxTva(entity.getTauxTva());
                dto.setMontantHt(entity.getPrixTotal());

                return dto;

        }

        public Map<String, Object> getAll(String title, int page, int size, String[] sort,
                        Specification<DevisEntity> spec) {

                Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

                Page<DevisEntity> devisEntity = null;

                if (title == null || title.equals("")) {
                        devisEntity = devisRepository.findAll(spec, pagingSort);
                } else {
                }

                List<DevisDTO> dtos = new ArrayList<>();

                devisEntity.forEach(entity -> {

                        DevisDTO dto = new DevisDTO();

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
                        if (entity.getDevisDetail() != null) {
                                dto.setDevisDetail(
                                                entity.getDevisDetail().stream().map(this::devisDetailConvertToDto)
                                                                .collect(Collectors.toList()));
                        } else {
                                dto.setDevisDetail(null);
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
                        if (devisDetailRepository.montantTotalDevisHT(dto.getId()) != null
                                        || devisDetailRepository.montantTotalDevisHT(dto.getId()) == 0) {
                                dto.setMontantTotalHT(
                                                new BigDecimal(devisDetailRepository
                                                                .montantTotalDevisHT(dto.getId())));
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
                                devisEntity.getNumber(),
                                devisEntity.getTotalElements(),
                                devisEntity.getTotalPages(),
                                dtos);

                return data;
        }

        public DevisDTO getById(Long id) {

                DevisEntity entity = null;
                try {
                        entity = devisRepository.getReferenceById(id);

                        DevisDTO dto = new DevisDTO();

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
                        if (entity.getDevisDetail() != null) {
                                dto.setDevisDetail(
                                                entity.getDevisDetail().stream().map(this::devisDetailConvertToDto)
                                                                .collect(Collectors.toList()));
                        } else {
                                dto.setDevisDetail(null);
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
                        if (devisDetailRepository.montantTotalDevisHT(dto.getId()) != null
                                        || devisDetailRepository.montantTotalDevisHT(dto.getId()) == 0) {
                                dto.setMontantTotalHT(
                                                new BigDecimal(devisDetailRepository
                                                                .montantTotalDevisHT(dto.getId())));
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

        public DevisDetailDTO getDevisDetailById(Long id) {

                DevisDetailEntity devisDetailEntity = null;
                try {
                        devisDetailEntity = devisDetailRepository.getReferenceById(id);
                        DevisDetailDTO devisDetailDto = DevisDetailMapper
                                        .getInstance()
                                        .convertToDto(devisDetailEntity);
                        return devisDetailDto;
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return null;
                }
        }

        public DevisDTO ajoutDevisService(DevisDTO dto) {

                try {
                        DevisEntity entity = new DevisEntity();
                        entity = DevisMapper.getInstance()
                                        .convertToEntity(dto);

                        DevisEntity creation = devisRepository.save(entity);

                        /* Enregistrement automatique des détails du Devis */

                        List<DevisDetailDTO> services_details = dto.getDevisDetail();

                        for (DevisDetailDTO service_detail : services_details) {

                                DevisDetailEntity detailEntity = DevisDetailMapper.getInstance()
                                                .convertToEntity(service_detail);

                                detailEntity.setIdServiceDetail(entity.getId());

                                devisDetailRepository.save(detailEntity);
                        }

                        /* Fin Enregistrement automatique des détails du Devis */

                        dto = creation != null
                                        ? getById(dto.getId())
                                        : null;
                } catch (Exception ex) {
                        dto = null;
                        System.out.println("null" + ex.getMessage());
                }

                return dto;
        }

        public DevisDTO modificationDevisService(Long id,
                        DevisDTO updated) {
                DevisEntity converted_Entity, updated_Entity = null;
                try {

                        DevisDTO getById = getById(id);
                        converted_Entity = DevisMapper.getInstance()
                                        .convertToEntity(getById.modifyValues(updated));

                        updated_Entity = devisRepository.save(converted_Entity);

                        /* modification automatique des détails du Devis */

                        List<DevisDetailDTO> services_details = updated.getDevisDetail();

                        for (DevisDetailDTO service_detail : services_details) {

                                DevisDetailEntity rfd = DevisDetailMapper.getInstance()
                                                .convertToEntity(service_detail);

                                rfd.setIdServiceDetail(updated_Entity.getId());

                                devisDetailRepository.save(rfd);
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

        public DevisDTO ajouter(DevisDTO devisDto) {

                try {
                        DevisEntity devisEntity = new DevisEntity();
                        devisEntity = DevisMapper.getInstance()
                                        .convertToEntity(devisDto);
                        devisEntity.setDateCreation(DateHelper.now());
                        DevisEntity creationDevis = devisRepository.save(devisEntity);

                        devisDto = creationDevis != null
                                        ? DevisMapper.getInstance().convertToDto(creationDevis)
                                        : null;
                } catch (Exception ex) {
                        devisDto = null;
                        System.out.println("null" + ex.getMessage());
                }

                return devisDto;
        }

        public DevisDetailDTO ajoutDevisDetailService(DevisDetailDTO devisDetailDto) {

                try {
                        DevisDetailEntity devisDetailEntity = new DevisDetailEntity();
                        devisDetailEntity = DevisDetailMapper.getInstance()
                                        .convertToEntity(devisDetailDto);
                        // serviceDetailEntity.setDateCreation(DateHelper.now());
                        DevisDetailEntity creationDevisDetail = devisDetailRepository.save(devisDetailEntity);

                        devisDetailDto = creationDevisDetail != null
                                        ? DevisDetailMapper.getInstance().convertToDto(creationDevisDetail)
                                        : null;
                } catch (Exception ex) {
                        devisDetailDto = null;
                        System.out.println("null" + ex.getMessage());
                }

                return devisDetailDto;
        }

        public DevisDTO update(Long id, DevisDTO updated) {
                DevisEntity converted_DevisEntity, updated_DevisEntity = null;
                try {

                        DevisDTO serviceDto = getById(id);
                        converted_DevisEntity = DevisMapper.getInstance()
                                        .convertToEntity(serviceDto.modifyValues(updated));
                        // converted_ClientEntity.setDateModification(DateHelper.now());
                        updated_DevisEntity = devisRepository.save(converted_DevisEntity);
                        updated = DevisMapper.getInstance().convertToDto(updated_DevisEntity);

                } catch (Exception e) {
                        System.out.println("Erreur lors du Service: " + e.getMessage());
                        updated = null;
                }

                return updated;
        }

        public DevisDetailDTO updateDevisDetail(Long id, DevisDetailDTO updated) {
                DevisDetailEntity converted_DevisDetailEntity, updated_DevisDetailEntity = null;
                try {

                        DevisDetailDTO devisDetailDto = getDevisDetailById(id);
                        converted_DevisDetailEntity = DevisDetailMapper.getInstance()
                                        .convertToEntity(devisDetailDto.modifyValues(updated));
                        // converted_ClientEntity.setDateModification(DateHelper.now());
                        updated_DevisDetailEntity = devisDetailRepository.save(converted_DevisDetailEntity);
                        updated = DevisDetailMapper.getInstance().convertToDto(updated_DevisDetailEntity);

                } catch (Exception e) {
                        System.out.println("Erreur lors du ServiceDetail: " + e.getMessage());
                        updated = null;
                }

                return updated;
        }

}
