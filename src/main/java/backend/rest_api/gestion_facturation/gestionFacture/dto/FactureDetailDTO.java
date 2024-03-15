package backend.rest_api.gestion_facturation.gestionFacture.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class FactureDetailDTO {

    private Long id;
    private Long idFacture;
    private ServiceDetailDTO serviceDetail;
    private Long idServiceDetail;
    private String designation;
    private Double quantite;
    private Double prixUnitHt;
    private BigDecimal montantHt;

    public FactureDetailDTO modifyValues(FactureDetailDTO updated) {
        this.setServiceDetail(updated.getServiceDetail() != null ? updated.getServiceDetail() : this.getServiceDetail());
        this.setDesignation(
                updated.getDesignation() != null ? updated.getDesignation() : this.getDesignation());
        this.setIdServiceDetail(updated.getIdServiceDetail() != null ? updated.getIdServiceDetail() : this.getIdServiceDetail());
        this.setIdFacture(updated.getIdFacture() != null ? updated.getIdFacture() : this.getIdFacture());
        this.setQuantite(updated.getQuantite() != null ? updated.getQuantite() : this.getQuantite());
        this.setPrixUnitHt(updated.getPrixUnitHt() != null ? updated.getPrixUnitHt() : this.getPrixUnitHt());

        return this;
    }

}
