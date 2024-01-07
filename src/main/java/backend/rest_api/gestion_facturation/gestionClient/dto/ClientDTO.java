package backend.rest_api.gestion_facturation.gestionClient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {
    
    private Long id;
    private String code;
	private String nom;
	private String prenom;
	private String telephone;
	private String fax;
	private String adresse;
	private String email;
	private String registreCommerce;
	private String nif;
	private Boolean assujettiTva;
	private String dateCreation;
	private String nomUtilisateurCreation;

    public ClientDTO modifyValues(ClientDTO updated) {

        this.setCode(updated.getCode() != null ? updated.getCode() : this.getCode());
        this.setNom(updated.getNom() != null ? updated.getNom() : this.getNom());
        this.setPrenom(updated.getPrenom() != null ? updated.getPrenom()
                : this.getPrenom());
        this.setTelephone(updated.getTelephone() != null ? updated.getTelephone()
                : this.getTelephone());
        this.setFax(updated.getFax() != null ? updated.getFax() : this.getFax());
        this.setAdresse(updated.getAdresse() != null ? updated.getAdresse()
                : this.getAdresse());
        this.setEmail(updated.getEmail() != null ? updated.getEmail()
                : this.getEmail());
        this.setRegistreCommerce(updated.getRegistreCommerce() != null ? updated.getRegistreCommerce() : this.getRegistreCommerce());
        this.setNif(updated.getNif() != null ? updated.getNif()
                : this.getNif());
        this.setAssujettiTva(updated.getAssujettiTva() != null ? updated.getAssujettiTva()
                : this.getAssujettiTva());
        this.setDateCreation(updated.getDateCreation() != null ? updated.getDateCreation()
                : this.getDateCreation());
        this.setNomUtilisateurCreation(updated.getNomUtilisateurCreation() != null ? updated.getNomUtilisateurCreation()
                : this.getNomUtilisateurCreation());

        return this;
    }
}
