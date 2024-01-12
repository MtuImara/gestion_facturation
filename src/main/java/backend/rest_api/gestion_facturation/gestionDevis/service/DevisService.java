package backend.rest_api.gestion_facturation.gestionDevis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.constantes.StaticListOfValues;
import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDTO;
import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDetailDTO;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisEntity;
import backend.rest_api.gestion_facturation.gestionDevis.mapper.DevisDetailMapper;
import backend.rest_api.gestion_facturation.gestionDevis.mapper.DevisMapper;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisDetailRepository;
import backend.rest_api.gestion_facturation.gestionDevis.repository.DevisRepository;
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

    public Map<String, Object> getAll(String title, int page, int size, String[] sort,
            Specification<DevisEntity> spec) {

        Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

        Page<DevisEntity> devisEntity = null;

        if (title == null || title.equals("")) {
            devisEntity = devisRepository.findAll(spec, pagingSort);
        } else {
        }

        List<DevisDTO> dtos = new ArrayList<>();

        for (DevisEntity entities : devisEntity) {
            dtos.add(DevisMapper.getInstance().convertToDto(entities));
        }

        Map<String, Object> data = PagingAndSortingHelper.filteredAndSortedResult(
                devisEntity.getNumber(),
                devisEntity.getTotalElements(),
                devisEntity.getTotalPages(),
                dtos);

        return data;
    }

    public DevisDTO getById(Long id) {

        DevisEntity devisEntity = null;
        try {
           devisEntity = devisRepository.getReferenceById(id);
            DevisDTO devisDto = DevisMapper.getInstance()
                    .convertToDto(devisEntity);
            return devisDto;
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

                                detailEntity.setIdService(entity.getId());

                                devisDetailRepository.save(detailEntity);
                        }

                        /* Fin Enregistrement automatique des détails du Devis */

                        dto = creation != null
                                        ? DevisMapper.getInstance()
                                                        .convertToDto(creation)
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

                                rfd.setIdService(updated_Entity.getId());

                                devisDetailRepository.save(rfd);
                        }

                        /* Fin modificatio automatique des détails du service */
                        updated = DevisMapper.getInstance()
                                        .convertToDto(updated_Entity);

                } catch (Exception e) {
                        System.out.println("Erreur lors de la modification: " + e.getMessage());
                        updated = null;
                }

                return updated;
        }
    
}
