
package com.java-demo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.java-demo.utils.ElementUtils;
import org.junit.Assert;

public class UserRegistrationProcessPage {

    private WebDriver driver;
    private ElementUtils elementUtils;

    @FindBy(id = "emailField")
    private WebElement emailField;

    @FindBy(id = "passwordField")
    private WebElement passwordField;

    @FindBy(id = "confirmationField")
    private WebElement confirmationField;

    @FindBy(id = "registerButton")
    private WebElement registerButton;

    @FindBy(id = "successMessage")
    private WebElement successMessage;

    @FindBy(id = "errorMessage")
    private WebElement errorMessage;

    @FindBy(id = "emailInUseError")
    private WebElement emailInUseError;

    @FindBy(id = "invalidEmailFormatError")
    private WebElement invalidEmailFormatError;

    @FindBy(id = "passwordRequiredError")
    private WebElement passwordRequiredError;

    @FindBy(id = "confirmationPasswordRequiredError")
    private WebElement confirmationPasswordRequiredError;

    @FindBy(id = "emailCannotContainSpacesError")
    private WebElement emailCannotContainSpacesError;

    @FindBy(id = "emailTooShortError")
    private WebElement emailTooShortError;

    @FindBy(id = "passwordTooShortError")
    private WebElement passwordTooShortError;

    @FindBy(id = "emailCannotContainRepeatedCharactersError")
    private WebElement emailCannotContainRepeatedCharactersError;

    @FindBy(id = "emailCannotContainConsecutiveSpecialCharactersError")
    private WebElement emailCannotContainConsecutiveSpecialCharactersError;

    public UserRegistrationProcessPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtils = new ElementUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String email) {
        elementUtils.clearAndSendKeys(emailField, email);
    }

    public void enterPassword(String password) {
        elementUtils.clearAndSendKeys(passwordField, password);
    }

    public void enterConfirmation(String confirmation) {
        elementUtils.clearAndSendKeys(confirmationField, confirmation);
    }

    public void clickRegisterButton() {
        elementUtils.clickElement(registerButton);
    }

    public boolean isSuccessMessageDisplayed() {
        return elementUtils.isElementDisplayed(successMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return elementUtils.isElementDisplayed(errorMessage);
    }

    public boolean isEmailInUseErrorDisplayed() {
        return elementUtils.isElementDisplayed(emailInUseError);
    }

    public boolean isInvalidEmailFormatErrorDisplayed() {
        return elementUtils.isElementDisplayed(invalidEmailFormatError);
    }

    public boolean isPasswordRequiredErrorDisplayed() {
        return elementUtils.isElementDisplayed(passwordRequiredError);
    }

    public boolean isConfirmationPasswordRequiredErrorDisplayed() {
        return elementUtils.isElementDisplayed(confirmationPasswordRequiredError);
    }

    public boolean isEmailCannotContainSpacesErrorDisplayed() {
        return elementUtils.isElementDisplayed(emailCannotContainSpacesError);
    }

    public boolean isEmailTooShortErrorDisplayed() {
        return elementUtils.isElementDisplayed(emailTooShortError);
    }

    public boolean isPasswordTooShortErrorDisplayed() {
        return elementUtils.isElementDisplayed(passwordTooShortError);
    }

    public boolean isEmailCannotContainRepeatedCharactersErrorDisplayed() {
        return elementUtils.isElementDisplayed(emailCannotContainRepeatedCharactersError);
    }

    public boolean isEmailCannotContainConsecutiveSpecialCharactersErrorDisplayed() {
        return elementUtils.isElementDisplayed(emailCannotContainConsecutiveSpecialCharactersError);
    }

    public void isOnRegistrationPage() {
        WebElement registrationPageHeader = driver.findElement(By.xpath("//h1[text()='Registration']"));
        Assert.assertTrue("Registration page is not displayed.", elementUtils.isElementDisplayed(registrationPageHeader));
    }

    public void isAccountCreated() {
        WebElement accountCreationSuccess = driver.findElement(By.id("accountCreationSuccess"));
        String actualMessage = elementUtils.getElementText(accountCreationSuccess);
        String expectedMessage = "Your account has been created successfully.";
        Assert.assertEquals("Account creation message does not match.", expectedMessage, actualMessage);
    }

    public void canLoginWithEmail(String email, String password) {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        elementUtils.clearAndSendKeys(emailField, email);
        elementUtils.clearAndSendKeys(passwordField, password);
        elementUtils.clickElement(loginButton);

        WebElement homepageHeader = driver.findElement(By.xpath("//h1[text()='Welcome']"));
        Assert.assertTrue("Login was unsuccessful.", elementUtils.isElementDisplayed(homepageHeader));
    }

    public void navigateToRegistrationPage() {
        driver.get("http://localhost/registration");
        Assert.assertEquals("Failed to navigate to the registration page.", "http://localhost/registration", driver.getCurrentUrl());
    }

    public void isPasswordMismatchErrorDisplayed() {
        WebElement passwordMismatchError = driver.findElement(By.id("passwordMismatchError"));
        Assert.assertTrue("Password mismatch error is not displayed.", elementUtils.isElementDisplayed(passwordMismatchError));
    }

    public void isConfirmationEmailReceived(String expectedEmailSubject) {
        boolean emailReceived = checkForEmail("test@example.com", expectedEmailSubject); // Placeholder for actual email check
        Assert.assertTrue("Confirmation email has not been received.", emailReceived);
    }
}