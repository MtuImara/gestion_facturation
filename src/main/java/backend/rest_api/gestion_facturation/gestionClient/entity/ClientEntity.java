package backend.rest_api.gestion_facturation.gestionClient.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tbl_client")
public class ClientEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "nom")
	private String nom;

	@Column(name = "prenom")
	private String prenom;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "fax")
	private String fax;

	@Column(name = "adresse")
	private String adresse;

	@Column(name = "email")
	private String email;

	@Column(name = "registre_commerce")
	private String registreCommerce;

	@Column(name = "nif")
	private String nif;

	@Column(name = "assujetti_tva")
	private Boolean assujettiTva;

	@Column(name = "date_creation")
	private Date dateCreation;

	@Column(name = "id_utilisateur_creation")
	private Integer idUtilisateurCreation;

    @Column(name = "utilisateur_creation")
	private String nomUtilisateurCreation;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "utilisateur_creation", referencedColumnName = "id", insertable = false, updatable = false)
	// private GuUtilisateurEntity utilisateurCreation;

	// @OneToMany(mappedBy = "clientOuAutreSource")
	// private List<TresoFactureClientEntity> factureClients = new ArrayList<>();
    
}
