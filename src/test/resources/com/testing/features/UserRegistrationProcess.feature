
@feature_user_registration
Feature: UserRegistrationProcess

  Background: 
    Given the user is on the Registration Page

  @unique-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters a unique email address "<email>"
    And the user enters a valid password "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user should be redirected to the Login Page

    Examples:
      | email              | password             | confirmation       |
      | krishna@gmail.com  | ValidPassword123     | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms "<confirm_password>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see a success message displayed on the screen
    And the user should be redirected to the login page

    Examples:
      | email               | password      | confirm_password |
      | krishna@gmail.com   | Password123   | Password123      |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see the success message displayed on the screen

    Examples:
      | email                | password                   | confirmation               |
      | krishna@gmail.com    | <long_password>           | <long_password>            |

  @successful-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password as "<confirmation_password>"
    And the user clicks the "Register" button
    Then the user should receive a confirmation email in the inbox

    Examples:
      | email               | password              | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!       |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters a password that includes letters, numbers, and special characters
    And the user confirms the password in the confirmation field
    And the user clicks the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                    | password      | confirm_password |
      | uniqueuser@example.com   | Password123!  | Password123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | email               | password         | confirmation     |
      | krishna@gmail.com   | Password123!     | Password123!     |

  @valid-email
  Scenario Outline: Validate Email Format
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    Then the email field accepts the unique email format

    Examples:
      | email               |
      | krishna@gmail.com   |

  @valid-password
  Scenario Outline: Validate Password Complexity
    Given the user is on the Registration Page
    When I enter "<password>" in the password field
    Then the password field accepts the valid complex password

    Examples:
      | password         |
      | Password123!     |

  @valid-confirmation
  Scenario Outline: Validate Password Confirmation
    Given the user is on the Registration Page
    When I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    Then the confirmation field matches the password

    Examples:
      | password         | confirmation     |
      | Password123!     | Password123!     |

  @success-message
  Scenario Outline: Verify Success Message
    Given the user is on the Registration Page
    When I click the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | 
      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system creates the account and redirects to a success page
    And the user checks the inbox for a confirmation email

    Examples:
      | email              | password              | confirmation         |
      | krishna@gmail.com  | ValidPassword123!     | ValidPassword123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @validate_fields_accessibility
  Scenario Outline: User Registration Process
    When I navigate to the registration page
    Then the registration page loads successfully
    And the email field is visible and accessible
    And the password field is visible and accessible
    And the confirmation password field is visible and accessible
    When I enter a unique email address "<email>"
    And I enter a valid password "<password>"
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email               | password           |
      | <email>            | <password>         |
      | krishna@gmail.com   | ValidPassword123   |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                | password           | confirmation        |
      | krishna@gmail.com    | ValidPassword123   | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see a success message displayed on the screen
    And the user should be redirected to the login page

    Examples:
      | email               | password            | confirmation       |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email               | password            | confirmation_password    |
      | krishna@gmail.com   | SecurePassword123   | SecurePassword123        |

  @validate_fields_accessibility
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user uses the Tab key to focus on the email field
    And the user enters "<email>" in the email field
    And the user uses the Tab key to focus on the password field
    And the user enters "<password>" in the password field
    And the user uses the Tab key to focus on the confirmation password field
    And the user enters "<confirmation_password>" in the confirmation password field
    Then the registration process should be successful

    Examples:
      | email               | password             | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123     | ValidPassword123        |

@validate_labels
Scenario Outline: User Registration Process - Validate Labels
  Given the user is on the Registration Page
  Then the email field has a visible label
  And the password field has a visible label
  And the confirmation password field has a visible label
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  Then the registration should be successful

  Examples:
    | email               | password           |
    | krishna@gmail.com   | ValidPassword123   |

@enter_unique_email
Scenario Outline: User Registration Process - Enter Unique Email
  Given the user is on the Registration Page
  When I enter "<email>" in the email field
  Then the email field accepts the unique email format

  Examples:
    | email               |
    | krishna@gmail.com   |

@enter_valid_password
Scenario Outline: User Registration Process - Enter Valid Password
  Given the user is on the Registration Page
  When I enter "<password>" in the password field
  Then the password field accepts the valid password

  Examples:
    | password           |
    | ValidPassword123   |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user taps on the email field to focus
    And the user enters "<email>"
    And the user taps on the password field to focus
    And the user enters "<password>"
    And the user taps on the confirmation password field to focus
    Then the registration page should be accessible and usable on touch devices

    Examples:
      | email               | password            |
      | krishna@gmail.com   | ValidPassword123    |

  @feature_user_registration
  @enabled_fields_validation
  Scenario Outline: User Registration Process
    When I navigate to the registration page
    Then the registration page loads successfully
    And the email field is enabled and can be interacted with
    And the password field is enabled and can be interacted with
    And the confirmation password field is enabled and can be interacted with
    When I enter a unique email address "<email>"
    And I enter a valid password "<password>"
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email               | password               |
      | krishna@gmail.com   | ValidPassword123       |

@accessible-fields
Scenario Outline: User Registration Process
  When I navigate to the registration page using a screen reader
  Then the screen reader announces the email field correctly
  And the screen reader announces the password field correctly
  And the screen reader announces the confirmation password field correctly
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  Then the email field accepts the unique email format
  And the password field accepts the valid password

  Examples:
    | email                  | password              | confirmation_password    |
    | krishna@gmail.com      | ValidPassword123      | ValidPassword123         |

  Scenario Outline: Validate password mismatch error message
    Given the user enters a unique email address "<email>"
    And the user enters a valid password "<password>"
    And the user enters a different password "<confirmation_password>"
    When the user clicks the "Register" button
    Then an error message is displayed indicating the passwords do not match
    And the confirmation password field is highlighted to indicate the error

    Examples:
      | email               | password            | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123    | DifferentPassword456    |

  Scenario Outline: User Registration Process
    When the user enters a unique email address "<email>"
    And the user enters a valid password "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then a loading indicator is displayed while the registration request is processed
    And the loading indicator disappears after the registration is complete

  Examples:
    | email               | password              | confirmation         |
    | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @successful_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the "Register" button
    Then the email field should be cleared
    And the password field should be cleared

    Examples:
      | email               | password           | confirmation       |
      | krishna@gmail.com   | ValidPassword123   | ValidPassword123    |

  @mobile-accessibility
  Scenario Outline: User Registration Process on Mobile
    Given the user opens the registration page on a mobile device
    Then the registration page loads successfully on mobile
    Then the email field is visible and accessible on mobile
    Then the password field is visible and accessible on mobile
    Then the confirmation password field is visible and accessible on mobile
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email              | password           |
      | krishna@gmail.com  | ValidPassword123   |

@registration-error
Scenario Outline: User Registration Process with Already Registered Email
  Given the user is on the registration page
  When I enter "<email>"
  And I enter "<password>"
  And I confirm the password "<confirmation>"
  And I click the "Register" button
  Then an error message should be displayed indicating the email is already in use
  And the email field should be highlighted to indicate the error

  Examples:
    | email               | password            | confirmation        |
    | krishna@gmail.com   | ValidPassword123    | ValidPassword123     |

  @successful-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message indicating the account has been created

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @redirect-to-login
  Scenario Outline: User Redirect After Registration
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message indicating the account has been created
    And the user should be redirected to the login page

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @tooltip-validation
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I hover over the email field
    Then the tooltip should provide information about valid email format
    When I hover over the password field
    Then the tooltip should provide information about password requirements
    When I hover over the confirmation password field
    Then the tooltip should provide information about matching passwords
    When I click the Register button without filling in the fields
    Then the system displays error messages for empty fields
    And the tooltips disappear after the user interacts with the fields

    Examples:
      | action |
      |       |

  @invalid_email_retain
  Scenario Outline: User Registration with Invalid Email Retaining Values
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see an error message for the invalid email
    And the email field should retain the value "<email>"
    And the password field should retain the value "<password>"

    Examples:
      | email    | password           | confirmation       |
      | abc@     | ValidPassword123   | ValidPassword123    |

  @invalid-email
  Scenario Outline: Validate Email Format Error Message
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email    | password              | confirmation         |
      | abc@     | ValidPassword123!     | ValidPassword123!     |

  @short-password-error
  Scenario Outline: User Registration Process with Short Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then an error message should be displayed indicating the password is too short
    And the password field should be highlighted to indicate the error

    Examples:
      | email               | password | confirmation |
      | krishna@gmail.com   | short    | short        |

  @validate_required_fields
  Scenario Outline: User Registration Process
    When the user observes the email field
    Then the email field should have a required field indicator
    When the user observes the password field
    Then the password field should have a required field indicator
    When the user observes the confirmation password field
    Then the confirmation password field should have a required field indicator
    When the user clicks the "Register" button without filling in the fields
    Then the system should display error messages for empty fields
    And the required field indicators should remain visible until the fields are filled

    Examples:
      | <email> | <password> | <confirmation_password> |
      |         |            |                        |

  @invalid-password-length
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password exceeds the maximum length
    And the password field should be highlighted to indicate the error

    Examples:
      | email                | password             | confirmation        |
      | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @empty-fields-error
  Scenario Outline: User Registration Process with Empty Fields
    When the user leaves the email field empty
    And the user leaves the password field empty
    And the user leaves the confirmation password field empty
    And the user clicks the Register button
    Then the system displays error messages for all empty fields
    And the email and password fields are highlighted to indicate the errors

    Examples:
      | email | password | confirmation_password |
      |       |          |                      |

  @feature_user_registration
  @long_password_error
  Scenario Outline: User Registration Process with Long Password
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system displays an error message indicating the password is too long
    And the password field is highlighted to indicate the error

    Examples:
      | email             | password                                                                                                                                                                                                                                                                                                                                 | confirmation                                                                                                                                                                                                                                                                                                                                 |
      | krishna@gmail.com | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system should display an error message indicating the password contains invalid characters

    Examples:
      | email                | password     | confirmation   |
      | krishna@gmail.com    | !@#$%^&*     | !@#$%^&*       |

  @empty_email_error
  Scenario Outline: User Registration Process with Empty Email Field
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters a valid password "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see an error message indicating the email field is required
    And the email field should be highlighted

    Examples:
      | password            | confirmation       |
      | ValidPassword123    | ValidPassword123    |

  @empty-password
  Scenario Outline: User Registration Process with Empty Password Field
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then the system should display an error message indicating the password field is required
    And the password field should be highlighted to indicate the error

    Examples:
      | email                |
      | krishna@gmail.com    |

@empty_confirmation_password
Scenario Outline: User Registration Process with Empty Confirmation Password
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I leave the confirmation password field empty
  And I click the "Register" button
  Then an error message should be displayed indicating the confirmation password field is required
  And the confirmation password field should be highlighted to indicate the error

  Examples:
    | email                | password            |
    | krishna@gmail.com    | ValidPassword123    |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email Format
    When I enter "<email>"
    And I enter "<password>"
    And I confirm "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted

    Examples:
      | email   | password               | confirmation        |
      | abc@    | ValidPassword123!      | ValidPassword123!    |

  @invalid-email-format
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email  | password                | confirmation          |
      | abc@   | ValidPassword123!       | ValidPassword123!     |

  @invalid-email-domain
  Scenario Outline: Validate Registration with Invalid Email Domain
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the Register button
    Then the system should display an error message indicating the email domain is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email    | password              | confirmation         |
      | abc@     | ValidPassword123!     | ValidPassword123!     |

  @invalid-email
  Scenario Outline: User Registration with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password            | confirmation        |
      | abc@!     | ValidPassword123    | ValidPassword123     |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain lowercase letters
    And the password field should be highlighted to indicate the error

    Examples:
      | email              | password       | confirmation    |
      | krishna@gmail.com  | PASSWORD123    | PASSWORD123      |

  @invalid-email-format
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the system displays an error message for the invalid email format
    And the email field is highlighted to indicate the error

    Examples:
      | email              | password               | confirmation          |
      | abc@@example.com   | ValidPassword123!      | ValidPassword123!      |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system should display an error message indicating the password must contain uppercase letters
    And the password field should be highlighted to indicate the error

    Examples:
      | email                        | password      | confirmation  |
      | uniqueuser@example.com      | password123   | password123    |

@invalid-password
Scenario Outline: User Registration Process with Password Without Numbers
  Given the user is on the Registration Page
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then an error message should be displayed indicating the password must contain numbers
  And the password field should be highlighted to indicate the error

  Examples:
    | email               | password                    | confirmation                |
    | krishna@gmail.com   | passwordwithoutnumbers      | passwordwithoutnumbers       |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain special characters
    And the password field should be highlighted to indicate the error

    Examples:
      | email                | password      | confirmation  |
      | krishna@gmail.com    | password123   | password123    |

  @invalid-email
  Scenario Outline: Validate registration fails with invalid email format
    Given the user is on the Registration Page
    When I enter an invalid email format "<email>"
    And I enter a valid password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid

    Examples:
      | email    | password             | confirmation        |
      | abc@     | ValidPassword123     | ValidPassword123     |

  @empty_confirmation_password
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then an error message should be displayed indicating the confirmation password field is required
    And the confirmation password field should be highlighted

    Examples:
      | email               | password            |
      | krishna@gmail.com   | ValidPassword123    |

  @empty-password
  Scenario Outline: User Registration Process with Empty Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then an error message should be displayed indicating the password field is required
    And the password field should be highlighted to indicate the error

    Examples:
      | email               |
      | krishna@gmail.com   |

  @short_password_error
  Scenario Outline: User Registration Fails with Short Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password is too short
    And the password field should be highlighted to indicate the error

    Examples:
      | email              | password | confirmation |
      | krishna@gmail.com  | short    | short        |

  @invalid-email-registration
  Scenario Outline: User Registration Fails with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password             | confirmation          |
      | abc@      | ValidPassword123!    | ValidPassword123!     |

  @long_password_error
  Scenario Outline: User Registration Fails with Long Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system displays an error message for the long password
    And the password field is highlighted to indicate the error

    Examples:
      | email              | password                                                                                  | confirmation                                                                              |
      | krishna@gmail.com  | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @invalid-password
  Scenario Outline: Validate Registration Fails with Invalid Password Characters
    Given the user is on the Registration Page
    When the user enters a unique email address "<email>"
    And the user enters a password "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the system should display an error message indicating the password contains invalid characters

    Examples:
      | email                | password      | confirmation   |
      | krishna@gmail.com    | !@#$%^&*      | !@#$%^&*       |

  @empty-email
  Scenario Outline: User Registration Fails with Empty Email Field
    Given the user is on the Registration Page
    When I leave the email field empty
    And I enter "<password>" in the password field
    And I confirm "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system displays an error message indicating the email field is required
    And the email field is highlighted to indicate the error

    Examples:
      | password             | confirmation        |
      | ValidPassword123     | ValidPassword123     |

  @duplicate-email
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the "Register" button
    Then an error message should be displayed indicating the email is already in use
    And the email field should be highlighted to indicate the error

    Examples:
      | email               | password            | confirmation        |
      | krishna@gmail.com   | ValidPassword123!   | ValidPassword123!    |

  @invalid-password-confirmation
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And I click the Register button
    Then an error message should be displayed indicating the passwords do not match

    Examples:
      | email               | password            | confirmation_password    |
      | krishna@gmail.com   | ValidPassword123    | DifferentPassword456      |

  @invalid-email-registration
  Scenario Outline: Validate Registration Fails with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm the password with "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid

    Examples:
      | email        | password              | confirmation         |
      | abc@         | ValidPassword123!     | ValidPassword123!     |

  @invalid-email
  Scenario Outline: Validate Registration with Invalid Email Format
    When I enter an email address "<email>"
    And I enter a valid password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email  | password              | confirmation         |
      | abc@   | ValidPassword123!     | ValidPassword123!     |

  @invalid-email-format
  Scenario Outline: User Registration Fails with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email      | password             | confirmation        |
      | abc@!      | ValidPassword123     | ValidPassword123     |

  @invalid-email-format
  Scenario Outline: User Registration Fails with Invalid Email Format
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then the user should see an error message indicating the email format is invalid
    And the email field should be highlighted

    Examples:
      | email           | password               | confirmation          |
      | abc@@gmail.com  | ValidPassword123!      | ValidPassword123!      |

  @invalid-password-reg
  Scenario Outline: User Registration Fails with Password Without Uppercase
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password with "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the password must contain uppercase letters

    Examples:
      | email                  | password      | confirmation   |
      | uniqueuser@gmail.com  | password123   | password123     |
