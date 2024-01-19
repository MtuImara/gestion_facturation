package backend.rest_api.gestion_facturation.gestionFacture.dto;

import java.math.BigDecimal;
import java.util.List;

import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionClient.dto.ClientDTO;
import backend.rest_api.gestion_facturation.gestionTVA.dto.TauxTvaDto;

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
public class FactureDTO {

    private Long id;
    private String code;
    private String reference;
    private String denominationClient;
    private String commentaire;
    private StaticValue typeStatut;
    private String dateOperation;
    private ClientDTO client;
    private List<FactureDetailDTO> factureDetail;
    private TauxTvaDto tva;
    private Double tauxTva;
    private BigDecimal montantTotalHT;
    private BigDecimal montantTotalTTC;
    private String dateCreation;
    private String dateModification;
    private String idUtilisateurCreation;

    public FactureDTO modifyValues(FactureDTO updated) {

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
        this.setFactureDetail(updated.getFactureDetail() != null ? updated.getFactureDetail()
                : this.getFactureDetail());
        this.setTva(updated.getTva() != null ? updated.getTva() : this.getTva());
        this.setTauxTva(updated.getTauxTva() != null ? updated.getTauxTva() : this.getTauxTva());
        this.setDateCreation(updated.getDateCreation() != null ? updated.getDateCreation()
                : this.getDateCreation());
        this.setDateModification(updated.getDateModification() != null ? updated.getDateModification()
                : this.getDateModification());
        this.setIdUtilisateurCreation(updated.getIdUtilisateurCreation() != null ? updated.getIdUtilisateurCreation()
                : this.getIdUtilisateurCreation());

        return this;
    }

}
