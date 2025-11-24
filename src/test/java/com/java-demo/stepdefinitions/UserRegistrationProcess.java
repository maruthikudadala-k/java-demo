package com.java-demo.stepdefinitions;

import com.java-demo.utils.ElementUtils;
import org.junit.Assert;
import com.java-demo.driverfactory.DriverFactory;
import com.java-demo.pages.UserRegistrationProcessPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class UserRegistrationProcess extends DriverFactory {
UserRegistrationProcessPage userRegistrationProcessPage = new UserRegistrationProcessPage(driver);
@Given("the user is on the Registration Page")
public void the_user_is_on_the_registration_page() {
userRegistrationProcessPage.navigateToRegistrationPage();
}
@When("I enter {string} in the email field")
public void i_enter_in_the_email_field(String email) {
userRegistrationProcessPage.enterEmail(email);
}
@When("I enter {string} in the password field")
public void i_enter_in_the_password_field(String password) {
userRegistrationProcessPage.enterPassword(password);
}
@When("I enter {string} in the confirmation field")
public void i_enter_in_the_confirmation_field(String confirmation) {
userRegistrationProcessPage.enterConfirmation(confirmation);
}
@When("I click the {string} button")
public void i_click_the_button(String button) {
userRegistrationProcessPage.clickRegisterButton();
}
@Then("a success message should appear on the screen")
public void a_success_message_should_appear_on_the_screen() {
userRegistrationProcessPage.isSuccessMessageDisplayed();
}
@Then("a confirmation email should be received in the inbox")
public void a_confirmation_email_should_be_received_in_the_inbox() {
userRegistrationProcessPage.isConfirmationEmailReceived();
}
@Then("the user should be able to log in using {string}")
public void the_user_should_be_able_to_log_in_using(String email) {
userRegistrationProcessPage.canLoginWithEmail(email);
}
@Then("an error message appears indicating password mismatch")
public void an_error_message_appears_indicating_password_mismatch() {
userRegistrationProcessPage.isPasswordMismatchErrorDisplayed();
}
@Then("the account is not created")
public void the_account_is_not_created() {
userRegistrationProcessPage.isAccountCreated();
}
@Then("the user remains on the registration page")
public void the_user_remains_on_the_registration_page() {
userRegistrationProcessPage.isOnRegistrationPage();
}
@Then("the user should see an error message")
public void the_user_should_see_an_error_message() {
userRegistrationProcessPage.isErrorMessageDisplayed();
}
@Then("the user should see the success message displayed on the screen")
public void the_user_should_see_the_success_message_displayed_on_the_screen() {
userRegistrationProcessPage.isSuccessMessageDisplayed();
}
@Then("the user should see the error message indicating that the email is already in use")
public void the_user_should_see_the_error_message_indicating_that_the_email_is_already_in_use() {
userRegistrationProcessPage.isEmailInUseErrorDisplayed();
}
@Then("the user should see the error message indicating that the email format is invalid")
public void the_user_should_see_the_error_message_indicating_that_the_email_format_is_invalid() {
userRegistrationProcessPage.isInvalidEmailFormatErrorDisplayed();
}
@Then("the user should see the error message indicating that the password is required")
public void the_user_should_see_the_error_message_indicating_that_the_password_is_required() {
userRegistrationProcessPage.isPasswordRequiredErrorDisplayed();
}
@Then("the user should see the error message indicating that the confirmation password is required")
public void the_user_should_see_the_error_message_indicating_that_the_confirmation_password_is_required() {
userRegistrationProcessPage.isConfirmationPasswordRequiredErrorDisplayed();
}
@Then("the user should see the error message indicating that the email cannot contain spaces")
public void the_user_should_see_the_error_message_indicating_that_the_email_cannot_contain_spaces() {
userRegistrationProcessPage.isEmailCannotContainSpacesErrorDisplayed();
}
@Then("the user should see the error message indicating that the email must contain more than two characters")
public void the_user_should_see_the_error_message_indicating_that_the_email_must_contain_more_than_two_characters() {
userRegistrationProcessPage.isEmailTooShortErrorDisplayed();
}
@Then("the user should see the error message indicating that the password does not meet the minimum length requirement")
public void the_user_should_see_the_error_message_indicating_that_the_password_does_not_meet_the_minimum_length_requirement() {
userRegistrationProcessPage.isPasswordTooShortErrorDisplayed();
}
@Then("the user should see the error message indicating that the email cannot contain repeated characters")
public void the_user_should_see_the_error_message_indicating_that_the_email_cannot_contain_repeated_characters() {
userRegistrationProcessPage.isEmailCannotContainRepeatedCharactersErrorDisplayed();
}
@Then("the user should see the error message indicating that the email cannot contain consecutive special characters")
public void the_user_should_see_the_error_message_indicating_that_the_email_cannot_contain_consecutive_special_characters() {
userRegistrationProcessPage.isEmailCannotContainConsecutiveSpecialCharactersErrorDisplayed();
}