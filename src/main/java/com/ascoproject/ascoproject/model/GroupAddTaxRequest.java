package com.ascoproject.ascoproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class GroupAddTaxRequest {
    private List<TaxTypes> taxTypes;
}
