package backend.rest_api.gestion_facturation.gestionServices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;

@Repository
public interface ServiceDetailRepository extends JpaRepository<ServiceDetailEntity, Long> {
    @Query("SELECT count(u) > 0 FROM ServiceDetailEntity u  WHERE u.code=:code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT G FROM ServiceDetailEntity G WHERE G.code=:code and G.id!=:id")
    Optional<ServiceDetailEntity> verificationCode(@Param("id") Long id, @Param("code") String code);

}
