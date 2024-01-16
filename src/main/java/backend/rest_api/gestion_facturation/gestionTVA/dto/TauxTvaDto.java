package backend.rest_api.gestion_facturation.gestionTVA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TauxTvaDto {

  private Long id;
  private String code;
  private String libelle;
  private Double taux;
  private Boolean typeTva;
}
