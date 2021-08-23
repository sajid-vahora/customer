package com.revenue.nsw.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value = "customer")
public class Customer {

    @Id
    private Long id;

    @Column("scv_id")
    private Long scvId;

    @Column("person_flag")
    private Boolean personFlag;

    @Column("first_name")
    private String firstName;

    @Column("org_name")
    private String organizationName;

    @Column("total_cust_record")
    private Integer totalCustomerRecords;

    @Column("amount_owed")
    private Double amountOwed;

    @Column("imps_cust_records")
    private Integer impsCustomerRecords;

    @Column("fes_cust_records")
    private Integer fesCustomerRecords;

    @Column("dms_cust_records")
    private Integer dmsCustomerRecords;

    @Column("tax_cust_records")
    private Integer taxCustomerRecords;

    @Column("ata")
    private Boolean ata;

    @Column("url")
    private String url;
}
