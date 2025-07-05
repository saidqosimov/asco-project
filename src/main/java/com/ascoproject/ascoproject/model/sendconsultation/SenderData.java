package com.ascoproject.ascoproject.model.sendconsultation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SenderData {
    private String name;
    private String number;
    private String comment;
}
