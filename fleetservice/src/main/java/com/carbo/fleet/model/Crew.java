package com.carbo.fleet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Data
@Document(collection = "crews")
@JsonInclude(JsonInclude.Include.NON_NULL)
@CompoundIndex(def = "{'organizationId': 1, 'fleetId':1, 'name': 1}", name = "organizationId_fleetId_name_index", unique = true)
public class Crew {
    @Id
    private String id;

    @Field("name")
    @Size(max = 100, message = "name can not be more the 100 characters")
    String name;

    @Field("jobPattern")
    private String jobPattern;

    @Field("shiftStart")
    private String shiftStart;

    @Field("startDate")
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;

    @Field("organizationId")
    private String organizationId;

    @Field("fleetId")
    private String fleetId;

    @Field("created")
    private Long created = new Date().getTime();

    @Field("modified")
    private Long modified  = new Date().getTime();
}
