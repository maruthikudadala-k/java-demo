package com.carbo.fleet.controllers;

import com.carbo.fleet.dto.CrewDto;
import com.carbo.fleet.model.Crew;
import com.carbo.fleet.model.CrewDisplayObject;
import com.carbo.fleet.services.CrewService;
import com.carbo.fleet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.carbo.fleet.utils.ControllerUtil.getOrganizationId;

@RestController
@RequestMapping("/v1/crew")
public class CrewController {

    CrewService crewService;

    @Autowired
    public CrewController(CrewService crewService) {
        this.crewService = crewService;
    }

    @GetMapping("/")
    public CrewDisplayObject getAllCrew(HttpServletRequest request,
                                                    @RequestParam(name = "offSet", defaultValue = "0", required = false) int offSet,
                                                    @RequestParam(name = "limit", defaultValue = "10", required = false) int limit) {
        String organizationId = getOrganizationId(request);
        return crewService.findAll(organizationId, offSet, limit);

    }

    @GetMapping("/{id}")
    public CrewDto getCrew(HttpServletRequest request, @PathVariable String id) {
        return crewService.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createCrew(HttpServletRequest request, @Valid @RequestBody CrewDto crew) {
        String organizationId = getOrganizationId(request);
        crew.setOrganizationId(organizationId);
        Crew crewNew = crewService.saveCrew(crew);
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", Constants.CREW_ALREADY_EXISTS);
        return crewNew != null ? ResponseEntity.status(HttpStatus.CREATED).body(crewNew)
                : ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @PutMapping("/")
    public CrewDto updatePersonnel(HttpServletRequest request, @RequestBody CrewDto crew) {
        String organizationId = getOrganizationId(request);
        crew.setOrganizationId(organizationId);
        Boolean crewStatus = crewService.updateCrew(crew);
        Map<String, String> message = new HashMap<>();
        return crewService.findById(crew.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCrew(@PathVariable String id) {
        crewService.deleteCrew(id);
    }

    @GetMapping("/getByFleet")
    public CrewDisplayObject getAllCrewByFleet(HttpServletRequest request,
                                                    @RequestParam(name = "offSet", defaultValue = "0", required = false) int offSet,
                                                    @RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
                                                    @RequestParam("fleetName") String fleetName) {
        String organizationId = getOrganizationId(request);
        return crewService.findAllByFleet(organizationId, fleetName, offSet, limit);
    }
}