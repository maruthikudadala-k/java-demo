package com.carbo.fleet.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@Document(collection = "personnel")
@CompoundIndex(def = "{'organizationId': 1, 'employeeId':1}", name = "organization_employee_index", unique = true)
public class Personnel {
    @Id
    private String id;

    @Field("firstName")
    private String firstName;

    @Field("secondName")
    private String secondName;

    @Field("jobTitle")
    private String jobTitle;

    @Field("employeeId")
    private String employeeId;

    @Field("districtId")
    private String districtId;

    @Field("supervisor")
    private Boolean supervisor;

    @Field("supervisorId")
    private String supervisorId;

    @Field("fleetId")
    private String fleetId;

    @Field("crewId")
    private String crewId;

    @Field("organizationId")
    private String organizationId;
}
