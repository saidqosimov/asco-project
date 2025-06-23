package com.ascoproject.ascoproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "info_entity")

public class InfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "type_of_tax", nullable = false, length = 350)
    @NotBlank
    @Size(max = 350)
    private String typeOfTax;
    @Column(name = "full_info", length = 4000)
    @NotBlank
    @Size(max = 4000)
    private String fullInfo;
}

/*
CREATE TABLE info_entity (
    id SERIAL PRIMARY KEY,
    type_of_tax VARCHAR(350) NOT NULL,
    full_info VARCHAR(4000) NOT NULL
);
 */