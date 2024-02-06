package backend.rest_api.gestion_facturation.gestionUnitesDeMesure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.entity.UnitesDeMesureEntity;

@Repository
public interface UnitesDeMesureRepository extends JpaRepository<UnitesDeMesureEntity, Long> {

    @Query("SELECT count(u) > 0 FROM UnitesDeMesureEntity u  WHERE u.code=:code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT G FROM UnitesDeMesureEntity G WHERE G.code=:code and G.id!=:id")
    Optional<UnitesDeMesureEntity> verificationCode(@Param("id") Long id, @Param("code") String code);

}
