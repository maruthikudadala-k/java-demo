package com.java_demo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.utils.ElementUtils;
import org.testng.Assert;

public class VehicleManagementStepsPage {

    private WebDriver driver;
    private ElementUtils elementUtils;

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

    @FindBy(xpath = "//div[@class='success-message']")
    private WebElement successMessage;

    @FindBy(id = "vehicleSearchInput")
    private WebElement vehicleSearchInput;

    @FindBy(xpath = "//button[contains(@class, 'delete-button')]")
    private WebElement deleteButton;

    public VehicleManagementStepsPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        PageFactory.initElements(driver, this);
    }









}