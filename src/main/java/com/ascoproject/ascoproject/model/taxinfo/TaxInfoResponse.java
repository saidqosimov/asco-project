package com.ascoproject.ascoproject.model.taxinfo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class TaxInfoResponse {
    private String taxType;
    private List<TaxInfoModel> taxesByType;
}
