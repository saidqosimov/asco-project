package com.ascoproject.ascoproject.service;

import com.ascoproject.ascoproject.entity.InfoEntity;
import com.ascoproject.ascoproject.entity.TaxInfoEntity;
import com.ascoproject.ascoproject.entity.TranslateEntity;
import com.ascoproject.ascoproject.repository.TaxInfoRepository;
import com.ascoproject.ascoproject.repository.TranslateRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class FileImportService {

    private final TranslateService translateService;
    private final TaxInfoService taxInfoService;
    private final InfoEntityService infoEntityService;


    public void exportTaxReportCsv(HttpServletResponse response) throws IOException {
        List<TaxInfoEntity> taxInfoList = taxInfoService.findAll();

        try (PrintWriter writer = response.getWriter()) {
            // CSV header
            writer.println("tax_type,tax_type_ru,report_name,report_name_ru,reporting_period,reporting_period_ru,report_date,report_comment,report_comment_ru,tax_name,tax_name_ru,payment_period,payment_period_ru,tax_date,tax_comment,tax_comment_ru,deleted");

            // Har bir satrni yozish
            for (TaxInfoEntity taxInfo : taxInfoList) {
                writer.println(String.join(",",
                        safe(taxInfo.getTaxType()),
                        safe(translateService.translate(taxInfo.getTaxType())),
                        safe(taxInfo.getReportName()),
                        safe(translateService.translate(taxInfo.getReportName())),
                        safe(taxInfo.getReportingPeriod()),
                        safe(translateService.translate(taxInfo.getReportingPeriod())),
                        safe(taxInfo.getReportDate()),
                        safe(taxInfo.getReportComment()),
                        safe(translateService.translate(taxInfo.getReportComment())),
                        safe(taxInfo.getTaxName()),
                        safe(translateService.translate(taxInfo.getTaxName())),
                        safe(taxInfo.getPaymentPeriod()),
                        safe(translateService.translate(taxInfo.getPaymentPeriod())),
                        safe(taxInfo.getTaxDate()),
                        safe(taxInfo.getTaxComment()),
                        safe(translateService.translate(taxInfo.getTaxComment())),
                        String.valueOf(taxInfo.isDeleted())
                ));
            }
        }
    }

    private String safe(String val) {
        if (val == null) return "";
        String escaped = val.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    public void importInfoEntityCsv(MultipartFile file) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<InfoEntity> infoEntityList = new LinkedList<>();
            String[] headers = reader.readNext();
            Set<TranslateEntity> translateEntitySet = new HashSet<>();
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toLowerCase(), i);
            }
            String[] line;
            while ((line = reader.readNext()) != null) {
                InfoEntity infoEntity = InfoEntity.builder()
                        .typeOfTax(line[headerMap.get("tax_of_type")])
                        .fullInfo(line[headerMap.get("full_info")])
                        .build();
                infoEntityList.add(infoEntity);
                translateEntitySet.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("type_of_tax")])
                        .ru(line[headerMap.get("type_of_tax_ru")])
                        .build());
                translateEntitySet.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("full_info")])
                        .ru(line[headerMap.get("full_info_ru")])
                        .build());
            }
            infoEntityService.deleteAll();
            infoEntityService.saveAll(infoEntityList);
            translateService.setTranslateRepository(translateEntitySet.stream().toList());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void importCsv(MultipartFile file) throws Exception {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<TaxInfoEntity> taxInfoEntities = new LinkedList<>();
            String[] headers = reader.readNext(); // 1-qator: header
            Set<TranslateEntity> translateEntities = new HashSet<>();
            // Header nomlarini indeks bilan map qilish
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toLowerCase(), i);
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                TaxInfoEntity taxInfoEntity = TaxInfoEntity.builder()
                        .taxType(line[headerMap.get("tax_type")])
                        .reportName(line[headerMap.get("report_name")])
                        .reportingPeriod(line[headerMap.get("reporting_period")])
                        .reportDate(line[headerMap.get("report_date")])
                        .reportComment(line[headerMap.get("report_comment")])
                        .taxName(line[headerMap.get("tax_name")])
                        .paymentPeriod(line[headerMap.get("payment_period")])
                        .taxDate(line[headerMap.get("tax_date")])
                        .taxComment(line[headerMap.get("tax_comment")])
                        .build();
                taxInfoEntities.add(taxInfoEntity);
                translateEntities.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("tax_type")])
                        .ru(line[headerMap.get("tax_type_ru")])
                        .build());
                translateEntities.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("report_name")])
                        .ru(line[headerMap.get("report_name_ru")])
                        .build());
                translateEntities.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("reporting_period")])
                        .ru(line[headerMap.get("reporting_period_ru")])
                        .build());
                translateEntities.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("report_comment")])
                        .ru(line[headerMap.get("report_comment_ru")])
                        .build());
                translateEntities.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("tax_name")])
                        .ru(line[headerMap.get("tax_name_ru")])
                        .build());
                translateEntities.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("payment_period")])
                        .ru(line[headerMap.get("payment_period_ru")])
                        .build());
                translateEntities.add(TranslateEntity.builder()
                        .uz(line[headerMap.get("tax_comment")])
                        .ru(line[headerMap.get("tax_comment_ru")])
                        .build());
            }

            taxInfoService.deleteAllTaxInfo();
            taxInfoService.saveAll(taxInfoEntities);
            translateService.setTranslateRepository(translateEntities.stream().toList());
        }
    }

    public void exportInfoEntityCsv(HttpServletResponse response) throws IOException {
        List<InfoEntity> infoEntities = infoEntityService.findAll();
        try (PrintWriter writer = response.getWriter()) {
            writer.println("type_of_tax,type_of_tax_ru,full_info,full_info_ru");
            for (InfoEntity infoEntity : infoEntities) {
                writer.println(String.join(",",
                        safe(infoEntity.getTypeOfTax()),
                        safe(translateService.translate(infoEntity.getTypeOfTax())),
                        safe(infoEntity.getFullInfo()),
                        safe(translateService.translate(infoEntity.getFullInfo()))
                ));
            }
        }
    }
}
