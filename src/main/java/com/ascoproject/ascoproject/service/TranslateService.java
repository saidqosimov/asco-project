package com.ascoproject.ascoproject.service;

import com.ascoproject.ascoproject.entity.TranslateEntity;
import com.ascoproject.ascoproject.model.InfoEntityModel;
import com.ascoproject.ascoproject.model.TaxInfoModel;
import com.ascoproject.ascoproject.repository.TranslateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslateService {
    private final TranslateRepository translateRepository;

    public synchronized String translate(String uz) {
        if (uz == null || uz.isEmpty()) {
            return uz;
        }
        return translateRepository.findByUz(uz)
                .map(TranslateEntity::getRu)
                .orElse(uz);
    }

    public synchronized void setTranslateRepository(List<TranslateEntity> translateEntities) {
        translateRepository.saveAll(translateEntities);
    }

    public synchronized void updateTranslated(TaxInfoModel uz, TaxInfoModel ru) {
        List<TranslateEntity> translateEntities = new LinkedList<>();
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getTaxType())
                .ru(ru.getTaxType())
                .build());
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getReportName())
                .ru(ru.getReportName())
                .build());
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getReportingPeriod())
                .ru(ru.getReportingPeriod())
                .build());
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getReportComment())
                .ru(ru.getReportComment())
                .build());
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getTaxName())
                .ru(ru.getTaxName())
                .build());
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getPaymentPeriod())
                .ru(ru.getPaymentPeriod())
                .build());
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getTaxComment())
                .ru(ru.getTaxComment())
                .build());
        translateRepository.saveAll(translateEntities);
    }

    public synchronized void updateTranslated(InfoEntityModel uz, InfoEntityModel ru) {
        List<TranslateEntity> translateEntities = new LinkedList<>();
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getTypeOfTax())
                .ru(ru.getTypeOfTax())
                .build());
        translateEntities.add(TranslateEntity.builder()
                .uz(uz.getFullInfo())
                .ru(ru.getFullInfo())
                .build());
        translateRepository.saveAll(translateEntities);
    }

    public String translateRuToUz(String ru) {
        if (ru == null || ru.isEmpty()) {
            return ru;
        }
        return translateRepository.findByRu(ru)
                .map(TranslateEntity::getUz)
                .orElse(ru);
    }
}
