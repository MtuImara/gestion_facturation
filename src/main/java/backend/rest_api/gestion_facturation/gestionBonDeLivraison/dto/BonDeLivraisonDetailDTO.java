package backend.rest_api.gestion_facturation.gestionBonDeLivraison.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import backend.rest_api.gestion_facturation.gestionServices.dto.ServiceDTO;
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
    private ServiceDTO service;
    private String designation;
    private Double quantite;
    private Double prixUnitHt;
    private BigDecimal montantHt;

    public BonDeLivraisonDetailDTO modifyValues(BonDeLivraisonDetailDTO updated) {
        this.setService(updated.getService() != null ? updated.getService() : this.getService());
        this.setDesignation(
                updated.getDesignation() != null ? updated.getDesignation() : this.getDesignation());
        this.setQuantite(updated.getQuantite() != null ? updated.getQuantite() : this.getQuantite());
        this.setPrixUnitHt(updated.getPrixUnitHt() != null ? updated.getPrixUnitHt() : this.getPrixUnitHt());

        return this;
    }

}
