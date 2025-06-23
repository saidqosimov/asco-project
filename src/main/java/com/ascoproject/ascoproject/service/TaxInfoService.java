package com.ascoproject.ascoproject.service;

import com.ascoproject.ascoproject.entity.TaxInfoEntity;
import com.ascoproject.ascoproject.entity.UserEntity;
import com.ascoproject.ascoproject.model.GroupAddTaxRequest;
import com.ascoproject.ascoproject.model.TaxInfoModel;
import com.ascoproject.ascoproject.model.TaxInfoResponse;
import com.ascoproject.ascoproject.model.TaxTypes;
import com.ascoproject.ascoproject.repository.TaxInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxInfoService {
    private final TaxInfoRepository taxInfoRepository;
    private final TranslateService translateService;
    private final UserService userService;


    public synchronized TaxInfoEntity save(TaxInfoEntity taxInfoEntity) {
        return taxInfoRepository.save(taxInfoEntity);
    }

    public synchronized void saveAll(List<TaxInfoEntity> taxInfoEntities) {
        taxInfoRepository.saveAll(taxInfoEntities);
    }

    public synchronized List<TaxInfoEntity> findAll() {
        return taxInfoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public synchronized Page<TaxInfoResponse> findAll(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }
        List<TaxInfoResponse> collect = taxInfoRepository.findAllTaxInfo().stream()
                .filter(t -> t.getTaxType() != null && !t.isDeleted())
                .collect(Collectors.groupingBy(
                        TaxInfoEntity::getTaxType,
                        LinkedHashMap::new, // tartibni saqlash uchun!
                        Collectors.mapping(this::mapToModel, Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> {
                    TaxInfoResponse response = new TaxInfoResponse();
                    response.setTaxType(entry.getKey());
                    response.setTaxesByType(entry.getValue());
                    return response;
                })
                .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), collect.size());
        List<TaxInfoResponse> pageContent = collect.subList(start, end);
        return new PageImpl<>(pageContent, pageable, collect.size());
    }

    private TaxInfoModel mapToModel(TaxInfoEntity entity) {
        TaxInfoModel model = new TaxInfoModel();
        model.setId(entity.getId());
        model.setTaxType(entity.getTaxType());
        model.setReportName(entity.getReportName());
        model.setReportingPeriod(entity.getReportingPeriod());
        model.setReportDate(entity.getReportDate());
        model.setReportComment(entity.getReportComment());
        model.setTaxName(entity.getTaxName());
        model.setPaymentPeriod(entity.getPaymentPeriod());
        model.setTaxDate(entity.getTaxDate());
        model.setTaxComment(entity.getTaxComment());
        model.setDeleted(entity.isDeleted());
        return model;
    }

    private TaxInfoModel mapToTranslateModel(TaxInfoEntity entity) {
        TaxInfoModel model = new TaxInfoModel();
        model.setId(entity.getId());
        model.setTaxType(translateSafe(entity.getTaxType()));
        model.setReportName(translateSafe(entity.getReportName()));
        model.setReportingPeriod(translateSafe(entity.getReportingPeriod()));
        model.setReportDate(entity.getReportDate());
        model.setReportComment(translateSafe(entity.getReportComment()));
        model.setTaxName(translateSafe(entity.getTaxName()));
        model.setPaymentPeriod(translateSafe(entity.getPaymentPeriod()));
        model.setTaxDate(entity.getTaxDate());
        model.setTaxComment(translateSafe(entity.getTaxComment()));
        model.setDeleted(entity.isDeleted());
        return model;
    }

    public List<String> getTaxTypesUz() {
        return taxInfoRepository.findAllTaxInfo().stream()
                .map(TaxInfoEntity::getTaxType)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new)) // tartibni saqlaydi
                .stream()
                .toList();
    }

    public List<String> getTaxTypesRu() {
        return taxInfoRepository.findAllTaxInfo().stream()
                .map(TaxInfoEntity::getTaxType)
                .filter(Objects::nonNull)
                .map(translateService::translate) // 🔁 ruscha tarjima
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new)) // tartibni saqlaydi, dublikat yo'q
                .stream()
                .toList();
    }


    @Transactional(readOnly = true)
    public Page<TaxInfoResponse> findAllRu(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }

        // 1. Sahifalab olinadi
        Page<TaxInfoEntity> originalPage = taxInfoRepository.findAll(pageable);

        List<TaxInfoResponse> collect = taxInfoRepository.findAllTaxInfo().stream()
                .filter(t -> t.getTaxType() != null && !t.isDeleted())
                .collect(Collectors.groupingBy(
                        TaxInfoEntity::getTaxType,
                        LinkedHashMap::new, // tartibni saqlash uchun!
                        Collectors.mapping(this::mapToTranslateModel, Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> {
                    TaxInfoResponse response = new TaxInfoResponse();
                    response.setTaxType(translateSafe(entry.getKey()));
                    response.setTaxesByType(entry.getValue());
                    return response;
                })
                .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), collect.size());
        List<TaxInfoResponse> pageContent = collect.subList(start, end);

        return new PageImpl<>(pageContent, pageable, collect.size());
    }

    private String translateSafe(String text) {
        return text == null ? null : translateService.translate(text);
    }

/*
    @Transactional(readOnly = true)
    public synchronized Page<TaxInfoEntity> findAllRu(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }
        Page<TaxInfoEntity> originalPage = taxInfoRepository.findAll(pageable);

        List<TaxInfoEntity> translatedList = originalPage.getContent().stream()
                .map(taxInfoEntity -> TaxInfoEntity.builder()
                        .id(taxInfoEntity.getId())
                        .taxType(translateService.translate(taxInfoEntity.getTaxType()))
                        .reportName(translateService.translate(taxInfoEntity.getReportName()))
                        .reportingPeriod(translateService.translate(taxInfoEntity.getReportingPeriod()))
                        .reportDate(taxInfoEntity.getReportDate())
                        .reportComment(translateService.translate(taxInfoEntity.getReportComment()))
                        .taxName(translateService.translate(taxInfoEntity.getTaxName()))
                        .paymentPeriod(translateService.translate(taxInfoEntity.getPaymentPeriod()))
                        .taxDate(taxInfoEntity.getTaxDate())
                        .taxComment(translateService.translate(taxInfoEntity.getTaxComment()))
                        .deleted(taxInfoEntity.isDeleted())
                        .build())
                .toList();

        return new PageImpl<>(translatedList, pageable, originalPage.getTotalElements());
    }
*/


    public synchronized TaxInfoEntity findTaxInfoEntityById(Long id) {
        return taxInfoRepository.findTaxInfoEntityById(id);
    }

    public synchronized void updateTaxById(Long id, TaxInfoModel uz, TaxInfoModel ru) {
        TaxInfoEntity taxInfoEntityById = findTaxInfoEntityById(id);
        taxInfoEntityById.setTaxType(uz.getTaxType());
        taxInfoEntityById.setReportName(uz.getReportName());
        taxInfoEntityById.setReportingPeriod(uz.getReportingPeriod());
        taxInfoEntityById.setReportDate(uz.getReportDate());
        taxInfoEntityById.setReportComment(uz.getReportComment());
        taxInfoEntityById.setTaxName(uz.getTaxName());
        taxInfoEntityById.setPaymentPeriod(uz.getPaymentPeriod());
        taxInfoEntityById.setTaxDate(uz.getTaxDate());
        taxInfoEntityById.setTaxComment(uz.getTaxComment());
        taxInfoEntityById.setDeleted(uz.isDeleted());
        taxInfoRepository.save(taxInfoEntityById);
        translateService.updateTranslated(uz, ru);
    }

    public synchronized void deleteAllTaxInfo() {
        userService.deleteAllTaxInfo();
        taxInfoRepository.deleteAll();
        taxInfoRepository.truncateTaxInfo();
    }

    public synchronized void deleteTaxById(Long id) {
        userService.deleteTaxInfo(id);
        taxInfoRepository.deleteTaxInfo(id);
    }

    public synchronized void truncateTaxInfo() {
        taxInfoRepository.truncateTaxInfo();
    }

    public void addTaxReport(TaxInfoModel uz, TaxInfoModel ru) {
        TaxInfoEntity taxInfoEntity = new TaxInfoEntity();
        taxInfoEntity.setTaxType(uz.getTaxType());
        taxInfoEntity.setReportName(uz.getReportName());
        taxInfoEntity.setReportingPeriod(uz.getReportingPeriod());
        taxInfoEntity.setReportDate(uz.getReportDate());
        taxInfoEntity.setReportComment(uz.getReportComment());
        taxInfoEntity.setTaxName(uz.getTaxName());
        taxInfoEntity.setPaymentPeriod(uz.getPaymentPeriod());
        taxInfoEntity.setTaxDate(uz.getTaxDate());
        taxInfoEntity.setTaxComment(uz.getTaxComment());
        taxInfoEntity.setDeleted(false);
        taxInfoRepository.save(taxInfoEntity);
        translateService.updateTranslated(uz, ru);
    }


    public synchronized List<TaxInfoEntity> getTaxInfoByType(String typeTax) {
        return taxInfoRepository.findAllByTaxType(typeTax);
    }

    public synchronized void addTaxesGroupServiceUz(Long chatId, GroupAddTaxRequest groupAddTaxRequest) {
        List<TaxInfoEntity> taxInfoEntityList = new LinkedList<>();
        for (TaxTypes taxType : groupAddTaxRequest.getTaxTypes()) {
            if (taxType.getChecked()) {
                taxInfoEntityList.addAll(getTaxInfoByType(taxType.getTaxType()));
            }
        }
        userService.addTaxesGroup(chatId, taxInfoEntityList);
    }

    public synchronized void addTaxesGroupServiceRu(Long chatId, GroupAddTaxRequest groupAddTaxRequest) {
        List<TaxInfoEntity> taxInfoEntityList = new LinkedList<>();
        for (TaxTypes taxType : groupAddTaxRequest.getTaxTypes()) {
            if (taxType.getChecked()) {
                taxInfoEntityList.addAll(getTaxInfoByType(translateService.translateRuToUz(taxType.getTaxType())));
            }
        }
        userService.addTaxesGroup(chatId, taxInfoEntityList);
    }

}
