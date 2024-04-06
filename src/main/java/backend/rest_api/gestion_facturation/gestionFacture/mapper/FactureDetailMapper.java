package backend.rest_api.gestion_facturation.gestionFacture.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionFacture.dto.FactureDetailDTO;
import backend.rest_api.gestion_facturation.gestionFacture.entity.FactureDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;

@Component
public class FactureDetailMapper {

    public FactureDetailMapper() {
    }

    public static FactureDetailMapper getInstance() {
        return new FactureDetailMapper();
    }

    public FactureDetailEntity convertToEntity(FactureDetailDTO dto) {

        FactureDetailEntity entity = new FactureDetailEntity();

        entity.setId(dto.getId());
        if (dto.getServiceDetail() != null) {
            entity.setIdServiceDetail(dto.getServiceDetail().getId());
        }
        entity.setIdFacture(dto.getIdFacture());
        entity.setIdServiceDetail(dto.getIdServiceDetail());
        entity.setDesignation(dto.getDesignation());
        entity.setQuantite(dto.getQuantite());
        entity.setPrixUnitHt(dto.getPrixUnitHt());
        entity.setTauxTva(dto.getTauxTva());
        if (dto.getTauxTva() != null || dto.getTauxTva() == 0) {
            entity.setPrixTotal(new BigDecimal((dto.getQuantite() * dto.getPrixUnitHt())
                    + ((dto.getQuantite() * dto.getPrixUnitHt()) * dto.getTauxTva() / 100)));
        } else {
            entity.setPrixTotal(new BigDecimal((dto.getQuantite() * dto.getPrixUnitHt())));
        }

        return entity;

    }

    public FactureDetailDTO convertToDto(FactureDetailEntity entity) {

        FactureDetailDTO dto = new FactureDetailDTO();

        dto.setId(entity.getId());
        if (entity.getServiceDetail() != null) {
            dto.setServiceDetail(ServiceDetailMapper.getInstance()
                    .convertToDto(entity.getServiceDetail()));
        }
        dto.setIdServiceDetail(entity.getIdServiceDetail());
        dto.setIdFacture(entity.getIdFacture());
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixUnitHt(entity.getPrixUnitHt());
        if (entity.getTauxTva() != null) {
            dto.setTauxTva(entity.getTauxTva());
        } else {
            dto.setTauxTva(0.0);
        }
        dto.setMontantHt(entity.getPrixTotal());

        // if (entity.getTauxTva() != null) {
        // dto.setMontantHt(new BigDecimal((dto.getQuantite() * dto.getPrixUnitHt()) *
        // (dto.getTauxTva() / 100)));
        // } else {
        // dto.setMontantHt(new BigDecimal(dto.getQuantite() * dto.getPrixUnitHt()));
        // }

        return dto;

    }

}
