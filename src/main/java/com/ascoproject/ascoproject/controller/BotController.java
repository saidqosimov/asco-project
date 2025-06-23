package com.ascoproject.ascoproject.controller;

import com.ascoproject.ascoproject.entity.InfoEntity;
import com.ascoproject.ascoproject.model.*;
import com.ascoproject.ascoproject.service.FileImportService;
import com.ascoproject.ascoproject.service.InfoEntityService;
import com.ascoproject.ascoproject.service.TaxInfoService;
import com.ascoproject.ascoproject.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/bot")
@RequiredArgsConstructor
public class BotController {
    private final TaxInfoService taxInfoService;
    private final FileImportService fileImportService;
    private final InfoEntityService infoEntityService;
    private final UserService userService;

    @GetMapping("/tax-report-list/uz/page")
    public ResponseEntity<ResponseResult<Page<TaxInfoResponse>>> getTaxReportListPagingUz(Pageable pageable) {
        ResponseAll<ResponseResult<Page<TaxInfoResponse>>> result = taxInfoService.findAll(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/tax-report-list/ru/page")
    public ResponseEntity<ResponseResult<Page<TaxInfoResponse>>> getTaxReportListPagingRu(Pageable pageable) {
        ResponseAll<ResponseResult<Page<TaxInfoResponse>>> result = taxInfoService.findAllRu(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/info-entity-list/uz/page")
    public ResponseEntity<ResponseResult<Page<InfoEntity>>> getInfoEntityListPagingUz(Pageable pageable) {
        ResponseAll<ResponseResult<Page<InfoEntity>>> result = infoEntityService.findAll(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/info-entity-list/ru/page")
    public ResponseEntity<ResponseResult<Page<InfoEntity>>> getInfoEntityListPagingRu(Pageable pageable) {
        ResponseAll<ResponseResult<Page<InfoEntity>>> result = infoEntityService.findAllRu(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PutMapping("/tax-report-update/{id}")
    public ResponseEntity<ResponseResult<String>> updateTaxReport(@PathVariable Long id, @RequestBody TaxInfoUpdateModel taxInfoUpdateModel) {
        ResponseAll<ResponseResult<String>> result = taxInfoService.updateTaxById(id, taxInfoUpdateModel.getUz(), taxInfoUpdateModel.getRu());
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @DeleteMapping("/tax-report-delete/{id}")
    public ResponseEntity<ResponseResult<?>> deleteTaxReport(@PathVariable Long id) {
        ResponseAll<ResponseResult<String>> result = taxInfoService.deleteTaxById(id);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PutMapping("/info_entity-update/{id}")
    public ResponseEntity<ResponseResult<?>> updateInfoEntity(@PathVariable Long id, @RequestBody InfoEntityUpdateModel infoEntityUpdateModel) {
        ResponseAll<ResponseResult<String>> result = infoEntityService.updateInfoById(id, infoEntityUpdateModel.getUz(), infoEntityUpdateModel.getRu());
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @DeleteMapping("/info-entity-delete/{id}")
    public ResponseEntity<ResponseResult<?>> deleteInfoEntity(@PathVariable Long id) {
        ResponseAll<ResponseResult<String>> result = infoEntityService.deleteInfoEntityById(id);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PostMapping("/add-info_entity")
    public ResponseEntity<ResponseResult<?>> addInfoEntity(@RequestBody InfoEntityUpdateModel infoEntityUpdateModel) {
        ResponseAll<ResponseResult<String>> result = infoEntityService.addInfoEntity(infoEntityUpdateModel.getUz(), infoEntityUpdateModel.getRu());
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PostMapping("/add-tax-report")
    public ResponseEntity<ResponseResult<?>> addTaxReport(@RequestBody TaxInfoUpdateModel taxInfoUpdateModel) {
        ResponseAll<ResponseResult<String>> result = taxInfoService.addTaxReport(taxInfoUpdateModel.getUz(), taxInfoUpdateModel.getRu());
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }
/*
    @GetMapping("/info-list/uz")
    public ResponseEntity<String> getInfoListUz() {
        return ResponseEntity.ok("Info List");
    }

    @GetMapping("/info-list/ru")
    public ResponseEntity<String> getInfoListRu() {
        return ResponseEntity.ok("Info List");
    }*/

    @PostMapping("/import-tax-info-csv")
    public ResponseEntity<ResponseResult<?>> uploadTaxInfoCsv(@RequestParam("file") MultipartFile file) throws Exception {
        ResponseAll<ResponseResult<String>> result = fileImportService.importCsv(file);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/export-tax-info")
    public ResponseEntity<ResponseResult<?>> exportTaxInfoToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"tax_info.csv\"");
        ResponseAll<ResponseResult<String>> result = fileImportService.exportTaxReportCsv(response);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PostMapping("/import-info-entity-csv")
    public ResponseEntity<ResponseResult<?>> uploadInfoEntityCsv(@RequestParam("file") MultipartFile file) throws Exception {
        ResponseAll<ResponseResult<String>> result = fileImportService.importInfoEntityCsv(file);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/export-info-entity")
    public ResponseEntity<ResponseResult<?>> exportInfoEntityToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"info_entity.csv\"");
        ResponseAll<ResponseResult<String>> result = fileImportService.exportInfoEntityCsv(response);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-users/uz/page")
    public ResponseEntity<ResponseResult<Page<UserModel>>> getUsers(Pageable pageable) {
        ResponseAll<ResponseResult<Page<UserModel>>> result = userService.getUsers(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-users/ru/page")
    public ResponseEntity<ResponseResult<Page<UserModel>>> getUsersRu(Pageable pageable) {
        ResponseAll<ResponseResult<Page<UserModel>>> result = userService.getUsersRu(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-groups/uz/page")
    public ResponseEntity<ResponseResult<Page<GroupModel>>> getGroups(Pageable pageable) {
        ResponseAll<ResponseResult<Page<GroupModel>>> result = userService.getGroups(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-groups/ru/page")
    public ResponseEntity<ResponseResult<Page<GroupModel>>> getGroupsRu(Pageable pageable) {
        ResponseAll<ResponseResult<Page<GroupModel>>> result = userService.getGroups(pageable);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-taxes-choose/uz")
    public ResponseEntity<ResponseResult<List<String>>> getTaxesChooseUz() {
        ResponseAll<ResponseResult<List<String>>> result = taxInfoService.getTaxTypesUz();
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-taxes-choose/ru")
    public ResponseEntity<List<String>> getTaxesChooseRu() {
        List<String> taxTypesUz = taxInfoService.getTaxTypesRu();
        return ResponseEntity.ok(taxTypesUz);
    }

    @PostMapping("/add-group-taxes/uz/{chatId}")
    public ResponseEntity<ResponseResult<?>> addGroupTaxesUz(@PathVariable Long chatId, @RequestBody GroupAddTaxRequest groupAddTaxRequest) {
        ResponseAll<ResponseResult<String>> result = taxInfoService.addTaxesGroupServiceUz(chatId, groupAddTaxRequest);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PostMapping("/add-group-taxes/ru/{chatId}")
    public ResponseEntity<ResponseResult<?>> addGroupTaxesRu(@PathVariable Long chatId, @RequestBody GroupAddTaxRequest groupAddTaxRequest) {
        ResponseAll<ResponseResult<String>> result = taxInfoService.addTaxesGroupServiceRu(chatId, groupAddTaxRequest);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-group/uz/{chatId}")
    public ResponseEntity<ResponseResult<GroupModel>> getGroup(@PathVariable Long chatId) {
        ResponseAll<ResponseResult<GroupModel>> result = userService.getGroup(chatId);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-group/ru/{chatId}")
    public ResponseEntity<ResponseResult<GroupModel>> getGroupRu(@PathVariable Long chatId) {
        ResponseAll<ResponseResult<GroupModel>> result = userService.getGroupRu(chatId);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PutMapping("/group-update/{chatId}")
    public ResponseEntity<ResponseResult<?>> updateGroup(@PathVariable Long chatId, @RequestBody GroupUpdateRequest groupUpdateRequest) {
        ResponseAll<ResponseResult<String>> result = userService.updateGroup(chatId, groupUpdateRequest);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @GetMapping("/get-user/{chatId}")
    public ResponseEntity<ResponseResult<UserModel>> getUser(@PathVariable Long chatId) {
        ResponseAll<ResponseResult<UserModel>> result = userService.getUser(chatId);
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

    @PutMapping("/user-update/{chatId}")
    public ResponseEntity<ResponseResult<?>> updateUser(@PathVariable Long chatId, @RequestBody UserUpdateRequest userUpdateRequest) {
        ResponseAll<ResponseResult<String>> result = userService.updateUserActivity(chatId, userUpdateRequest.getIsActive());
        return ResponseEntity.status(result.getStatus()).body(result.getResponse());
    }

}