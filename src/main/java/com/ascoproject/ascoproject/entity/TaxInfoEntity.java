package com.ascoproject.ascoproject.entity;

import jakarta.persistence.*;
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
@Table(name = "tax_info")
public class TaxInfoEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        @Column(name = "tax_type", length = 129)
    @Size(max = 129)
    private String taxType;
    @Column(name = "report_name", length = 151)
    @Size(max = 151)
    private String reportName;
    @Column(name = "reporting_period", length = 40)
    @Size(max = 40)
    private String reportingPeriod;
    @Column(name = "report_date", length = 10)
    @Size(max = 10)
    private String reportDate;
    @Column(name = "report_comment", length = 366)
    @Size(max = 366)
    private String reportComment;
    @Column(name = "tax_name", length = 207)
    @Size(max = 207)
    private String taxName;
    @Column(name = "payment_period", length = 40)
    @Size(max = 40)
    private String paymentPeriod;
    @Column(name = "tax_date", length = 10)
    @Size(max = 10)
    private String taxDate;
    @Column(name = "tax_comment", length = 102)
    @Size(max = 102)
    private String taxComment;
    @Column(name = "deleted", length = 5)
    private boolean deleted = false;
}
/*
PGPASSWORD=JeORcuAvXNCKVOAZevRPUjRNsbEdWHad psql -h monorail.proxy.rlwy.net -U postgres -p 51988 -d railway

CREATE TABLE tax_info (
    id SERIAL PRIMARY KEY,
    tax_type VARCHAR(129),
    report_name VARCHAR(151),
    reporting_period VARCHAR(40),
    report_date VARCHAR(10),
    report_comment VARCHAR(366),
    tax_name VARCHAR(207),
    payment_period VARCHAR(40),
    tax_date VARCHAR(10),
    tax_comment VARCHAR(102),
    deleted BOOLEAN DEFAULT FALSE
);

*/