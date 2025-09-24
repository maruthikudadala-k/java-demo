package com.carbo.fleet.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TotalCountObject {
    private Long totalCount;

    public TotalCountObject(Long totalCount) {
        this.totalCount = totalCount;
    }

    public TotalCountObject() {
    }
}
