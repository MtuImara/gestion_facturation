package backend.rest_api.gestion_facturation.gestionFacture.service;

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
import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDTO;
import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDetailDTO;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureDetailEntity;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureEntity;
import backend.rest_api.gestion_facturation.gestionFacture.mapper.FactureDetailMapper;
import backend.rest_api.gestion_facturation.gestionFacture.mapper.FactureMapper;
import backend.rest_api.gestion_facturation.gestionFacture.repository.FactureDetailRepository;
import backend.rest_api.gestion_facturation.gestionFacture.repository.FactureRepository;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.helpers.DateHelper;
import backend.rest_api.gestion_facturation.helpers.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;
    private final FactureDetailRepository factureDetailRepository;

    public List<StaticValue> typeStatut() {

        StaticListOfValues StaticListOfValues = new StaticListOfValues();
        List<StaticValue> list_of_values = StaticListOfValues.getTypeStatut();

        return list_of_values;
    }

    public FactureDetailDTO factureDetailConvertToDto(FactureDetailEntity entity) {

        FactureDetailDTO dto = new FactureDetailDTO();

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

    public Map<String, Object> getAllFactureService(String title, int page, int size, String[] sort,
            Specification<FactureEntity> spec) {

        Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

        Page<FactureEntity> factureEntity = null;

        if (title == null || title.equals("")) {
            factureEntity = factureRepository.findAll(spec, pagingSort);
        } else {
        }

        List<FactureDTO> dtos = new ArrayList<>();

        factureEntity.forEach(entity -> {

            FactureDTO dto = new FactureDTO();

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
            dto.setDateEcheance(DateHelper.toText(entity.getDateEcheance(), "time"));
            dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
            dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
            // if (entity.getClient() != null) {
            // dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
            // }
            if (entity.getFactureDetail() != null) {
                dto.setFactureDetail(entity.getFactureDetail().stream().map(this::factureDetailConvertToDto)
                        .collect(Collectors.toList()));
            } else {
                dto.setFactureDetail(null);
            }
            if (entity.getClient().getAssujettiTva() == true) {
                dto.setTauxTva(entity.getTauxTva());
            } else {
                dto.setTauxTva(null);
            }
            if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
                dto.setMontantTotalTTC(
                        new BigDecimal((dto.getTauxTva() * factureDetailRepository
                                .montantTotalFactureHT(dto.getId())) / 100));
            } else {
                dto.setMontantTotalHT(
                        new BigDecimal(factureDetailRepository.montantTotalFactureHT(dto.getId())));
            }

            dtos.add(dto);

        });

        Map<String, Object> data = PagingAndSortingHelper.filteredAndSortedResult(
                factureEntity.getNumber(),
                factureEntity.getTotalElements(),
                factureEntity.getTotalPages(),
                dtos);

        return data;
    }

    public FactureDTO getById(Long id) {

        FactureEntity entity = null;
        try {
            entity = factureRepository.getReferenceById(id);

            FactureDTO dto = new FactureDTO();

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
            dto.setDateEcheance(DateHelper.toText(entity.getDateEcheance(), "time"));
            dto.setDateCreation(DateHelper.toText(entity.getDateCreation(), "time"));
            dto.setDateModification(DateHelper.toText(entity.getDateModification(), "time"));
            if (entity.getClient() != null) {
                dto.setClient(ClientMapper.getInstance().convertToDto(entity.getClient()));
            }
            if (entity.getFactureDetail() != null) {
                dto.setFactureDetail(entity.getFactureDetail().stream().map(this::factureDetailConvertToDto)
                        .collect(Collectors.toList()));
            } else {
                dto.setFactureDetail(null);
            }
            if (entity.getClient().getAssujettiTva() == true) {
                dto.setTauxTva(entity.getTauxTva());
            } else {
                dto.setTauxTva(null);
            }
            if (entity.getClient() != null && entity.getClient().getAssujettiTva() == true) {
                dto.setMontantTotalTTC(
                        new BigDecimal((dto.getTauxTva() * factureDetailRepository
                                .montantTotalFactureHT(dto.getId())) / 100));
            } else {
                dto.setMontantTotalHT(
                        new BigDecimal(factureDetailRepository.montantTotalFactureHT(dto.getId())));
            }
            return dto;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public FactureDTO ajoutFactureService(FactureDTO dto) {

        try {
            FactureEntity entity = new FactureEntity();
            entity = FactureMapper.getInstance()
                    .convertToEntity(dto);

            FactureEntity creation = factureRepository.save(entity);

            /* Enregistrement automatique des détails du Facture */

            List<FactureDetailDTO> services_details = dto.getFactureDetail();

            for (FactureDetailDTO service_detail : services_details) {

                FactureDetailEntity detailEntity = FactureDetailMapper.getInstance()
                        .convertToEntity(service_detail);

                detailEntity.setIdService(entity.getId());

                factureDetailRepository.save(detailEntity);
            }

            /* Fin Enregistrement automatique des détails du Facture */

            dto = creation != null
                    ? getById(dto.getId())
                    : null;
        } catch (Exception ex) {
            dto = null;
            System.out.println("null" + ex.getMessage());
        }

        return dto;
    }

    public FactureDTO modificationFactureService(Long id,
            FactureDTO updated) {
        FactureEntity converted_Entity, updated_Entity = null;
        try {

            FactureDTO getById = getById(id);
            converted_Entity = FactureMapper.getInstance()
                    .convertToEntity(getById.modifyValues(updated));

            updated_Entity = factureRepository.save(converted_Entity);

            /* modification automatique des détails du Facture */

            List<FactureDetailDTO> services_details = updated.getFactureDetail();

            for (FactureDetailDTO service_detail : services_details) {

                FactureDetailEntity rfd = FactureDetailMapper.getInstance()
                        .convertToEntity(service_detail);

                rfd.setIdService(updated_Entity.getId());

                factureDetailRepository.save(rfd);
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
