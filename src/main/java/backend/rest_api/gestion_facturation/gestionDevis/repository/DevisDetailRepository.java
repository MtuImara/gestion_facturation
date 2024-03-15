package backend.rest_api.gestion_facturation.gestionDevis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;

@Repository
public interface DevisDetailRepository extends JpaRepository<DevisDetailEntity, Long> {

    // @Query("SELECT count(u) > 0 FROM DevisDetailEntity u  WHERE u.code=:code")
    // boolean existsByCode(@Param("code") String code);

    // @Query("SELECT G FROM DevisDetailEntity G WHERE G.code=:code and G.id!=:id")
    // Optional<DevisDetailEntity> verificationCode(@Param("id") Long id, @Param("code") String code);


    @Query(value = "SELECT COALESCE(SUM(a.quantite * a.prix_unit_ht), 0) FROM gestion_facturation.tbl_detail_devis a WHERE a.id_devis=?1", nativeQuery = true)
    Double montantTotalDevisHT(@Param("id_devis") Long id_devis);

}
