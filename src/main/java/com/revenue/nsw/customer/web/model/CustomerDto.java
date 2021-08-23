package com.revenue.nsw.customer.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {

    @JsonProperty("scvId")
    private Long scvId;

    @JsonProperty("personFlag")
    private Boolean personFlag;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("orgName")
    private String organizationName;

    @JsonProperty("totalCustomerRecords")
    private Integer totalCustomerRecords;

    @JsonProperty("amountOwned")
    private Double amountOwed;

    @JsonProperty("impsCustomerRecords")
    private Integer impsCustomerRecords;

    @JsonProperty("fesCustomerRecords")
    private Integer fesCustomerRecords;

    @JsonProperty("dmsCustomerRecords")
    private Integer dmsCustomerRecords;

    @JsonProperty("taxCustomerRecords")
    private Integer taxCustomerRecords;

    @JsonProperty("ata")
    private Boolean ata;

    @JsonProperty("url")
    private String url;
}
