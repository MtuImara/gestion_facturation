package backend.rest_api.gestion_facturation.gestionFacture.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.rest_api.gestion_facturation.gestionClient.entity.ClientEntity;
import backend.rest_api.gestion_facturation.gestionTVA.entity.TauxTvaEntity;
import backend.rest_api.gestion_facturation.gestionServices.entity.ServiceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "tbl_facture")
public class FactureEntity implements Serializable {

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

    @Column(name = "commentaire", nullable = true)
    private String commentaire;

    @Column(name = "type_statut", length = 11)
    private Integer typeStatut;

    @Column(name = "date_operation")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateOperation;

    @Column(name = "date_echeance")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateEcheance;

    @Column(name = "id_client", nullable = true)
    private Long idClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_client", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private ClientEntity client;

    @Column(name = "id_service", nullable = true)
    private Long idService;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_service", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private ServiceEntity service;

    @Column(name = "id_tva", nullable = true)
    private Long idTva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_tva", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private TauxTvaEntity tva;

    @Column(name = "taux_tva")
    private Double tauxTva;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "facture")
    private List<FactureDetailEntity> factureDetail;

    @Column(name = "date_creation")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateCreation;

    @Column(name = "date_modification")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dateModification;

    @Column(name = "utilisateur_creation")
    private String idUtilisateurCreation;

}
