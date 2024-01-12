package backend.rest_api.gestion_facturation.gestionDevis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisEntity;

public interface DevisRepository extends JpaRepository<DevisEntity, Long>, JpaSpecificationExecutor<DevisEntity> {
    @Query("SELECT count(u) > 0 FROM DevisEntity u  WHERE u.code=:code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT G FROM DevisEntity G WHERE G.code=:code and G.id!=:id")
    Optional<DevisEntity> verificationCode(@Param("id") Long id, @Param("code") String code);

}
