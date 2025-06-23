package com.ascoproject.ascoproject.controller;

import com.ascoproject.ascoproject.entity.InfoEntity;
import com.ascoproject.ascoproject.model.*;
import com.ascoproject.ascoproject.service.FileImportService;
import com.ascoproject.ascoproject.service.InfoEntityService;
import com.ascoproject.ascoproject.service.TaxInfoService;
import com.ascoproject.ascoproject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bot")
@RequiredArgsConstructor
public class BotController {
    private final TaxInfoService taxInfoService;
    private final FileImportService fileImportService;
    private final InfoEntityService infoEntityService;
    private final UserService userService;

    @GetMapping("/tax-report-list/uz/page")
    public ResponseEntity<Page<TaxInfoResponse>> getTaxReportListPagingUz(Pageable pageable) {
        Page<TaxInfoResponse> result = taxInfoService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tax-report-list/ru/page")
    public ResponseEntity<Page<TaxInfoResponse>> getTaxReportListPagingRu(Pageable pageable) {
        Page<TaxInfoResponse> result = taxInfoService.findAllRu(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/info-entity-list/uz/page")
    public ResponseEntity<Page<InfoEntity>> getInfoEntityListPagingUz(Pageable pageable) {
        Page<InfoEntity> result = infoEntityService.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/info-entity-list/ru/page")
    public ResponseEntity<Page<InfoEntity>> getInfoEntityListPagingRu(Pageable pageable) {
        Page<InfoEntity> result = infoEntityService.findAllRu(pageable);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/tax-report-update/{id}")
    public ResponseEntity<?> updateTaxReport(@PathVariable Long id, @RequestBody TaxInfoUpdateModel taxInfoUpdateModel) {
        taxInfoService.updateTaxById(id, taxInfoUpdateModel.getUz(), taxInfoUpdateModel.getRu());
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/tax-report-delete/{id}")
    public ResponseEntity<?> deleteTaxReport(@PathVariable Long id) {
        taxInfoService.deleteTaxById(id);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/info_entity-update/{id}")
    public ResponseEntity<?> updateInfoEntity(@PathVariable Long id, @RequestBody InfoEntityUpdateModel infoEntityUpdateModel) {
        infoEntityService.updateInfoById(id, infoEntityUpdateModel.getUz(), infoEntityUpdateModel.getRu());
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/info-entity-delete/{id}")
    public ResponseEntity<?> deleteInfoEntity(@PathVariable Long id) {
        infoEntityService.deleteInfoEntityById(id);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/add-info_entity")
    public ResponseEntity<?> addInfoEntity(@RequestBody InfoEntityUpdateModel infoEntityUpdateModel) {
        infoEntityService.addInfoEntity(infoEntityUpdateModel.getUz(), infoEntityUpdateModel.getRu());
        return ResponseEntity.ok("success");
    }

    @PostMapping("/add-tax-report")
    public ResponseEntity<?> addTaxReport(@RequestBody TaxInfoUpdateModel taxInfoUpdateModel) {
        taxInfoService.addTaxReport(taxInfoUpdateModel.getUz(), taxInfoUpdateModel.getRu());
        return ResponseEntity.ok("success");
    }

    @GetMapping("/info-list/uz")
    public ResponseEntity<String> getInfoListUz() {
        return ResponseEntity.ok("Info List");
    }

    @GetMapping("/info-list/ru")
    public ResponseEntity<String> getInfoListRu() {
        return ResponseEntity.ok("Info List");
    }

    @PostMapping("/import-tax-info-csv")
    public ResponseEntity<?> uploadTaxInfoCsv(@RequestParam("file") MultipartFile file) throws Exception {
        fileImportService.importCsv(file);
        return ResponseEntity.ok("Successfully imported file");
    }

    @GetMapping("/export-tax-info")
    public void exportTaxInfoToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"tax_info.csv\"");
        fileImportService.exportTaxReportCsv(response);
    }

    @PostMapping("/import-info-entity-csv")
    public ResponseEntity<?> uploadInfoEntityCsv(@RequestParam("file") MultipartFile file) throws Exception {
        fileImportService.importInfoEntityCsv(file);
        return ResponseEntity.ok("Successfully imported file");
    }

    @GetMapping("/export-info-entity")
    public void exportInfoEntityToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"info_entity.csv\"");
        fileImportService.exportInfoEntityCsv(response);
    }

    @GetMapping("/get-users/uz/page")
    public ResponseEntity<Page<UserModel>> getUsers(Pageable pageable) {
        Page<UserModel> users = userService.getUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-users/ru/page")
    public ResponseEntity<Page<UserModel>> getUsersRu(Pageable pageable) {
        Page<UserModel> users = userService.getUsersRu(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-groups/uz/page")
    public ResponseEntity<Page<GroupModel>> getGroups(Pageable pageable) {
        Page<GroupModel> groups = userService.getGroups(pageable);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/get-groups/ru/page")
    public ResponseEntity<Page<GroupModel>> getGroupsRu(Pageable pageable) {
        Page<GroupModel> groups = userService.getGroups(pageable);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/get-taxes-choose/uz")
    public ResponseEntity<List<String>> getTaxesChooseUz() {
        List<String> taxTypesUz = taxInfoService.getTaxTypesUz();
        return ResponseEntity.ok(taxTypesUz);
    }

    @GetMapping("/get-taxes-choose/ru")
    public ResponseEntity<List<String>> getTaxesChooseRu() {
        List<String> taxTypesUz = taxInfoService.getTaxTypesRu();
        return ResponseEntity.ok(taxTypesUz);
    }

    @PostMapping("/add-group-taxes/uz/{chatId}")
    public ResponseEntity<?> addGroupTaxesUz(@PathVariable Long chatId, @RequestBody GroupAddTaxRequest groupAddTaxRequest) {
        taxInfoService.addTaxesGroupServiceUz(chatId, groupAddTaxRequest);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/add-group-taxes/ru/{chatId}")
    public ResponseEntity<?> addGroupTaxesRu(@PathVariable Long chatId, @RequestBody GroupAddTaxRequest groupAddTaxRequest) {
        taxInfoService.addTaxesGroupServiceRu(chatId, groupAddTaxRequest);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/get-group/uz/{chatId}")
    public ResponseEntity<GroupModel> getGroup(@PathVariable Long chatId) {
        GroupModel group = userService.getGroup(chatId);
        return ResponseEntity.ok(group);
    }
    @GetMapping("/get-group/ru/{chatId}")
    public ResponseEntity<GroupModel> getGroupRu(@PathVariable Long chatId) {
        GroupModel group = userService.getGroupRu(chatId);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/group-update/{chatId}")
    public ResponseEntity<?> updateGroup(@PathVariable Long chatId, @RequestBody GroupUpdateRequest groupUpdateRequest) {
        userService.updateGroup(chatId, groupUpdateRequest);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/get-user/{chatId}")
    public ResponseEntity<UserModel> getUser(@PathVariable Long chatId) {
        UserModel user = userService.getUser(chatId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user-update/{chatId}")
    public ResponseEntity<?> updateUser(@PathVariable Long chatId, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUserActivity(chatId, userUpdateRequest.getIsActive());
        return ResponseEntity.ok("success");
    }

}