package com.ascoproject.ascoproject.model.taxinfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaxInfoUpdateModel {
    private TaxInfoModel uz;
    private TaxInfoModel ru;
}
