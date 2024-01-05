package backend.rest_api.gestion_facturation.gestionServices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceEntity;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("SELECT count(u) > 0 FROM ServiceEntity u  WHERE u.code=:code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT G FROM ServiceEntity G WHERE G.code=:code and G.id!=:id")
    Optional<ServiceEntity> verificationCode(@Param("id") Long id, @Param("code") String code);


}
