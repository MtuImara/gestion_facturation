package backend.rest_api.gestion_facturation.gestionBonDeCommande.repository;

import java.util.Date;
import java.util.List;
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

    @Query(value = "SELECT * FROM tbl_bon_de_commande WHERE date_format(date_operation,'%Y-%m-%d') =?1", nativeQuery = true)
    List<BonDeCommandeEntity> getAllByDateOperation(@Param("dateOperation") Date dateOperation);

    @Query(value = "Select * from tbl_bon_de_commande h where h.date_operation >= ?1 and date_format(h.date_operation,'%Y-%m-%d') <= ?2", nativeQuery = true)
    List<BonDeCommandeEntity> getAllByEntreDateOperation(@Param("dateOperationDebut") Date dateOperationDebut,
            @Param("dateOperationFin") Date dateOperationFin);

}
