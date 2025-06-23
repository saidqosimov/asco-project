package com.ascoproject.ascoproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "translate")
public class TranslateEntity implements Serializable {
    @Id
    @Size(max = 4000)
    private String uz;
    @Size(max = 4200)
    private String ru;
    
}
