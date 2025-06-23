package com.ascoproject.ascoproject.service;

import com.ascoproject.ascoproject.entity.InfoEntity;
import com.ascoproject.ascoproject.model.InfoEntityModel;
import com.ascoproject.ascoproject.model.ResponseAll;
import com.ascoproject.ascoproject.model.ResponseResult;
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
    public ResponseAll<ResponseResult<Page<InfoEntity>>> findAll(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }
        ResponseResult<Page<InfoEntity>> result = new ResponseResult<>();
        result.setResult(infoEntityRepository.findAll(pageable));
        return ResponseAll.<ResponseResult<Page<InfoEntity>>>builder()
                .response(result)
                .status(200)
                .build();
    }

    public void deleteAll() {
        infoEntityRepository.deleteAll();
        infoEntityRepository.truncateInfoEntity();
    }

    public void saveAll(List<InfoEntity> infoEntityList) {
        infoEntityRepository.saveAll(infoEntityList);
    }

    @Transactional(readOnly = true)
    public ResponseAll<ResponseResult<Page<InfoEntity>>> findAllRu(Pageable pageable) {
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
        ResponseResult<Page<InfoEntity>> result = new ResponseResult<>();
        result.setResult(new PageImpl<>(translatedList, pageable, originalPage.getTotalElements()));
        return ResponseAll.<ResponseResult<Page<InfoEntity>>>builder()
                .response(result)
                .status(200)
                .build();
    }

    public ResponseAll<ResponseResult<String>> updateInfoById(Long id, InfoEntityModel uz, InfoEntityModel ru) {
        InfoEntity infoEntityById = infoEntityRepository.findById(id).orElse(null);
        assert infoEntityById != null;
        infoEntityById.setTypeOfTax(uz.getTypeOfTax());
        infoEntityById.setFullInfo(uz.getFullInfo());
        infoEntityRepository.save(infoEntityById);
        translateService.updateTranslated(uz, ru);
        ResponseResult<String> result = new ResponseResult<>();
        result.setResult("update successfully");
        return ResponseAll.<ResponseResult<String>>builder()
                .response(result)
                .status(200)
                .build();
    }

    public ResponseAll<ResponseResult<String>> deleteInfoEntityById(Long id) {
        infoEntityRepository.deleteById(id);
        ResponseResult<String> result = new ResponseResult<>();
        result.setResult("delete successfully");
        return ResponseAll.<ResponseResult<String>>builder()
                .response(result)
                .status(200)
                .build();
    }

    public ResponseAll<ResponseResult<String>> addInfoEntity(InfoEntityModel uz, InfoEntityModel ru) {
        InfoEntity infoEntity = new InfoEntity();
        infoEntity.setTypeOfTax(uz.getTypeOfTax());
        infoEntity.setFullInfo(uz.getFullInfo());
        infoEntityRepository.save(infoEntity);
        translateService.updateTranslated(uz, ru);
        ResponseResult<String> result = new ResponseResult<>();
        result.setResult("info_entity successfully added");
        return ResponseAll.<ResponseResult<String>>builder()
                .response(result)
                .status(200)
                .build();
    }
}
