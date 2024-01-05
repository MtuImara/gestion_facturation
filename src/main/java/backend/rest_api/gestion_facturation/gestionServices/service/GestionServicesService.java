package backend.rest_api.gestion_facturation.gestionServices.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDetailDTO;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;
import backend.rest_api.gestion_facturation.gestionServices.repository.ServiceDetailRepository;
import backend.rest_api.gestion_facturation.gestionServices.repository.ServiceRepository;
import backend.rest_api.gestion_facturation.helpers.PagingAndSortingHelper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GestionServicesService {

    private final ServiceRepository serviceRepository;
    private final ServiceDetailRepository serviceDetailRepository;

    public Map<String, Object> getAll(String title, int page, int size, String[] sort) {

        Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

        Page<ServiceEntity> serviceEntity = null;

        if (title == null || title.equals("")) {
            serviceEntity = serviceRepository.findAll(pagingSort);
        } else {
        }

        List<ServiceDTO> dtos = new ArrayList<>();

        for (ServiceEntity entities : serviceEntity) {
            dtos.add(ServiceMapper.getInstance().convertToDto(entities));
        }

        Map<String, Object> data = PagingAndSortingHelper.filteredAndSortedResult(
                serviceEntity.getNumber(),
                serviceEntity.getTotalElements(),
                serviceEntity.getTotalPages(),
                dtos);

        return data;
    }

    public ServiceDTO getById(Long id) {

        ServiceEntity serviceEntity = null;
        try {
            serviceEntity = serviceRepository.getReferenceById(id);
            ServiceDTO serviceDto = ServiceMapper
                    .getInstance()
                    .convertToDto(serviceEntity);
            return serviceDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ServiceDTO ajoutService(ServiceDTO dto) {

                try {
                        ServiceEntity entity = new ServiceEntity();
                        entity = ServiceMapper.getInstance()
                                        .convertToEntity(dto);

                        ServiceEntity creation = serviceRepository.save(entity);

                        /* Enregistrement automatique des détails du service */

                        List<ServiceDetailDTO> services_details = dto.getServiceDetail();

                        for (ServiceDetailDTO service_detail : services_details) {

                                ServiceDetailEntity detailEntity = ServiceDetailMapper.getInstance()
                                                .convertToEntity(service_detail);

                                detailEntity.setIdService(entity.getId());

                                serviceDetailRepository.save(detailEntity);
                        }

                        /* Fin Enregistrement automatique des détails du service */

                        dto = creation != null
                                        ? ServiceMapper.getInstance()
                                                        .convertToDto(creation)
                                        : null;
                } catch (Exception ex) {
                        dto = null;
                        System.out.println("null" + ex.getMessage());
                }

                return dto;
        }

        public ServiceDTO modificationService(Long id,
                        ServiceDTO updated) {
                ServiceEntity converted_Entity, updated_Entity = null;
                try {

                        ServiceDTO getById = getById(id);
                        converted_Entity = ServiceMapper.getInstance()
                                        .convertToEntity(getById.modifyValues(updated));

                        updated_Entity = serviceRepository.save(converted_Entity);

                        /* Enregistrement automatique des détails du service */

                        List<ServiceDetailDTO> services_details = updated.getServiceDetail();

                        for (ServiceDetailDTO service_detail : services_details) {

                                ServiceDetailEntity rfd = ServiceDetailMapper.getInstance()
                                                .convertToEntity(service_detail);

                                rfd.setIdService(updated_Entity.getId());

                                serviceDetailRepository.save(rfd);
                        }

                        /* Fin Enregistrement automatique des détails du service */
                        updated = ServiceMapper.getInstance()
                                        .convertToDto(updated_Entity);

                } catch (Exception e) {
                        System.out.println("Erreur lors de la modification: " + e.getMessage());
                        updated = null;
                }

                return updated;
        }
    
}
