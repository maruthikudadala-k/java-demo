package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.PersonnelDto;
import com.carbo.fleet.model.PersonnelDisplay;
import com.carbo.fleet.services.PersonnelService;
import com.carbo.fleet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;

@RestController
@RequestMapping("v1/personnel")
public class PersonnelController {

    @Autowired
    PersonnelService personnelService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonnelDisplay getAllPersonnel(HttpServletRequest request,
                                                                    @RequestParam(name = "offSet", defaultValue = "0", required = false) int offSet,
                                                                    @RequestParam(name = "limit", defaultValue = "10", required = false) int limit) {
        String organizationId = getOrganizationId(request);
        return personnelService.findAll(organizationId, offSet, limit);
    }

    @GetMapping("/{id}")
    public PersonnelDto getPersonnel(HttpServletRequest request, @PathVariable String id) {
        return personnelService.findById(id);
    }

    @PostMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPersonnel(HttpServletRequest request, @Valid @RequestBody PersonnelDto personnelDto) {
        String organizationId = getOrganizationId(request);
        personnelDto.setOrganizationId(organizationId);
        Boolean status = personnelService.savePersonnel(personnelDto);
        Map<String, String> message = new HashMap<>();
        if (status) {
            message.put("successMessage", Constants.PERSONNEL_CREATED);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } else {
            message.put("errorMessage", Constants.PERSONNEL_ALREADY_EXISTS);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
    }

    @PutMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonnelDto updatePersonnel(HttpServletRequest request, @RequestBody PersonnelDto personnelDto) {
        String organizationId = getOrganizationId(request);
        personnelDto.setOrganizationId(organizationId);
        personnelService.updatePersonnel(personnelDto);
        return personnelService.findById(personnelDto.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePersonnel(@PathVariable String id) {
        personnelService.deletePersonnel(id);
    }

    @GetMapping("/getByField")
    public PersonnelDisplay getAllPersonnelByFilter(HttpServletRequest request,
                                                              @RequestParam(name = "offSet", defaultValue = "0", required = false) int offSet,
                                                              @RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
                                                              @RequestParam(value = "personnelName", defaultValue = "", required = false) String personnelName,
                                                              @RequestParam(value = "districtId", defaultValue = "", required = false) String districtId,
                                                              @RequestParam(value = "jobTitle", defaultValue = "", required = false) String jobTitle) {
        String organizationId = getOrganizationId(request);
        return personnelService.findbyValue(organizationId, personnelName, districtId, jobTitle, offSet, limit);
    }
}
