package com.ascoproject.ascoproject.service;

import com.ascoproject.ascoproject.entity.TaxInfoEntity;
import com.ascoproject.ascoproject.entity.UserEntity;
import com.ascoproject.ascoproject.model.*;
import com.ascoproject.ascoproject.repository.BotUserRepository;
import com.ascoproject.ascoproject.repository.TaxInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final BotUserRepository botUserRepository;
    private final TranslateService translateService;
    private final TelegramService telegramService;
    private final TaxInfoRepository taxInfoRepository;

    @Transactional(readOnly = true)
    public synchronized Page<UserModel> getUsers(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }
        Page<UserEntity> botUsers = botUserRepository.findAll(pageable);
        List<UserModel> userModels = botUsers.getContent().stream()
                .map(userEntity -> UserModel.builder()
                        .id(userEntity.getId())
                        .chatId(userEntity.getChatId())
                        .isActive(userEntity.getIsActive())
                        .step(userEntity.getStep())
                        .lang(userEntity.getLang())
                        .threeDaysAgo(userEntity.getThreeDaysAgo())
                        .twoDaysAgo(userEntity.getTwoDaysAgo())
                        .theDayBefore(userEntity.getTheDayBefore())
                        .countTaxInfo(getUserTaxes(userEntity.getChatId()).size())
                        .taxes(getUserTaxes(userEntity.getChatId()))
                        .build()
                ).toList();
        return new PageImpl<>(userModels, pageable, botUsers.getTotalElements());
    }

    @Transactional(readOnly = true)
    public synchronized Page<UserModel> getUsersRu(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }
        Page<UserEntity> botUsers = botUserRepository.findAll(pageable);
        List<UserModel> userModels = botUsers.getContent().stream()
                .map(userEntity -> UserModel.builder()
                        .id(userEntity.getId())
                        .chatId(userEntity.getChatId())
                        .isActive(userEntity.getIsActive())
                        .step(userEntity.getStep())
                        .lang(userEntity.getLang())
                        .threeDaysAgo(userEntity.getThreeDaysAgo())
                        .twoDaysAgo(userEntity.getTwoDaysAgo())
                        .theDayBefore(userEntity.getTheDayBefore())
                        .countTaxInfo(getUserTaxesRu(userEntity.getChatId()).size())
                        .taxes(getUserTaxesRu(userEntity.getChatId()))
                        .build()
                ).toList();
        return new PageImpl<>(userModels, pageable, botUsers.getTotalElements());
    }

    public synchronized List<String> getUserTaxes(Long chatId) {
        return Optional.ofNullable(botUserRepository.findByChatId(chatId))
                .map(UserEntity::getTaxInfo)
                .orElse(Collections.emptyList())
                .stream()
                .map(TaxInfoEntity::getTaxType)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }


    public synchronized List<String> getUserTaxesRu(Long chatId) {
        return Optional.ofNullable(botUserRepository.findByChatId(chatId))
                .map(UserEntity::getTaxInfo)
                .orElse(Collections.emptyList())
                .stream()
                .map(TaxInfoEntity::getTaxType)
                .filter(Objects::nonNull)
                .distinct()
                .map(translateService::translate) // ✅ ruscha tarjima qilish
                .filter(Objects::nonNull)
                .toList();
    }


    public synchronized void deleteTaxInfo(Long id) {
        botUserRepository.deleteTaxInfoLinks(id);
    }

    public synchronized void deleteAllTaxInfo() {
        botUserRepository.deleteAll();
    }

    public synchronized void updateUserActivity(Long chatId, boolean isActive) {
        UserEntity user = botUserRepository.findByChatId(chatId);
        user.setIsActive(isActive);
        botUserRepository.save(user);
    }
    @Transactional(readOnly = true)
    public synchronized Page<GroupModel> getGroups(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }

        Page<UserEntity> botUsers = botUserRepository.findAll(pageable);

        List<GroupModel> groupModels = botUsers.getContent().stream()
                .filter(user -> String.valueOf(user.getChatId()).startsWith("-100")) // Guruhlar
                .map(userEntity -> {
                    TelegramChat chatInfo = telegramService.getChatInfo(userEntity.getChatId());
                    return GroupModel.builder()
                            .id(userEntity.getId())
                            .groupId(userEntity.getChatId())
                            .groupName(chatInfo != null ? chatInfo.getTitle() : null)
                            .groupType(chatInfo != null ? chatInfo.getType() : null)
                            .link(chatInfo != null ? chatInfo.getInviteLink() : null)
                            .isActive(userEntity.getIsActive())
                            .lang(userEntity.getLang())
                            .threeDaysAgo(userEntity.getThreeDaysAgo())
                            .twoDaysAgo(userEntity.getTwoDaysAgo())
                            .theDayBefore(userEntity.getTheDayBefore())
                            .countTaxInfo(getUserTaxes(userEntity.getChatId()).size())
                            .build();
                })
                .toList();
        return new PageImpl<>(groupModels, pageable, groupModels.size());
    }
/*
    @Transactional(readOnly = true)
    public synchronized Page<GroupModel> getGroupsRu(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("id").ascending()
            );
        }

        Page<UserEntity> botUsers = botUserRepository.findAll(pageable);

        List<GroupModel> groupModels = botUsers.getContent().stream()
                .filter(user -> String.valueOf(user.getChatId()).startsWith("-100")) // Guruhlar
                .map(userEntity -> {
                    TelegramChat chatInfo = telegramService.getChatInfo(userEntity.getChatId());
                    return GroupModel.builder()
                            .id(userEntity.getId())
                            .groupId(userEntity.getChatId())
                            .groupName(chatInfo != null ? chatInfo.getTitle() : null)
                            .groupType(chatInfo != null ? chatInfo.getType() : null)
                            .link(chatInfo != null ? chatInfo.getInviteLink() : null)
                            .isActive(userEntity.getIsActive())
                            .lang(userEntity.getLang())
                            .threeDaysAgo(userEntity.getThreeDaysAgo())
                            .twoDaysAgo(userEntity.getTwoDaysAgo())
                            .theDayBefore(userEntity.getTheDayBefore())
                            .countTaxInfo(getUserTaxes(userEntity.getChatId()).size())
                            .taxes(getUserTaxesRu(userEntity.getChatId()))
                            .build();
                })
                .toList();
        return new PageImpl<>(groupModels, pageable, groupModels.size());
    }*/
    public synchronized void addTaxesGroup(Long chatId, List<TaxInfoEntity> taxInfoEntities) {
        UserEntity group = botUserRepository.findByChatId(chatId);
        group.setTaxInfo(taxInfoEntities);
        botUserRepository.save(group);
    }

    public void updateGroup(Long chatId, GroupUpdateRequest groupUpdateRequest) {
        UserEntity group = botUserRepository.findByChatId(chatId);
        group.setIsActive(groupUpdateRequest.getIsActive());
        group.setLang(groupUpdateRequest.getLang());
        group.setThreeDaysAgo(groupUpdateRequest.getThreeDaysAgo());
        group.setTwoDaysAgo(groupUpdateRequest.getTwoDaysAgo());
        group.setTheDayBefore(groupUpdateRequest.getTheDayBefore());
        botUserRepository.save(group);    }

    public GroupModel getGroup(Long chatId) {
        UserEntity group = botUserRepository.findByChatId(chatId);
        TelegramChat chatInfo = telegramService.getChatInfo(chatId);
        return GroupModel.builder()
                .id(group.getId())
                .groupId(group.getChatId())
                .groupName(chatInfo != null ? chatInfo.getTitle() : null)
                .groupType(chatInfo != null ? chatInfo.getType() : null)
                .link(chatInfo != null ? chatInfo.getInviteLink() : null)
                .isActive(group.getIsActive())
                .lang(group.getLang())
                .threeDaysAgo(group.getThreeDaysAgo())
                .twoDaysAgo(group.getTwoDaysAgo())
                .theDayBefore(group.getTheDayBefore())
                .countTaxInfo(getUserTaxes(group.getChatId()).size())
                .taxes(getGroupTaxTypesUz(chatId))
                .build();
    }    public GroupModel getGroupRu(Long chatId) {
        UserEntity group = botUserRepository.findByChatId(chatId);
        TelegramChat chatInfo = telegramService.getChatInfo(chatId);
        return GroupModel.builder()
                .id(group.getId())
                .groupId(group.getChatId())
                .groupName(chatInfo != null ? chatInfo.getTitle() : null)
                .groupType(chatInfo != null ? chatInfo.getType() : null)
                .link(chatInfo != null ? chatInfo.getInviteLink() : null)
                .isActive(group.getIsActive())
                .lang(group.getLang())
                .threeDaysAgo(group.getThreeDaysAgo())
                .twoDaysAgo(group.getTwoDaysAgo())
                .theDayBefore(group.getTheDayBefore())
                .countTaxInfo(getUserTaxes(group.getChatId()).size())
                .taxes(getGroupTaxTypesRu(chatId))
                .build();
    }
    public List<TaxTypes> getGroupTaxTypesUz(Long chatId) {
        // Foydalanuvchining taxType'lari
        List<String> userTaxTypes = getUserTaxes(chatId);

        // Barcha mavjud taxType'lar (tartib saqlanadi)
        List<String> allTaxTypes = taxInfoRepository.findAllTaxInfo().stream()
                .map(TaxInfoEntity::getTaxType)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new)) // tartibni saqlaydi
                .stream()
                .toList();

        // Har bir taxType uchun foydalanuvchida bor-yo‘qligini tekshiramiz
        return allTaxTypes.stream()
                .map(taxType -> {
                    TaxTypes t = new TaxTypes();
                    t.setTaxType(taxType);
                    t.setChecked(userTaxTypes.contains(taxType));
                    return t;
                })
                .toList();
    }
    public List<TaxTypes> getGroupTaxTypesRu(Long chatId) {
        // Foydalanuvchining taxType'lari
        List<String> userTaxTypes = getUserTaxesRu(chatId);

        // Barcha mavjud taxType'lar (tartib saqlanadi)
        List<String> allTaxTypes = taxInfoRepository.findAllTaxInfo().stream()
                .map(TaxInfoEntity::getTaxType)
                .filter(Objects::nonNull)
                .map(translateService::translate) // 🔁 ruscha tarjima
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new)) // tartibni saqlaydi
                .stream()
                .toList();

        // Har bir taxType uchun foydalanuvchida bor-yo‘qligini tekshiramiz
        return allTaxTypes.stream()
                .map(taxType -> {
                    TaxTypes t = new TaxTypes();
                    t.setTaxType(taxType);
                    t.setChecked(userTaxTypes.contains(taxType));
                    return t;
                })
                .toList();
    }

    public UserModel getUser(Long chatId){
        UserEntity user = botUserRepository.findByChatId(chatId);
        return UserModel.builder()
                .id(user.getId())
                .chatId(user.getChatId())
                .isActive(user.getIsActive())
                .lang(user.getLang())
                .step(user.getStep())
                .threeDaysAgo(user.getThreeDaysAgo())
                .twoDaysAgo(user.getTwoDaysAgo())
                .theDayBefore(user.getTheDayBefore())
                .countTaxInfo(getUserTaxes(chatId).size())
                .build();
    }
}
