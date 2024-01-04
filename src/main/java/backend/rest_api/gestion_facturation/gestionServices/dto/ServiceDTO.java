package backend.rest_api.gestion_facturation.gestionServices.dto;

import java.util.List;

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
public class ServiceDTO {

    private Long id;
    private String code;
    private String designation;
    private String commentaire;
    private List<ServiceDetailDTO> serviceDetail;

    public ServiceDTO modifyValues(ServiceDTO updated) {

        this.setCode(updated.getCode() != null ? updated.getCode() : this.getCode());
        this.setDesignation(updated.getDesignation() != null ? updated.getDesignation() : this.getDesignation());
        this.setCommentaire(updated.getCommentaire() != null ? updated.getCommentaire()
                : this.getCommentaire());
        this.setServiceDetail(updated.getServiceDetail() != null ? updated.getServiceDetail()
                : this.getServiceDetail());

        return this;
    }

}
