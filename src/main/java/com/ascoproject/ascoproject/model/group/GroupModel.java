package com.ascoproject.ascoproject.model.group;

import com.ascoproject.ascoproject.entity.Languages;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GroupModel {
    private Long id;
    private Long groupId;
    private String groupName;
    private String groupType;
    private String link;
    private Boolean isActive;
    private Languages lang;
    private Boolean threeDaysAgo;
    private Boolean twoDaysAgo;
    private Boolean theDayBefore;
    private Integer countTaxInfo;
    private List<TaxTypes> taxes;
}
