package com.ascoproject.ascoproject.model.group;

import com.ascoproject.ascoproject.entity.Languages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GroupUpdateRequest {
    private Boolean isActive;
    private Languages lang;
    private Boolean threeDaysAgo;
    private Boolean twoDaysAgo;
    private Boolean theDayBefore;
}

