package com.ascoproject.ascoproject.model.infoentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InfoEntityUpdateModel {
    private InfoEntityModel uz;
    private InfoEntityModel ru;
}
