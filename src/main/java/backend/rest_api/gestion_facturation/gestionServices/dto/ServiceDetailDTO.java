package backend.rest_api.gestion_facturation.gestionServices.dto;

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
public class ServiceDetailDTO {

    private Long id;
    private String code;
    private String designation;
    private String uniteMesure;
    private Long idService;
    private ServiceDTO service;

    public ServiceDetailDTO modifyValues(ServiceDetailDTO updated) {

        this.setCode(updated.getCode() != null ? updated.getCode() : this.getCode());
        this.setDesignation(updated.getDesignation() != null ? updated.getDesignation() : this.getDesignation());
        this.setUniteMesure(updated.getUniteMesure() != null ? updated.getUniteMesure()
                : this.getUniteMesure());
        this.setService(updated.getService() != null ? updated.getService()
                : this.getService());

        return this;
    }

}
