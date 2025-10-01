package com.fleetservice.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class DriverManagementSteps {
    
    @Given("user is on the driver management page")
    public void userIsOnDriverManagementPage() {
        // Implementation
    }
    
    @When("user clicks on add driver button")
    public void userClicksAddDriverButton() {
        // Implementation
    }
    
    @When("user enters driver name {string}")
    public void userEntersDriverName(String name) {
        // Implementation
    }
    
    @When("user enters driver license number {string}")
    public void userEntersLicenseNumber(String license) {
        // Implementation
    }
    
    @When("user enters driver phone number {string}")
    public void userEntersPhoneNumber(String phone) {
        // Implementation
    }
    
    @When("user clicks on save driver button")
    public void userClicksSaveDriverButton() {
        // Implementation
    }
    
    @Then("driver should be added successfully")
    public void driverShouldBeAddedSuccessfully() {
        // Implementation
    }
    
    @Then("user should see driver {string} in the list")
    public void userShouldSeeDriverInList(String name) {
        // Implementation
    }
    
    @When("user assigns vehicle {string} to driver {string}")
    public void userAssignsVehicleToDriver(String vehicle, String driver) {
        // Implementation
    }
    
    @Then("vehicle {string} should be assigned to driver {string}")
    public void vehicleShouldBeAssignedToDriver(String vehicle, String driver) {
        // Implementation
    }
}
