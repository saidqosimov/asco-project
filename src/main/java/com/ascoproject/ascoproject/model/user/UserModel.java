package com.ascoproject.ascoproject.model.user;

import com.ascoproject.ascoproject.entity.Languages;
import com.ascoproject.ascoproject.entity.UserStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserModel {
    private Long id;
    private Long chatId;
    private Boolean isActive;
    private UserStep step;
    private Languages lang;
    private Boolean threeDaysAgo;
    private Boolean twoDaysAgo;
    private Boolean theDayBefore;
    private Integer countTaxInfo;
    private List<String> taxes;
}