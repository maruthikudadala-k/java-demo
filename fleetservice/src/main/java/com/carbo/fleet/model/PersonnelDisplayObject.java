package com.carbo.fleet.model;

import lombok.Data;

@Data
public class PersonnelDisplayObject {
    private String id;
    private String firstName;
    private String secondName;
    private String jobTitle;
    private String employeeId;
    private Boolean supervisor;
    private String districtName;
    private String supervisorName;
    private String fleetName;
    private String crewName;
    private String organizationId;
}
