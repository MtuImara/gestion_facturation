package backend.rest_api.gestion_facturation.gestionBonDeCommande.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeEntity;

public interface BonDeCommandeRepository
        extends JpaRepository<BonDeCommandeEntity, Long>, JpaSpecificationExecutor<BonDeCommandeEntity> {
    @Query("SELECT count(u) > 0 FROM BonDeCommandeEntity u  WHERE u.code=:code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT G FROM BonDeCommandeEntity G WHERE G.code=:code and G.id!=:id")
    Optional<BonDeCommandeEntity> verificationCode(@Param("id") Long id, @Param("code") String code);

}
