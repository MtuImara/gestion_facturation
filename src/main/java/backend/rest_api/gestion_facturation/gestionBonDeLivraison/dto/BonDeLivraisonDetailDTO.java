package backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class BonDeLivraisonDetailDTO {

    private Long id;
    private Long idBonDeLivraison;
    private ServiceDetailDTO serviceDetail;
    private Long idServiceDetail;
    private String designation;
    private Double quantite;
    private Double prixUnitHt;
    private Double tauxTva;
    private BigDecimal montantHt;

    public BonDeLivraisonDetailDTO modifyValues(BonDeLivraisonDetailDTO updated) {
        this.setServiceDetail(
                updated.getServiceDetail() != null ? updated.getServiceDetail()
                        : this.getServiceDetail());
        this.setDesignation(
                updated.getDesignation() != null ? updated.getDesignation() : this.getDesignation());
        this.setIdServiceDetail(
                updated.getIdServiceDetail() != null ? updated.getIdServiceDetail()
                        : this.getIdServiceDetail());
        this.setIdBonDeLivraison(
                updated.getIdBonDeLivraison() != null ? updated.getIdBonDeLivraison() : this.getIdBonDeLivraison());
        this.setQuantite(updated.getQuantite() != null ? updated.getQuantite() : this.getQuantite());
        this.setPrixUnitHt(updated.getPrixUnitHt() != null ? updated.getPrixUnitHt() : this.getPrixUnitHt());
        this.setTauxTva(updated.getTauxTva() != null ? updated.getTauxTva() : this.getTauxTva());
        this.setMontantHt(updated.getMontantHt() != null ? updated.getMontantHt() : this.getMontantHt());

        return this;
    }

}
