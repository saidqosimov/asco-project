package com.ascoproject.ascoproject.model.infoentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InfoEntityModel {
    private Long id;
    private String typeOfTax;
    private String fullInfo;
}
