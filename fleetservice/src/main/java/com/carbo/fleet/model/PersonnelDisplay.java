package com.carbo.fleet.model;

import com.carbo.fleet.dto.PersonnelDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PersonnelDisplay {
    private List<PersonnelDto> personnelDisplayObject;
    private Long totalCount;
}
