package backend.rest_api.gestion_facturation.gestionUnitesDeMesure.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.dto.UnitesDeMesureDTO;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.entity.UnitesDeMesureEntity;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.mapper.UnitesDeMesureMapper;
import backend.rest_api.gestion_facturation.gestionUnitesDeMesure.repository.UnitesDeMesureRepository;
import backend.rest_api.gestion_facturation.helpers.PagingAndSortingHelper;

@Service
public class UnitesDeMesureService {

    @Autowired
    private UnitesDeMesureRepository familleRepo;

    public Map<String, Object> getAll(String title, int page, int size, String[] sort) {

        Pageable pagingSort = PagingAndSortingHelper.pagination(sort, page, size);

        Page<UnitesDeMesureEntity> uniteEntity = null;

        if (title == null || title.equals("")) {
            uniteEntity = familleRepo.findAll(pagingSort);
        } else {
        }

        List<UnitesDeMesureDTO> uniteDtos = new ArrayList<>();

        for (UnitesDeMesureEntity uniteEntities : uniteEntity) {
            uniteDtos.add(UnitesDeMesureMapper.getInstance().convertToDto(uniteEntities));
        }

        Map<String, Object> data = PagingAndSortingHelper.filteredAndSortedResult(
                uniteEntity.getNumber(),
                uniteEntity.getTotalElements(),
                uniteEntity.getTotalPages(),
                uniteDtos);

        return data;
    }

    public UnitesDeMesureDTO getById(Long id) {

        UnitesDeMesureEntity uniteEntity = null;
        try {
            uniteEntity = familleRepo.getReferenceById(id);
            UnitesDeMesureDTO uniteDto = UnitesDeMesureMapper.getInstance()
                    .convertToDto(uniteEntity);
            return uniteDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public UnitesDeMesureDTO ajoutUniteAchatVenteService(UnitesDeMesureDTO uniteDto) {

        try {
            UnitesDeMesureEntity uniteEntity = new UnitesDeMesureEntity();
            uniteEntity = UnitesDeMesureMapper.getInstance().convertToEntity(uniteDto);
            UnitesDeMesureEntity creation = familleRepo.save(uniteEntity);

            uniteDto = creation != null
                    ? UnitesDeMesureMapper.getInstance().convertToDto(creation)
                    : null;
        } catch (Exception ex) {
            uniteDto = null;
            System.out.println("null" + ex.getMessage());
        }

        return uniteDto;
    }

    public UnitesDeMesureDTO updateUniteAchatVenteService(Long id, UnitesDeMesureDTO updated) {
        UnitesDeMesureEntity converted_familleArticleEntity, updated_familleArticleEntity = null;
        try {

            UnitesDeMesureDTO immobilisation = getById(id);
            converted_familleArticleEntity = UnitesDeMesureMapper.getInstance()
                    .convertToEntity(immobilisation.modifyValues(updated));
            // converted_familleArticleEntity.setDateModification(DateHelper.now());
            updated_familleArticleEntity = familleRepo.save(converted_familleArticleEntity);
            updated = UnitesDeMesureMapper.getInstance().convertToDto(updated_familleArticleEntity);

        } catch (Exception e) {
            System.out.println("Erreur lors de la modification de l'Unite d'Achat et de Vente': " + e.getMessage());
            updated = null;
        }

        return updated;
    }

}
