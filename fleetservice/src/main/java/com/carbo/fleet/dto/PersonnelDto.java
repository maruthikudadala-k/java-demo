package com.carbo.fleet.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class PersonnelDto {

    private String id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String secondName;

    @NotEmpty
    private String jobTitle;

    @NotEmpty
    private String employeeId;

    @NotNull
    private Boolean supervisor;

    @NotEmpty
    private String districtId;
    private String districtName;

    private String supervisorId;
    private String supervisorName;

    @NotEmpty
    private String fleetId;
    private String fleetName;

    @NotEmpty
    private String crewId;
    private String crewName;

    private String organizationId;
}
