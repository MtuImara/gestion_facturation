package backend.rest_api.gestion_facturation.gestionFacture.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureEntity;

@Repository
public interface FactureRepository extends JpaRepository<FactureEntity, Long>, JpaSpecificationExecutor<FactureEntity> {
    @Query("SELECT count(u) > 0 FROM FactureEntity u  WHERE u.code=:code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT G FROM FactureEntity G WHERE G.code=:code and G.id!=:id")
    Optional<FactureEntity> verificationCode(@Param("id") Long id, @Param("code") String code);

    @Query(value = "SELECT * FROM tbl_facture WHERE date_format(date_operation,'%Y-%m-%d') =?1", nativeQuery = true)
    List<FactureEntity> getAllByDateOperation(@Param("dateOperation") Date dateOperation);

    @Query(value = "Select * from tbl_facture h where h.date_operation >= ?1 and date_format(h.date_operation,'%Y-%m-%d') <= ?2", nativeQuery = true)
    List<FactureEntity> getAllByEntreDateOperation(@Param("dateOperationDebut") Date dateOperationDebut,
            @Param("dateOperationFin") Date dateOperationFin);

}
