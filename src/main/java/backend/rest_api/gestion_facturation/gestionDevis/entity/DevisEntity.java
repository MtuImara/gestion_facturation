package backend.rest_api.gestion_facturation.gestionDevis.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.rest_api.gestion_facturation.gestionClient.entity.ClientEntity;
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
@Table(name = "tbl_devis")
public class DevisEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "reference")
    private String reference;

    @Column(name = "denomination_client")
    private String denominationClient;

    @Column(name = "id_client", nullable = true)
    private Long idClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_client", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private ClientEntity service;

}
