import com.java-demo.utils.ElementUtils;
package com.java-demo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class DriverManagementStepsPage {

    private WebDriver driver;
    private ElementUtils elementUtils;

    @FindBy(id = "addDriverButton") // Unique locator for add driver button
    private WebElement addDriverButton;

    @FindBy(id = "driverNameInput") // Unique locator for driver name input
    private WebElement driverNameInput;

    @FindBy(id = "licenseNumberInput") // Unique locator for driver license number input
    private WebElement licenseNumberInput;

    @FindBy(id = "phoneNumberInput") // Unique locator for driver phone number input
    private WebElement phoneNumberInput;

    @FindBy(id = "saveDriverButton") // Unique locator for save driver button
    private WebElement saveDriverButton;

    @FindBy(id = "driverList") // Unique locator for driver list element
    private WebElement driverList;

    @FindBy(id = "vehicleAssignmentDropdown") // Unique locator for vehicle assignment dropdown
    private WebElement vehicleAssignmentDropdown;

    public DriverManagementStepsPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        PageFactory.initElements(driver, this);
    }









}