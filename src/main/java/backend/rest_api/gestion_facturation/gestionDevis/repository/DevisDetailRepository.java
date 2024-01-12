package backend.rest_api.gestion_facturation.gestionDevis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;

public interface DevisDetailRepository extends JpaRepository<DevisDetailEntity, Long> {

    @Query(value = "SELECT COALESCE(SUM(a.quantite * a.prix_unit_ht), 0) FROM tbl_detail_devis a WHERE a.id_devis=?1 ", nativeQuery = true)
    Double montantTotalDevisHT(@Param("id_devis") Long id_devis);

}
