package backend.rest_api.gestion_facturation.gestionClient.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import backend.rest_api.gestion_facturation.gestionClient.entity.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity> {

    @Query("SELECT count(u) > 0 FROM ClientEntity u  WHERE u.code=:code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT G FROM ClientEntity G WHERE G.code=:code and G.id!=:id")
    Optional<ClientEntity> verificationCode(@Param("id") Long id, @Param("code") String code);

}
