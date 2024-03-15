package backend.rest_api.gestion_facturation.gestionDevis.dto;

import java.math.BigDecimal;

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
public class DevisDetailDTO {

    private Long id;
    private Long idDevis;
    private ServiceDTO service;
    private Long id_service;
    private String designation;
    private Double quantite;
    private Double prixUnitHt;
    private BigDecimal montantHt;

    public DevisDetailDTO modifyValues(DevisDetailDTO updated) {
        this.setService(updated.getService() != null ? updated.getService() : this.getService());
        this.setDesignation(
                updated.getDesignation() != null ? updated.getDesignation() : this.getDesignation());
        this.setQuantite(updated.getQuantite() != null ? updated.getQuantite() : this.getQuantite());
        this.setPrixUnitHt(updated.getPrixUnitHt() != null ? updated.getPrixUnitHt() : this.getPrixUnitHt());
        this.setIdDevis(updated.getIdDevis() != null ? updated.getIdDevis() : this.getIdDevis());
        this.setId_service(updated.getId_service() != null ? updated.getId_service() : this.getId_service());

        return this;
    }

}
