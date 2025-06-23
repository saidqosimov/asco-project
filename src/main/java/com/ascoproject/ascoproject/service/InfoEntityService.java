package com.ascoproject.ascoproject.service;

import com.ascoproject.ascoproject.entity.InfoEntity;
import com.ascoproject.ascoproject.entity.TaxInfoEntity;
import com.ascoproject.ascoproject.model.InfoEntityModel;
import com.ascoproject.ascoproject.repository.InfoEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InfoEntityService {
    private final InfoEntityRepository infoEntityRepository;
    private final TranslateService translateService;

    public List<InfoEntity> findAll() {
        return infoEntityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<InfoEntity> findAll(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }
        return infoEntityRepository.findAll(pageable);
    }

    public void deleteAll() {
        infoEntityRepository.deleteAll();
        infoEntityRepository.truncateInfoEntity();
    }

    public void saveAll(List<InfoEntity> infoEntityList) {
        infoEntityRepository.saveAll(infoEntityList);
    }

    @Transactional(readOnly = true)
    public Page<InfoEntity> findAllRu(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }
        Page<InfoEntity> originalPage = infoEntityRepository.findAll(pageable);

        List<InfoEntity> translatedList = originalPage.getContent().stream()
                .map(infoEntity -> InfoEntity.builder()
                        .id(infoEntity.getId())
                        .typeOfTax(translateService.translate(infoEntity.getTypeOfTax()))
                        .fullInfo(translateService.translate(infoEntity.getFullInfo()))
                        .build())
                .toList();

        return new PageImpl<>(translatedList, pageable, originalPage.getTotalElements());
    }

    public void updateInfoById(Long id, InfoEntityModel uz, InfoEntityModel ru) {
        InfoEntity infoEntityById = infoEntityRepository.findById(id).orElse(null);
        if (infoEntityById == null) {
            return;
        }
        infoEntityById.setTypeOfTax(uz.getTypeOfTax());
        infoEntityById.setFullInfo(uz.getFullInfo());
        infoEntityRepository.save(infoEntityById);
        translateService.updateTranslated(uz, ru);
    }

    public void deleteInfoEntityById(Long id) {
        infoEntityRepository.deleteById(id);
    }

    public void addInfoEntity(InfoEntityModel uz, InfoEntityModel ru) {
        InfoEntity infoEntity = new InfoEntity();
        infoEntity.setTypeOfTax(uz.getTypeOfTax());
        infoEntity.setFullInfo(uz.getFullInfo());
        infoEntityRepository.save(infoEntity);
        translateService.updateTranslated(uz, ru);
    }
}
