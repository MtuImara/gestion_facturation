package backend.rest_api.gestion_facturation.gestionBonDeCommande.dto;

import java.math.BigDecimal;
import java.util.List;

import backend.rest_api.gestion_facturation.constantes.StaticValue;
import backend.rest_api.gestion_facturation.gestionClient.dto.ClientDTO;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
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
public class BonDeCommandeDTO {

        private Long id;
        private String code;
        private String reference;
        private String denominationClient;
        private String commentaire;
        private String etat;
        private StaticValue typeStatut;
        private String dateOperation;
        private ClientDTO client;
        private Long idClient;
        private ServiceDTO service;
        private Long id_service;
        private List<BonDeCommandeDetailDTO> bonDeCommandeDetail;
        private TauxTvaDto tva;
        private Long idTva;
        private Double tauxTva;
        private BigDecimal montantTotalHT;
        private BigDecimal montantTva;
        private BigDecimal montantTotalTTC;
        private String dateCreation;
        private String dateModification;
        private String idUtilisateurCreation;

        public BonDeCommandeDTO modifyValues(BonDeCommandeDTO updated) {

                this.setCode(updated.getCode() != null ? updated.getCode() : this.getCode());
                this.setReference(updated.getReference() != null ? updated.getReference()
                                : this.getReference());
                this.setDenominationClient(updated.getDenominationClient() != null ? updated.getDenominationClient()
                                : this.getDenominationClient());
                this.setCommentaire(updated.getCommentaire() != null ? updated.getCommentaire()
                                : this.getCommentaire());
                this.setEtat(updated.getEtat() != null ? updated.getEtat() : this.getEtat());
                this.setTypeStatut(updated.getTypeStatut() != null ? updated.getTypeStatut() : this.getTypeStatut());
                this.setDateOperation(
                                updated.getDateOperation() != null ? updated.getDateOperation()
                                                : this.getDateOperation());
                this.setClient(updated.getClient() != null ? updated.getClient() : this.getClient());
                this.setIdClient(updated.getIdClient() != null ? updated.getIdClient() : this.getIdClient());
                this.setService(updated.getService() != null ? updated.getService() : this.getService());
                this.setId_service(updated.getId_service() != null ? updated.getId_service() : this.getId_service());
                this.setBonDeCommandeDetail(updated.getBonDeCommandeDetail() != null ? updated.getBonDeCommandeDetail()
                                : this.getBonDeCommandeDetail());
                this.setTva(updated.getTva() != null ? updated.getTva() : this.getTva());
                this.setIdTva(updated.getIdTva() != null ? updated.getIdTva() : this.getIdTva());
                this.setTauxTva(updated.getTauxTva() != null ? updated.getTauxTva() : this.getTauxTva());
                this.setDateCreation(updated.getDateCreation() != null ? updated.getDateCreation()
                                : this.getDateCreation());
                this.setDateModification(updated.getDateModification() != null ? updated.getDateModification()
                                : this.getDateModification());
                this.setIdUtilisateurCreation(
                                updated.getIdUtilisateurCreation() != null ? updated.getIdUtilisateurCreation()
                                                : this.getIdUtilisateurCreation());

                return this;
        }

}
