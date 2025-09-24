package com.carbo.fleet.model;

import com.carbo.fleet.dto.CrewDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CrewDisplayObject {
    private List<CrewDto> crews;
    private long totalCount;
}
