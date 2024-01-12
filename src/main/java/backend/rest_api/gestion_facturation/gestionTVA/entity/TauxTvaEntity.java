package backend.rest_api.gestion_facturation.gestionTVA.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "tbl_taux_tvas")
public class TauxTva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11)
    private Long id;

    @Column(name = "code", length = 50, unique = true)
    private String code;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "taux")
    private Double taux;

    @Column(name = "type_tva")
    private Boolean typeTva;

}
