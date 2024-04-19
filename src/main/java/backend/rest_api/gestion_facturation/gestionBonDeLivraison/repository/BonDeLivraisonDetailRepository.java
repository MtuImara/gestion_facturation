package backend.rest_api.gestion_facturation.gestionBonDeLivraison.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionBonDeLivraison.entity.BonDeLivraisonDetailEntity;

@Repository
public interface BonDeLivraisonDetailRepository extends JpaRepository<BonDeLivraisonDetailEntity, Long> {

    // @Query(value = "SELECT COALESCE(SUM(a.quantite * a.prix_unit_ht), 0) FROM
    // gestion_facturation.tbl_bon_de_livraison_detail a WHERE
    // a.id_bon_de_livraison=?1", nativeQuery = true)
    // Double montantTotalBonDeLivraisonHT(@Param("id_bon_de_livraison") Long
    // id_bon_de_livraison);

    @Query(value = "SELECT COALESCE(SUM(a.prix_total), 0) FROM gestion_facturation.tbl_bon_de_livraison_detail a WHERE a.id_bon_de_livraison=?1", nativeQuery = true)
    Double montantTotalBonDeLivraisonHT(@Param("id_bon_de_livraison") Long id_bon_de_livraison);

}
