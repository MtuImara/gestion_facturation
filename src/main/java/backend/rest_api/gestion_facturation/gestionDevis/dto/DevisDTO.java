package backend.rest_api.gestion_facturation.gestionDevis.dto;

import java.util.List;

import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionClient.dto.ClientDTO;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;

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
public class DevisDTO {

    private Long id;
    private ServiceDTO service;
    private String code;
    private String reference;
    private String denominationClient;
    private String commentaire;
    private StaticValue typeStatut;
    private String dateOperation;
    private ClientDTO client;
    private List<DevisDetailDTO> devisDetail;
    private String dateCreation;
    private String dateModification;
    private String idUtilisateurCreation;

    public DevisDTO modifyValues(DevisDTO updated) {

        this.setService(updated.getService() != null ? updated.getService() : this.getService());
        this.setCode(updated.getCode() != null ? updated.getCode() : this.getCode());
        this.setReference(updated.getReference() != null ? updated.getReference()
                : this.getReference());
        this.setDenominationClient(updated.getDenominationClient() != null ? updated.getDenominationClient()
                : this.getDenominationClient());
        this.setCommentaire(updated.getCommentaire() != null ? updated.getCommentaire()
                : this.getCommentaire());
        this.setTypeStatut(updated.getTypeStatut() != null ? updated.getTypeStatut() : this.getTypeStatut());
        this.setDateOperation(
                updated.getDateOperation() != null ? updated.getDateOperation() : this.getDateOperation());
        this.setClient(updated.getClient() != null ? updated.getClient() : this.getClient());
        this.setDevisDetail(updated.getDevisDetail() != null ? updated.getDevisDetail()
                : this.getDevisDetail());
        this.setDateCreation(updated.getDateCreation() != null ? updated.getDateCreation()
                : this.getDateCreation());
        this.setDateModification(updated.getDateModification() != null ? updated.getDateModification()
                : this.getDateModification());
        this.setIdUtilisateurCreation(updated.getIdUtilisateurCreation() != null ? updated.getIdUtilisateurCreation()
                : this.getIdUtilisateurCreation());

        return this;
    }

}
