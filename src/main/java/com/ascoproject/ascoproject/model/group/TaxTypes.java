package com.ascoproject.ascoproject.model.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaxTypes {
    private Boolean checked;
    private String taxType;
}
