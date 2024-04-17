package backend.rest_api.gestion_facturation.gestionDevis.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionDevis.dto.DevisDetailDTO;
import backend.rest_api.gestion_facturation.gestionDevis.entity.DevisDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;

@Component
public class DevisDetailMapper {

    public DevisDetailMapper() {
    }

    public static DevisDetailMapper getInstance() {
        return new DevisDetailMapper();
    }

    public DevisDetailEntity convertToEntity(DevisDetailDTO dto) {

        DevisDetailEntity entity = new DevisDetailEntity();

        entity.setId(dto.getId());
        if (dto.getServiceDetail() != null) {
            entity.setIdServiceDetail(dto.getServiceDetail().getId());
        }
        entity.setIdDevis(dto.getIdDevis());
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

    public DevisDetailDTO convertToDto(DevisDetailEntity entity) {

        DevisDetailDTO dto = new DevisDetailDTO();

        dto.setId(entity.getId());
        dto.setIdDevis(entity.getIdDevis());
        if (entity.getServiceDetail() != null) {
            dto.setServiceDetail(ServiceDetailMapper.getInstance()
                    .convertToDto(entity.getServiceDetail()));
        }
        dto.setIdServiceDetail(entity.getIdServiceDetail());
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
