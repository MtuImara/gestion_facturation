package backend.rest_api.gestion_facturation.gestionBonDeCommande.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import backend.rest_api.gestion_facturation.gestionBonDeCommande.dto.BonDeCommandeDetailDTO;
import backend.rest_api.gestion_facturation.gestionBonDeCommande.entity.BonDeCommandeDetailEntity;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceDetailMapper;
import backend.rest_api.gestion_facturation.gestionServices.mapper.ServiceMapper;

@Component
public class BonDeCommandeDetailMapper {

    public BonDeCommandeDetailMapper() {
    }

    public static BonDeCommandeDetailMapper getInstance() {
        return new BonDeCommandeDetailMapper();
    }

    public BonDeCommandeDetailEntity convertToEntity(BonDeCommandeDetailDTO dto) {

        BonDeCommandeDetailEntity entity = new BonDeCommandeDetailEntity();

        entity.setId(dto.getId());
        if (dto.getServiceDetail() != null) {
            entity.setIdServiceDetail(dto.getServiceDetail().getId());
        }
        entity.setIdBonDeCommande(dto.getIdBonDeCommande());
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

    public BonDeCommandeDetailDTO convertToDto(BonDeCommandeDetailEntity entity) {

        BonDeCommandeDetailDTO dto = new BonDeCommandeDetailDTO();

        dto.setId(entity.getId());
        if (entity.getServiceDetail() != null) {
            dto.setServiceDetail(ServiceDetailMapper.getInstance()
                    .convertToDto(entity.getServiceDetail()));
        }
        dto.setIdServiceDetail(entity.getIdServiceDetail());
        dto.setIdBonDeCommande(entity.getIdBonDeCommande());
        dto.setDesignation(entity.getDesignation());
        dto.setQuantite(entity.getQuantite());
        dto.setPrixUnitHt(entity.getPrixUnitHt());
        if (entity.getTauxTva() != null) {
            dto.setTauxTva(entity.getTauxTva());
        } else {
            dto.setTauxTva(0.0);
        }
        dto.setMontantHt(entity.getPrixTotal());

        return dto;

    }

}
