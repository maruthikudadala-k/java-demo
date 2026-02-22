import com.java-demo.utils.ElementUtils;
package com.java_demo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class VehicleManagementStepsPage {

    private WebDriver driver;
    private ElementUtils elementUtils;

    public VehicleManagementStepsPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "addVehicleButton")
    private WebElement addVehicleButton;

    @FindBy(id = "vehicleNumberInput")
    private WebElement vehicleNumberInput;

    @FindBy(id = "vehicleModelInput")
    private WebElement vehicleModelInput;

    @FindBy(id = "vehicleTypeDropdown")
    private WebElement vehicleTypeDropdown;

    @FindBy(id = "saveVehicleButton")
    private WebElement saveVehicleButton;

    @FindBy(id = "vehicleList")
    private WebElement vehicleList;

    @FindBy(id = "searchVehicleInput")
    private WebElement searchVehicleInput;

    @FindBy(xpath = "//button[contains(@id, 'deleteButton')]") // Assuming delete buttons are uniquely identified
    private WebElement deleteButton;









}