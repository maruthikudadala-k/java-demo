package com.carbo.fleet.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Builder
@Data
public class CrewDto {
    public String id;

    @NotEmpty
    public String name;

    @NotEmpty
    public String jobPattern;

    @NotEmpty
    public String shiftStart;

    @NotEmpty
    public String startDate;

    public String organizationId;

    public String fleetName;

    public String districtName;

    @NotEmpty
    public String fleetId;

    public Long totalCount;
}


