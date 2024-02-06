package backend.rest_api.gestion_facturation.gestionUnitesDeMesure.dto;

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
public class UnitesDeMesureDTO {
    private Long id;
    private String code;
    private String designation;
    private Double valeur;

    public UnitesDeMesureDTO modifyValues(UnitesDeMesureDTO updated) {
        this.setCode(updated.getCode() != null ? updated.getCode() : this.getCode());
        this.setDesignation(
                updated.getDesignation() != null ? updated.getDesignation() : this.getDesignation());
        this.setValeur(updated.getValeur() != null ? updated.getValeur() : this.getValeur());

        return this;
    }
}
