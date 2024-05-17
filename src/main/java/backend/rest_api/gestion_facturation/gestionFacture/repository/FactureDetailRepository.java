package backend.rest_api.gestion_facturation.gestionFacture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureDetailEntity;

@Repository
public interface FactureDetailRepository extends JpaRepository<FactureDetailEntity, Long> {

    @Query(value = "SELECT COALESCE(SUM(a.prix_total), 0) FROM tbl_facture_detail a WHERE a.id_facture=?1", nativeQuery = true)
    Double montantTotalFactureHT(@Param("id_facture") Long id_facture);

    @Query("SELECT U FROM FactureDetailEntity U WHERE U.idFacture=:idFacture")
    List<FactureDetailEntity> getByFacture(@Param("idFacture") Long idFacture);

}
