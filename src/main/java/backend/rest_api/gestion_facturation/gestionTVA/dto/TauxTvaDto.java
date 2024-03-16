package backend.rest_api.gestion_facturation.gestionTVA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class TauxTvaDto {

  private Long id;
  private String code;
  private String libelle;
  private Double taux;
  private Boolean typeTva;
}
