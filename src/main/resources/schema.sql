CREATE TABLE if NOT EXISTS customer
(
    id                  INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    scv_id              int,
    person_flag         boolean,
    first_name          varchar(255),
    org_name            varchar (255),
    total_cust_record   int,
    amount_owed         double,
    imps_cust_records   int,
    fes_cust_records    int,
    dms_cust_records    int,
    tax_cust_records    int,
    ata                 boolean,
    url                 varchar(255)
);