package backend.rest_api.gestion_facturation.gestionTVA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionTVA.entity.TauxTvaEntity;

import java.util.List;
import java.util.Optional;
@Repository
public interface TauxTvaRepository extends JpaRepository<TauxTvaEntity, Long> {

  @Query("SELECT count(t) > 0 FROM TauxTva t  WHERE t.code = :code")
  boolean existsByCode(@Param("code") String code);

  @Query(value = "SELECT * FROM tbl_taux_tvas T WHERE T.code=:code and T.id!=:id",nativeQuery = true)
  Optional<TauxTvaEntity> verificationCode(@Param("id") Long id,
                                     @Param("code") String code);

  @Query(value = "SELECT * FROM tbl_taux_tvas t  WHERE t.code LIKE  %?1%", nativeQuery = true)
  List<TauxTvaEntity> findByCodeContainingKeywordAnywhere(@Param("code") String code);

  @Query(value = "SELECT * FROM tbl_taux_tvas t  WHERE t.libelle LIKE  %?1%", nativeQuery = true)
  List<TauxTvaEntity> findByLibelleContainingKeywordAnywhere(@Param("libelle") String libelle);

  @Query(value = "SELECT * FROM tbl_taux_tvas t  WHERE t.taux LIKE  %?1%", nativeQuery = true)
  List<TauxTvaEntity> findByTauxContainingKeywordAnywhere(@Param("taux") Double taux);

  @Query(value = "SELECT * FROM tbl_taux_tvas t  WHERE t.type_tva LIKE  %?1%", nativeQuery = true)
  List<TauxTvaEntity> findByTypeTvaContainingKeywordAnywhere(@Param("typeTva") Long typeTva);

  @Query(value = "SELECT * FROM tbl_taux_tvas p WHERE p.code LIKE %?1%"
          + " OR p.libelle LIKE %?1%", nativeQuery = true)
  List<TauxTvaEntity> search(String statut);
}
