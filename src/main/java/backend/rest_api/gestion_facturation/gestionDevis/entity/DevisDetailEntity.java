package backend.rest_api.gestion_facturation.gestionDevis.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceDetailEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_detail_devis")
public class DevisDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11)
    private Long id;

    @Column(name = "id_service_detail", nullable = true)
    private Long idServiceDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_service_detail", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private ServiceDetailEntity serviceDetail;

    @Column(name = "designation")
    private String designation;

    @Column(name = "quantite")
    private Double quantite;

    @Column(name = "taux_tva")
    private Double tauxTva;

    @Column(name = "prix_unit_ht")
    private Double prixUnitHt;

    @Column(name = "prix_total")
    private BigDecimal prixTotal;

    @Column(name = "id_devis", nullable = true)
    private Long idDevis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_devis", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private DevisEntity devis;

}
