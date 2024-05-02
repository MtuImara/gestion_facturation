package backend.rest_api.gestion_facturation.gestionBonDeCommande.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeDetailEntity;

public interface BonDeCommandeDetailRepository extends JpaRepository<BonDeCommandeDetailEntity, Long> {

    // @Query(value = "SELECT COALESCE(SUM(a.quantite * a.prix_unit_ht), 0) FROM
    // gestion_facturation.tbl_bon_de_commande_detail a WHERE
    // a.id_bon_de_commande=?1", nativeQuery = true)
    // Double montantTotalBonDeCommandeHT(@Param("id_bon_de_commande") Long
    // id_bon_de_commande);

    @Query(value = "SELECT COALESCE(SUM(a.prix_total), 0) FROM gestion_facturation.tbl_bon_de_commande_detail a WHERE a.id_bon_de_commande=?1", nativeQuery = true)
    Double montantTotalBonDeCommandeHT(@Param("id_bon_de_commande") Long id_bon_de_commande);

    @Query("SELECT U FROM BonDeCommandeDetailEntity U WHERE U.idBonDeCommande=:idBonDeCommande")
    List<BonDeCommandeDetailEntity> getByBonDeCommande(@Param("idBonDeCommande") Long idBonDeCommande);

}
