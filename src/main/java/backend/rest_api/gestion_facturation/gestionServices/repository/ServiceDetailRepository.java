package backend.rest_api.gestion_facturation.gestionServices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;

@Repository
public interface ServiceDetailRepository extends JpaRepository<ServiceDetailEntity, Long> {

}
