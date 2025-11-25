
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

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a valid email address "<email>"
    And I enter a valid password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the system creates the account and redirects to a success page
    And a confirmation email is received in the inbox

    Examples:
      | email                | password              | confirmation         |
      | krishna@gmail.com    | ValidPassword123!     | ValidPassword123!     |

  @successful_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm "<confirmation_password>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should receive a confirmation email in the inbox

    Examples:
      | email               | password               | confirmation_password     |
      | krishna@gmail.com   | ValidPassword123!      | ValidPassword123!         |

  @unique-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | email               | password            | confirmation       |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm the password as "<confirm_password>"
    And I click the "Register" button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email                     | password        | confirm_password  |
      | uniqueuser@example.com    | Password123!    | Password123!      |

  @valid-registration
  Scenario Outline: User Registration Process
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user is redirected to the login page

    Examples:
      | email               | password            | confirmation        |
      | krishna@gmail.com   | SecurePassword123   | SecurePassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | email               | password      |
      | krishna@gmail.com   | Password123   |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message
    And the user is redirected to the login page

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
      | email                | password           | confirmation        |
      | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @validate_fields_accessibility
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I verify that the email field is visible
    And I verify that the password field is visible
    And I verify that the confirmation password field is visible
    And I enter a unique email address "<email>"
    And I enter a valid password "<password>"
    Then the registration page loads successfully

    Examples:
      | email                | password              |
      | krishna@gmail.com    | ValidPassword123      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation_password>"
    And the user clicks the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                | password        | confirmation_password |
      | krishna@gmail.com    | Password123!    | Password123!          |

  @valid-email
  Scenario Outline: Validate Email Format
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    Then the email field accepts the unique email format

    Examples:
      | email                |
      | krishna@gmail.com    |

  @valid-password
  Scenario Outline: Validate Password Complexity
    Given the user is on the Registration Page
    When I enter a password "<password>"
    Then the password field accepts the valid complex password

    Examples:
      | password        |
      | Password123!    |

  @valid-confirmation
  Scenario Outline: Validate Password Confirmation
    Given the user is on the Registration Page
    When I confirm the password "<confirmation_password>"
    Then the confirmation field matches the password

    Examples:
      | confirmation_password |
      | Password123!          |

  @successful-registration
  Scenario Outline: Verify Successful Registration
    Given the user is on the Registration Page
    When I click the "Register" button
    Then the success message should be displayed on the screen
    And the user is redirected to the login page

    Examples:
      |                      |
      |                      |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email              | password            | confirmation        |
      | krishna@gmail.com  | ValidPassword123    | ValidPassword123     |

  @valid-user-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the Register button
    Then the success message should be displayed on the screen

    Examples:
      | email               | password            | confirmation       |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123    |

  @keyboard_navigation
  Scenario Outline: User Registration Process
    When the user uses the Tab key to focus on the email field
    And the user enters "<email>"
    When the user uses the Tab key to focus on the password field
    And the user enters "<password>"
    When the user uses the Tab key to focus on the confirmation password field
    And the user enters "<confirmation_password>"
    Then the user should be able to successfully focus on the confirmation password field

    Examples:
      | email                | password             | confirmation_password  |
      | krishna@gmail.com    | ValidPassword123     | ValidPassword123        |

  @validate_fields_enabled
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    Then the email field is enabled and can be interacted with
    And the password field is enabled and can be interacted with
    And the confirmation password field is enabled and can be interacted with
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email                | password            |
      | krishna@gmail.com    | ValidPassword123    |

@validate_labels
Scenario Outline: User Registration Process
  Given the user navigates to the registration page
  Then the registration page loads successfully
  And the email field has a visible label that describes its purpose
  And the password field has a visible label that describes its purpose
  And the confirmation password field has a visible label that describes its purpose
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  Then the email field accepts the unique email format
  And the password field accepts the valid password

  Examples:
    | email                | password           |
    | <email>             | <password>        |
    | krishna@gmail.com    | ValidPassword123   |

  @successful_registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the email and password fields should be cleared

    Examples:
      | email                | password             | confirmation        |
      | krishna@gmail.com    | ValidPassword123     | ValidPassword123     |

  @feature_user_registration
  @accessible-fields
  Scenario Outline: User Registration Process
    Given the user navigates to the registration page using a screen reader
    Then the screen reader announces the email field correctly
    And the screen reader announces the password field correctly
    And the screen reader announces the confirmation password field correctly
    When I enter a unique email address "<email>"
    And I enter a valid password "<password>"
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email                 | password           | confirmation_password  |
      | krishna@gmail.com     | ValidPassword123   | ValidPassword123       |

  @mobile-accessibility
  Scenario Outline: User Registration Process
    Given the user opens the registration page on a mobile device
    Then the registration page loads successfully on mobile
    Then the email field is visible and accessible on mobile
    Then the password field is visible and accessible on mobile
    Then the confirmation password field is visible and accessible on mobile
    When the user enters a unique email address "<email>"
    And the user enters a valid password "<password>"
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email              | password           |
      | <email>           | <password>         |
      | krishna@gmail.com  | ValidPassword123   |

  @successful-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then a success message should be displayed indicating the account has been created
    And the user should be redirected to the login page

    Examples:
      | email              | password              | confirmation         |
      | krishna@gmail.com  | ValidPassword123!     | ValidPassword123!     |

  @feature_user_registration
  @tooltip-validation
  Scenario Outline: User Registration Process
    When the user hovers over the email field
    Then the tooltip for the email field should provide information about valid email format
    When the user hovers over the password field
    Then the tooltip for the password field should provide information about password requirements
    When the user hovers over the confirmation password field
    Then the tooltip for the confirmation password field should provide information about matching passwords
    When the user clicks the "Register" button without filling in the fields
    Then the system should display error messages for empty fields
    Then the tooltips should disappear after the user interacts with the fields

    Examples:
      | <username> | <password> |
      |            |            |

  @error_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email is already in use
    And the email field should be highlighted to indicate the error

    Examples:
      | email               | password            | confirmation        |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123     |

  @loading-indicator
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then a loading indicator should be displayed while the registration request is processed

    Examples:
      | email               | password               | confirmation          |
      | krishna@gmail.com   | ValidPassword123!      | ValidPassword123!      |

  @invalid-email
  Scenario Outline: Validate Email and Password Retention After Error
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display an error message for the invalid email
    And the email field should retain "<email>"
    And the password field should retain "<password>"

    Examples:
      | email    | password            | confirmation        |
      | abc@     | ValidPassword123    | ValidPassword123     |

  @empty_fields_validation
  Scenario Outline: User Registration Process with Empty Fields
    When I leave the email field empty
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then error messages should be displayed for all empty fields
    And the email and password fields should be highlighted to indicate the errors

    Examples:
      | email | password | confirmation_password |
      |       |          |                       |

  @invalid-email-format
  Scenario Outline: User Registration with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email    | password            | confirmation         |
      | abc@     | ValidPassword123!   | ValidPassword123!     |

  @password_length_validation
  Scenario Outline: User Registration Process with Exceeding Password Length
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And the user clicks the Register button
    Then the system should display an error message indicating the password exceeds the maximum length
    And the password field should be highlighted to indicate the error

    Examples:
      | email                | password                                                                                                                                                                                                 | confirmation                                                                 |
      | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |
      | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @invalid-password
  Scenario Outline: User Registration Process with Short Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password is too short
    And the password field should be highlighted to indicate the error

    Examples:
      | email                | password | confirmation |
      | krishna@gmail.com    | short    | short        |

  @feature_user_registration
  @required-field-indicators
  Scenario Outline: Validate Required Field Indicators
    When I verify that the email field has a required field indicator
    And I verify that the password field has a required field indicator
    And I verify that the confirmation password field has a required field indicator
    And I click the Register button without filling in the fields
    Then the required field indicators are displayed correctly

    Examples:
      | <email_field_required> | <password_field_required> | <confirmation_password_field_required> | <error_messages_displayed> |
      | Yes                     | Yes                       | Yes                                    | Yes                        |

  @valid-registration
  Scenario Outline: User Registration Process
    When I open the registration page on a touch device
    Then the registration page should load successfully on touch devices
    When I tap on the email field to focus
    Then the email field can be tapped and focused on
    When I enter a unique email address "<email>"
    Then the email field accepts the unique email format
    When I tap on the password field to focus
    Then the password field can be tapped and focused on
    When I enter a valid password "<password>"
    Then the password field accepts the valid password
    When I tap on the confirmation password field to focus
    Then the confirmation password field can be tapped and focused on

    Examples:
      | email                | password              |
      | krishna@gmail.com    | ValidPassword123      |

  @feature_user_registration
  @password-mismatch
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation_password>"
    And the user clicks the Register button
    Then the system displays an error message indicating the passwords do not match
    And the confirmation password field is highlighted to indicate the error

    Examples:
      | email                | password              | confirmation_password   |
      | krishna@gmail.com    | ValidPassword123      | DifferentPassword456    |

  @empty-password
  Scenario Outline: User Registration Process with Empty Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the Register button
    Then an error message should be displayed indicating the password field is required
    And the password field should be highlighted to indicate the error

    Examples:
      | email             |
      | krishna@gmail.com |

  @empty_email_error
  Scenario Outline: User Registration Process with Empty Email Field
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system should display an error message indicating the email field is required
    And the email field should be highlighted to indicate the error

    Examples:
      | password           | confirmation        |
      | ValidPassword123   | ValidPassword123     |
```gherkin
  @invalid-password
  Scenario Outline: User Registration Process with Long Password
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system displays an error message indicating the password is too long

    Examples:
      | email             | password

  @empty_confirmation_password
  Scenario Outline: User Registration Process with Empty Confirmation Password
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user leaves the confirmation password field empty
    And the user clicks the "Register" button
    Then the user should see an error message indicating the confirmation password field is required

    Examples:
      | email                | password            |
      | krishna@gmail.com    | ValidPassword123    |

  @invalid-email
  Scenario Outline: User Registration with Invalid Email Domain
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the Register button
    Then the system should display an error message indicating the email domain is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password           | confirmation         |
      | abc@      | ValidPassword123!  | ValidPassword123!     |

@invalid-password
Scenario Outline: User Registration Process with Invalid Password Characters
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then an error message should be displayed indicating the password contains invalid characters
  And the password field should be highlighted to indicate the error

  Examples:
    | email               | password      | confirmation   |
    | krishna@gmail.com   | !@#$%^&*      | !@#$%^&*       |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the "Register" button
    Then the system displays an error message for the invalid email format
    And the email field is highlighted to indicate the error

    Examples:
      | email       | password               | confirmation           |
      | abc@       | ValidPassword123!      | ValidPassword123!      |

  @invalid-password
  Scenario Outline: User Registration Process with Password Lacking Uppercase Letters
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then the system should display an error message indicating that the password must contain uppercase letters
    And the password field should be highlighted to indicate the error

    Examples:
      | email                     | password      | confirmation   |
      | uniqueuser@example.com    | password123   | password123     |

  @invalid-password
  Scenario Outline: User Registration Process with Password Without Numbers
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the password must contain numbers
    And the password field should be highlighted to indicate the error

    Examples:
      | email                | password                     | confirmation                |
      | krishna@gmail.com    | passwordwithoutnumbers       | passwordwithoutnumbers       |

  @invalid-email-format
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message indicating the email format is invalid should be displayed
    And the email field should be highlighted

    Examples:
      | email              | password              | confirmation          |
      | abc@@example.com   | ValidPassword123!     | ValidPassword123!      |

  @invalid-password
  Scenario Outline: Validate Password Complexity Requirements
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain lowercase letters
    And the password field should be highlighted to indicate the error

    Examples:
      | email                 | password       | confirmation   |
      | krishna@gmail.com     | PASSWORD123    | PASSWORD123     |

  @invalid-email
  Scenario Outline: User Registration with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email   | password              | confirmation          |
      | abc@    | ValidPassword123!     | ValidPassword123!     |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password            | confirmation        |
      | abc@!     | ValidPassword123    | ValidPassword123     |

  @invalid-email-format
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email    | password           | confirmation        |
      | abc@     | ValidPassword123   | ValidPassword123     |

  @invalid-password
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the Register button
    Then the system displays an error message indicating the password must contain special characters

    Examples:
      | email               | password      | confirmation   |
      | krishna@gmail.com   | password123   | password123     |

  @empty-password
  Scenario Outline: User Registration Fails When Password Field is Empty
    Given the user enters "<email>" in the email field
    And the user leaves the password field empty
    And the user leaves the confirmation password field empty
    When the user clicks the Register button
    Then an error message should be displayed indicating the password field is required
    And the password field should be highlighted to indicate the error

    Examples:
      | email               |
      | krishna@gmail.com   |

  @invalid-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system should display an error message indicating the email is already in use
    And the email field should be highlighted to indicate the error

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @empty-email
  Scenario Outline: User Registration Process with Empty Email Field
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms "<password>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see an error message indicating the email field is required
    And the email field should be highlighted to indicate the error

    Examples:
      | password            |
      | ValidPassword123    |

  @empty_confirmation_password
  Scenario Outline: User Registration Fails with Empty Confirmation Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then an error message is displayed indicating the confirmation password field is required
    And the confirmation password field is highlighted to indicate the error

    Examples:
      | email                | password             |
      | krishna@gmail.com    | ValidPassword123     |

  @invalid-password-length
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then the system should display an error message indicating the password is too long

    Examples:
      | email             | password                                                                                      | confirmation                                                                                 |
      | krishna@gmail.com | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |
      | krishna@gmail.com | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @password_mismatch
  Scenario Outline: Validate registration failure due to password mismatch
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation_password>"
    And I click the "Register" button
    Then an error message should be displayed indicating the passwords do not match
    And the confirmation password field should be highlighted

    Examples:
      | email               | password               | confirmation_password   |
      | krishna@gmail.com   | ValidPassword123       | DifferentPassword456     |

  @invalid-email
  Scenario Outline: User Registration Fails with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the email format is invalid

    Examples:
      | email     | password              | confirmation         |
      | abc@      | ValidPassword123!     | ValidPassword123!     |

  @invalid-email
  Scenario Outline: User Registration Fails with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system displays an error message indicating the email format is invalid
    And the email field is highlighted to indicate the error

    Examples:
      | email    | password              | confirmation          |
      | abc@     | ValidPassword123!     | ValidPassword123!     |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password Characters
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see an error message indicating the password contains invalid characters
    And the password field should be highlighted to indicate the error

    Examples:
      | email               | password      | confirmation   |
      | krishna@gmail.com   | !@#$%^&*     | !@#$%^&*       |

  @invalid-email
  Scenario Outline: Validate registration fails for email without a domain
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system displays an error message indicating the email format is invalid
    And the email field is highlighted to indicate the error

    Examples:
      | email   | password              | confirmation         |
      | abc@    | ValidPassword123!     | ValidPassword123!     |

  @invalid-password
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see an error message indicating the password must contain uppercase letters

    Examples:
      | email                    | password      | confirmation   |
      | uniqueuser@gmail.com    | password123   | password123     |

  @invalid-email-registration
  Scenario Outline: User Registration Process with Invalid Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm "<confirmation>"
    And the user clicks the Register button
    Then the system displays an error message for the invalid email format
    And the email field is highlighted to indicate the error

    Examples:
      | email     | password            | confirmation         |
      | abc@!     | ValidPassword123    | ValidPassword123     |

  @short-password
  Scenario Outline: User Registration Fails with Short Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the user should see an error message indicating the password is too short

    Examples:
      | email              | password | confirmation |
      | krishna@gmail.com  | short    | short        |

  @invalid-email-format
  Scenario Outline: User Registration Process with Invalid Email Format
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email            | password              | confirmation         |
      | abc@@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a valid email address "<email>"
    And I enter a valid password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the system should create the account and redirect to a success page
    And a confirmation email should be received in the inbox

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @unique-email-registration
  Scenario Outline: User Registration Process
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user should be redirected to the Login Page

    Examples:
      | email               | password           | confirmation        |
      | krishna@gmail.com   | ValidPassword123   | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirm_password>"
    And I click the "Register" button
    Then the user should see the success message displayed on the screen
    And the user should be redirected to the login page

    Examples:
      | email                     | password       | confirm_password |
      | uniqueuser@example.com    | Password123!   | Password123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password with "<confirm_password>"
    And I click the "Register" button
    Then the user should see a success message

    Examples:
      | email               | password      | confirm_password |
      | krishna@gmail.com   | Password123   | Password123      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation_password>"
    And I click the "Register" button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email               | password              | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123      | ValidPassword123        |

@successful-registration
Scenario Outline: User Registration Process
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation_password>" in the confirmation field
  And I click the "Register" button
  Then the user should receive a confirmation email in the inbox

  Examples:
    | email               | password               | confirmation_password     |
    | krishna@gmail.com   | ValidPassword123!      | ValidPassword123!         |

@successful-registration
Scenario Outline: Validate Email Format
  When I enter "<email>" in the email field
  Then the email field should accept the unique email format

  Examples:
    | email               |
    | krishna@gmail.com   |

@successful-registration
Scenario Outline: Validate Password
  When I enter "<password>" in the password field
  Then the password field should accept the valid password

  Examples:
    | password               |
    | ValidPassword123!      |

@successful-registration
Scenario Outline: Validate Confirmation Password
  When I enter "<confirmation_password>" in the confirmation field
  Then the confirmation field should match the password

  Examples:
    | confirmation_password     |
    | ValidPassword123!         |

@successful-registration
Scenario Outline: Check Account Creation
  When I click the "Register" button
  Then the system should create the account and redirect to a success page

  Examples:
    |                          |
    |                          |

@successful-registration
Scenario Outline: Check Confirmation Email
  Then the user should receive a confirmation email in the inbox

  Examples:
    |                          |
    |                          |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the user should see a success message displayed
    And the user should be redirected to the login page

    Examples:
      | email                 | password        | confirmation     |
      | krishna@gmail.com     | Password123!    | Password123!      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see the success message displayed on the screen
    And the user should be redirected to the login page

    Examples:
      | email               | password             | confirmation        |
      | krishna@gmail.com   | SecurePassword123    | SecurePassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should create the account and display a success message
    And the user should be redirected to the login page

    Examples:
      | email               | password             | confirmation        |
      | <email>            | <password>          | <confirmation>      |
      | krishna@gmail.com   | ValidPassword123     | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters a unique email address "<email>"
    And the user enters a password that meets the maximum length requirement "<password>"
    And the user confirms the password in the confirmation field "<confirmation_password>"
    And the user clicks the "Register" button
    Then the user should see a success message displayed on the screen
    And the user should be redirected to the Login Page

    Examples:
      | email               | password           | confirmation_password |
      | krishna@gmail.com   | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

@validate_email_password_fields
Scenario Outline: User Registration Process
  Given the user is on the Registration Page
  When the user verifies that the email field is visible
  And the user verifies that the password field is visible
  And the user verifies that the confirmation password field is visible
  And the user enters a unique email address in the email field
  And the user enters a valid password in the password field
  Then the registration page should load successfully

  Examples:
    | email                 | password            |
    | krishna@gmail.com     | ValidPassword123    |

  @focus-accessibility
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user uses the Tab key to focus on the email field
    And the user enters "<email>" in the email field
    And the user uses the Tab key to focus on the password field
    And the user enters "<password>" in the password field
    And the user uses the Tab key to focus on the confirmation password field
    And the user enters "<confirmation_password>" in the confirmation password field
    Then the user should successfully complete the registration process

    Examples:
      | email              | password              | confirmation_password  |
      | krishna@gmail.com  | ValidPassword123      | ValidPassword123        |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user verifies that the email field is enabled
    And the user verifies that the password field is enabled
    And the user verifies that the confirmation password field is enabled
    And the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email                | password             | confirmation_password |
      | krishna@gmail.com    | ValidPassword123     | ValidPassword123      |

  @mobile-accessibility
  Scenario Outline: User Registration Process
    Given the registration page loads successfully on mobile
    Then the email field is visible and accessible on mobile
    And the password field is visible and accessible on mobile
    And the confirmation password field is visible and accessible on mobile
    When I enter a unique email address "<email>"
    And I enter a valid password "<password>"
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email                | password           |
      | <email>             | <password>         |
      | krishna@gmail.com    | ValidPassword123   |

@validate_labels
Scenario Outline: User Registration Process - Validate Labels
  Given the user is on the Registration Page
  Then the email field has a visible label
  And the password field has a visible label
  And the confirmation password field has a visible label
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  Then the user should be able to register successfully

  Examples:
    | email              | password            |
    | krishna@gmail.com  | ValidPassword123    |

@enter_unique_email
Scenario Outline: User Registration Process - Enter Unique Email
  Given the user is on the Registration Page
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  Then the user should be able to register successfully

  Examples:
    | email              | password            |
    | krishna@gmail.com  | ValidPassword123    |

@enter_valid_password
Scenario Outline: User Registration Process - Enter Valid Password
  Given the user is on the Registration Page
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  Then the user should be able to register successfully

  Examples:
    | email              | password            |
    | krishna@gmail.com  | ValidPassword123    |

  @invalid-email-registration
  Scenario Outline: User Registration with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the Register button
    Then the email and password fields should retain their values after the error message is displayed

    Examples:
      | email     | password             | confirmation       |
      | abc@      | ValidPassword123     | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user opens the registration page on a touch device
    When the user taps on the email field to focus
    And the user enters "<email>" in the email field
    And the user taps on the password field to focus
    And the user enters "<password>" in the password field
    And the user taps on the confirmation password field to focus
    And the user enters "<confirmation_password>" in the confirmation password field
    Then the user should be able to register successfully

    Examples:
      | email                 | password            | confirmation_password  |
      | krishna@gmail.com     | ValidPassword123    | ValidPassword123        |

  @successful-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then a success message is displayed indicating the account has been created
    And the user is redirected to the login page

    Examples:
      | email              | password               | confirmation         |
      | krishna@gmail.com  | ValidPassword123!      | ValidPassword123!     |

  @duplicate_email_error
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system should display an error message indicating the email is already in use
    And the email field should be highlighted to indicate the error

    Examples:
      | email              | password            | confirmation        |
      | krishna@gmail.com  | ValidPassword123    | ValidPassword123     |

  @loading_indicator
  Scenario Outline: Validate Loading State During Registration
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then a loading indicator should be displayed while the registration request is processed

    Examples:
      | email                | password               | confirmation          |
      | krishna@gmail.com    | ValidPassword123!      | ValidPassword123!      |

  @clear_fields_after_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the email field should be cleared
    And the password field should be cleared

    Examples:
      | email               | password          | confirmation       |
      | krishna@gmail.com   | ValidPassword123  | ValidPassword123    |

  @accessible_fields
  Scenario Outline: User Registration Process
    Given the user navigates to the registration page using a screen reader
    Then the screen reader should announce the email field correctly
    And the screen reader should announce the password field correctly
    And the screen reader should announce the confirmation password field correctly
    When the user enters a unique email address "<email>"
    And the user enters a valid password "<password>"
    Then the email field should accept the unique email format
    And the password field should accept the valid password

    Examples:
      | email                 | password             |
      | <email>              | <password>           |
      | krishna@gmail.com     | ValidPassword123     |

  @invalid-password-length
  Scenario Outline: User Registration Process with Exceeding Password Length
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password exceeds the maximum length
    And the password field should be highlighted to indicate the error

    Examples:
      | email                | password                                                                                       | confirmation                                                                                   |
      | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

@validate_required_fields
Scenario Outline: Validate Required Field Indicators on Registration Page
  When I navigate to the registration page
  Then the registration page loads successfully
  And the email field has a required field indicator
  And the password field has a required field indicator
  And the confirmation password field has a required field indicator
  When I click the Register button without filling in the fields
  Then the system displays error messages for empty fields
  And the required field indicators remain visible until the fields are filled

  Examples:
    | username | password | confirmation_password |
    |          |          |                      |

  @empty_fields
  Scenario Outline: User Registration Process with Empty Fields
    When I leave the email field empty
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the Register button
    Then error messages should be displayed for all empty fields

    Examples:
      | email | password | confirmation_password |
      |       |          |                       |

@invalid-email
Scenario Outline: Validate Registration with Invalid Email Format
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I confirm the password as "<confirmation>"
  And I click the "Register" button
  Then an error message should be displayed indicating the email format is invalid
  And the email field should be highlighted to indicate the error

  Examples:
    | email     | password              | confirmation         |
    | abc@      | ValidPassword123!     | ValidPassword123!     |

  @tooltip_validation
  Scenario Outline: User Registration Page Tooltip Information
    When I hover over the email field
    Then the tooltip for the email field should provide information about valid email format
    When I hover over the password field
    Then the tooltip for the password field should provide information about password requirements
    When I hover over the confirmation password field
    Then the tooltip for the confirmation password field should provide information about matching passwords
    When I click the "Register" button without filling in the fields
    Then the system should display error messages for empty fields
    And the tooltips should disappear after the user interacts with the fields

    Examples:
      |   |
      |   |

  @password_mismatch
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation_password>"
    And I click the "Register" button
    Then an error message should be displayed indicating the passwords do not match

    Examples:
      | email                | password             | confirmation_password   |
      | krishna@gmail.com    | ValidPassword123     | DifferentPassword456     |

  @feature_user_registration
  @long_password_error
  Scenario Outline: User Registration Process with Long Password
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters a password that exceeds the maximum length in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then an error message should be displayed indicating the password is too long
    And the password field should be highlighted to indicate the error

    Examples:
      | email               | confirmation       |
      | krishna@gmail.com   | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @empty_password
  Scenario Outline: User Registration with Empty Password Field
    Given the user is on the Registration Page
    When I enter "<email>"
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then an error message should be displayed indicating the password field is required
    And the password field should be highlighted to indicate the error

    Examples:
      | email                |
      | krishna@gmail.com    |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password contains invalid characters

    Examples:
      | email               | password      | confirmation   |
      | krishna@gmail.com   | !@#$%^&*      | !@#$%^&*       |

  @empty-email-error
  Scenario Outline: Validate registration error message for empty email
    Given the user is on the Registration Page
    And the email field is empty
    When I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the Register button
    Then the system displays an error message indicating the email field is required
    And the email field is highlighted to indicate the error

    Examples:
      | password            | confirmation        |
      | ValidPassword123    | ValidPassword123     |

  @empty_confirmation_password
  Scenario Outline: User Registration Process with Empty Confirmation Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then an error message should be displayed indicating the confirmation password field is required

    Examples:
      | email              | password           |
      | krishna@gmail.com  | ValidPassword123   |

  @invalid-email
  Scenario Outline: Validate Registration with Invalid Email Domain
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email domain is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password               | confirmation          |
      | abc@      | ValidPassword123!      | ValidPassword123!     |

  @invalid-email-format
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the system displays an error message for the invalid email format

    Examples:
      | email | password              | confirmation          |
      | abc@  | ValidPassword123!     | ValidPassword123!     |

  @short_password_error
  Scenario Outline: User Registration with Short Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display an error message indicating the password is too short

    Examples:
      | email               | password | confirmation |
      | krishna@gmail.com   | short    | short        |

  @valid_email
  Scenario Outline: User Registration with Valid Email
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display a success message indicating registration is complete

    Examples:
      | email               | password   | confirmation   |
      | validuser@gmail.com | validPass1  | validPass1     |

@invalid-email
Scenario Outline: User Registration Process with Invalid Email Format
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I confirm the password with "<confirmation>"
  And I click the "Register" button
  Then the system should display an error message indicating the email format is invalid
  And the email field should be highlighted to indicate the error

  Examples:
    | email   | password           | confirmation         |
    | abc@    | ValidPassword123!  | ValidPassword123!     |

  Scenario Outline: Validate that the registration fails when an invalid email format is entered
    When the user enters an invalid email format "<email>" in the email field
    And the user enters a valid password "<password>" in the password field
    And the user confirms the password "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then an error message is displayed indicating the email format is invalid
    And the email field is highlighted to indicate the error

  Examples:
    | email   | password            | confirmation         |
    | abc@    | ValidPassword123    | ValidPassword123      |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain uppercase letters
    And the password field should be highlighted to indicate the error

    Examples:
      | email                       | password      | confirmation  |
      | uniqueuser@example.com      | password123   | password123    |

  @invalid-password
  Scenario Outline: Validate Error Message for Password Without Numbers
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then an error message should be displayed indicating the password must contain numbers

    Examples:
      | email                | password                     | confirmation                |
      | krishna@gmail.com    | passwordwithoutnumbers       | passwordwithoutnumbers       |

  @invalid-email-format
  Scenario Outline: Validate Registration with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed for the invalid email format
    And the email field should be highlighted

    Examples:
      | email    | password           | confirmation        |
      | abc@!    | ValidPassword123   | ValidPassword123     |

@invalid-password
Scenario Outline: User Registration Process
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I confirm the password "<confirmation>"
  And I click the "Register" button
  Then the user should see an error message indicating the password must contain lowercase letters
  And the password field should be highlighted to indicate the error

  Examples:
    | email                | password       | confirmation    |
    | krishna@gmail.com    | PASSWORD123    | PASSWORD123      |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted

    Examples:
      | email              | password              | confirmation          |
      | abc@@example.com   | ValidPassword123!     | ValidPassword123!      |

  @empty-password
  Scenario Outline: User Registration Fails When Password Field Is Empty
    Given the user has entered "<email>" in the email field
    And the user has left the password field empty
    And the user has left the confirmation password field empty
    When the user clicks the "Register" button
    Then the system should display an error message indicating the password field is required
    And the password field should be highlighted to indicate the error

    Examples:
      | email              |
      | krishna@gmail.com  |

  @feature_user_registration
  @duplicate-email-registration
  Scenario Outline: User Registration Process with Already Registered Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email is already in use
    And the email field should be highlighted to indicate the error

    Examples:
      | email                | password               | confirmation          |
      | krishna@gmail.com    | ValidPassword123!      | ValidPassword123!      |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain special characters

    Examples:
      | email               | password      | confirmation   |
      | krishna@gmail.com   | password123   | password123     |

  @empty_email_registration
  Scenario Outline: Validate Registration Fails with Empty Email
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the Register button
    Then the system should display an error message for the empty email field
    And the email field should be highlighted to indicate the error

    Examples:
      | password           | confirmation       |
      | ValidPassword123   | ValidPassword123    |

  Scenario Outline: Validate that the registration fails when a password that exceeds the maximum length is entered
    When I enter a unique email address "<email>" in the email field
    And I enter a password that exceeds the maximum length "<password>" in the password field
    And I confirm the password "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then I verify that an error message is displayed indicating the password is too long
    And I verify that the password field is highlighted to indicate the error

    Examples:
      | email               | password                     | confirmation                 |
      | krishna@gmail.com   | aaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaa |

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
      | email               | password            |
      | krishna@gmail.com   | ValidPassword123    |

  @invalid-password
  Scenario Outline: User Registration Process with Short Password
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password that is shorter than the minimum length "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then the system displays an error message indicating the password is too short

    Examples:
      | email                | password | confirmation |
      | krishna@gmail.com    | short    | short        |

  @password_mismatch
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And I click the Register button
    Then the system should display an error message indicating the passwords do not match

    Examples:
      | email               | password              | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123      | DifferentPassword456    |

@invalid-password
Scenario Outline: User Registration Fails with Invalid Characters in Password
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I enter "<confirmation>"
  And I click the "Register" button
  Then an error message should be displayed indicating the password contains invalid characters
  And the password field should be highlighted to indicate the error

  Examples:
    | email               | password      | confirmation   |
    | krishna@gmail.com   | !@#$%^&*      | !@#$%^&*       |

  @invalid-email
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email       | password             | confirmation        |
      | abc         | ValidPassword123!    | ValidPassword123!    |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email            | password              | confirmation         |
      | abc@@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @invalid-email
  Scenario Outline: User Registration with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email   | password              | confirmation        |
      | abc@    | ValidPassword123!     | ValidPassword123!    |

  @invalid-email
  Scenario Outline: Validate Registration with Invalid Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted

    Examples:
      | email   | password            | confirmation        |
      | abc@!   | ValidPassword123    | ValidPassword123     |

  @invalid-password
  Scenario Outline: User Registration Process with Password Without Uppercase Letters
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the Register button
    Then an error message should be displayed indicating the password must contain uppercase letters

    Examples:
      | email                   | password      | confirmation   |
      | uniqueuser@gmail.com    | password123   | password123     |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system displays an error message indicating the email format is invalid
    And the email field is highlighted to indicate the error

    Examples:
      | email   | password             | confirmation         |
      | abc@    | ValidPassword123!    | ValidPassword123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password as "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | email               | password             | confirmation        |
      | krishna@gmail.com   | ValidPassword123     | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password as "<confirmation>"
    And the user clicks the Register button
    Then the user should be redirected to a success page
    And the user checks the inbox for a confirmation email

    Examples:
      | email                | password             | confirmation        |
      | krishna@gmail.com    | ValidPassword123!    | ValidPassword123!    |

  @valid-registration
  Scenario Outline: User Registration Process
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then the success message should be displayed on the screen
    And the user should be redirected to the Login Page

    Examples:
      | email               | password            | confirmation       |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123    |

  @successful-registration
  Scenario Outline: User Registration Process
    When the user enters "<email>"
    And the user enters "<password>"
    And the user enters "<confirmation_password>"
    And the user clicks the Register button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email               | password            | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123        |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation_password>"
    And the user clicks the "Register" button
    Then the user should be redirected to a success page
    And the user checks the inbox for a confirmation email

    Examples:
      | email                | password              | confirmation_password   |
      | krishna@gmail.com    | ValidPassword123!     | ValidPassword123!       |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirm_password>"
    And I click the "Register" button
    Then the user should see a success message

    Examples:
      | email                     | password        | confirm_password  |
      | uniqueuser@example.com    | Password123!    | Password123!      |

  @valid-email
  Scenario Outline: Validate Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    Then the email field accepts the unique email format

    Examples:
      | email                     |
      | uniqueuser@example.com    |

  @valid-password
  Scenario Outline: Validate Password Requirements
    Given the user is on the Registration Page
    When I enter "<password>"
    Then the password field accepts the valid password with special characters

    Examples:
      | password        |
      | Password123!    |

  @valid-confirmation
  Scenario Outline: Validate Password Confirmation
    Given the user is on the Registration Page
    When I enter "<confirm_password>"
    Then the confirmation field matches the password

    Examples:
      | confirm_password  |
      | Password123!      |

  @success-message
  Scenario Outline: Verify Success Message
    Given the user is on the Registration Page
    When I click the "Register" button
    Then the user should see a success message

    Examples:
      | 

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation_password>"
    And I click the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | email                | password         | confirmation_password |
      | krishna@gmail.com    | Password123!     | Password123!          |

  @valid-email
  Scenario Outline: Validate Email Format
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    Then the email field accepts the unique email format

    Examples:
      | email                |
      | krishna@gmail.com    |

  @valid-password
  Scenario Outline: Validate Password Complexity
    Given the user is on the Registration Page
    When I enter a password "<password>"
    Then the password field accepts the valid complex password

    Examples:
      | password         |
      | Password123!     |

  @valid-confirmation
  Scenario Outline: Validate Password Confirmation
    Given the user is on the Registration Page
    When I confirm the password "<confirmation_password>"
    Then the confirmation field matches the password

    Examples:
      | confirmation_password |
      | Password123!          |

  @redirect-login
  Scenario Outline: Redirect to Login Page
    Given the user is on the Registration Page
    When I click the "Register" button
    Then the user is redirected to the login page

    Examples:
      |                      |
      |                      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then a success message should be displayed on the screen

    Examples:
      | email                | password              | confirmation_password   |
      | krishna@gmail.com    | SecurePassword123     | SecurePassword123       |

  @feature_user_registration
  @validate_email_password_fields
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    Then the email field is visible and accessible
    And the password field is visible and accessible
    And the confirmation password field is visible and accessible
    When I enter a unique email address "<email>"
    And I enter a valid password "<password>"
    Then the registration process can proceed

    Examples:
      | email               | password            |
      | krishna@gmail.com   | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user navigates to the registration page
    When the user enters "<email>"
    And the user enters a password of maximum length "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message

    Examples:
      | email                | password                                                                                             | confirmation                                                                                         |
      | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |
      | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @valid-registration
  Scenario Outline: User Registration Process
    When I navigate to the registration page
    And I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirm_password>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email               | password      | confirm_password |
      | krishna@gmail.com   | Password123   | Password123      |

@focus-accessibility
Scenario Outline: User Registration Process
  Given the user is on the Registration Page
  When the user presses the Tab key to focus on the email field
  And the user enters "<email>"
  And the user presses the Tab key to focus on the password field
  And the user enters "<password>"
  And the user presses the Tab key to focus on the confirmation password field
  Then the user should see that the email field accepts the unique email format
  And the user should see that the password field accepts the valid password

  Examples:
    | email                | password            |
    | krishna@gmail.com    | ValidPassword123    |

  @validate_fields
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I verify that the email field is enabled
    And I verify that the password field is enabled
    And I verify that the confirmation password field is enabled
    And I enter "<email>" in the email field
    And I enter "<password>" in the password field
    Then the registration page should load successfully

    Examples:
      | email               | password           |
      | krishna@gmail.com   | ValidPassword123   |

@validate_labels
Scenario Outline: User Registration Process
  Given the user is on the Registration Page
  Then the email field has a visible label
  And the password field has a visible label
  And the confirmation password field has a visible label
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  Then the email field accepts the unique email format
  And the password field accepts the valid password

  Examples:
    | email                | password            |
    | krishna@gmail.com    | ValidPassword123    |

  @accessible_fields
  Scenario Outline: User Registration Process
    Given the user navigates to the registration page using a screen reader
    Then the screen reader should announce the email field correctly
    And the screen reader should announce the password field correctly
    And the screen reader should announce the confirmation password field correctly
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    Then the email field should accept the unique email format
    And the password field should accept the valid password

    Examples:
      | email               | password            |
      | <email>            | <password>         |
      | krishna@gmail.com   | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  Scenario Outline: User Registration Process
    When the user enters a unique email address "<email>" in the email field
    And the user enters a valid password "<password>" in the password field
    And the user confirms the password "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then a success message is displayed indicating the account has been created
    And the user is redirected to the login page

    Examples:
      | email               | password              | confirmation         |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @successful_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the Register button
    Then the email and password fields should be cleared after successful registration

    Examples:
      | email                | password            | confirmation         |
      | krishna@gmail.com    | ValidPassword123    | ValidPassword123      |

  @feature_user_registration
  @invalid_email_entry
  Scenario Outline: User Registration with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the Register button
    Then the email and password fields should retain their values after the error message is displayed

    Examples:
      | email   | password            | confirmation        |
      | abc@    | ValidPassword123    | ValidPassword123     |

  @mobile-accessibility-check
  Scenario Outline: User Registration Process
    When I open the registration page on a mobile device
    Then the registration page loads successfully on mobile
    And the email field is visible and accessible on mobile
    And the password field is visible and accessible on mobile
    And the confirmation password field is visible and accessible on mobile
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email                 | password            |
      | krishna@gmail.com     | ValidPassword123    |

  @loading-indicator-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then a loading indicator should be displayed while the registration request is processed

    Examples:
      | email                | password              | confirmation         |
      | krishna@gmail.com    | ValidPassword123!     | ValidPassword123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user opens the registration page on a touch device
    When the user taps on the email field to focus
    And the user enters "<email>" in the email field
    And the user taps on the password field to focus
    And the user enters "<password>" in the password field
    And the user taps on the confirmation password field to focus
    And the user enters "<confirmation_password>" in the confirmation password field
    Then the user should be able to register successfully

    Examples:
      | email               | password              | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123      | ValidPassword123        |

  @registration-error
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the Register button
    Then the user should see an error message indicating the email is already in use
    And the email field should be highlighted to indicate the error

    Examples:
      | email               | password            | confirmation       |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123    |

  @empty_fields_error
  Scenario Outline: User Registration Process with Empty Fields
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user leaves the password field empty
    And the user leaves the confirmation password field empty
    And the user clicks the Register button
    Then error messages should be displayed for all empty fields
    And the email field should be highlighted
    And the password field should be highlighted
    And the confirmation password field should be highlighted

    Examples:
      | email | password | confirmation_password |
      |       |          |                       |

  @validate_required_fields
  Scenario Outline: User Registration Process
    When the user verifies that the email field has a required field indicator
    And the user verifies that the password field has a required field indicator
    And the user verifies that the confirmation password field has a required field indicator
    And the user clicks the "Register" button without filling in the fields
    Then the system should display error messages for empty fields
    And the required field indicators should remain visible until the fields are filled

    Examples:
      | email_field_required | password_field_required | confirmation_password_field_required | error_messages_displayed | required_field_indicators_visible |
      | true                 | true                   | true                               | true                    | true                             |

  Scenario Outline: User Registration with Empty Password
    When I enter a unique email address <email>
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then I should see an error message indicating the password field is required
    And the password field should be highlighted to indicate the error

    Examples:
      | email              |
      | krishna@gmail.com  |

  @feature_user_registration
  @tooltip-validation
  Scenario Outline: User Registration Process - Tooltip Validation
    When the user hovers over the email field
    Then the tooltip should provide information about valid email format

    When the user hovers over the password field
    Then the tooltip should provide information about password requirements

    When the user hovers over the confirmation password field
    Then the tooltip should provide information about matching passwords

    When the user clicks the "Register" button without filling in the fields
    Then the system should display error messages for empty fields
    And the tooltips should disappear after the user interacts with the fields

    Examples:
      | action |
      | hover  |

  @invalid-email-format
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email    | password              | confirmation          |
      | abc@     | ValidPassword123!     | ValidPassword123!     |

@invalid-password-length
Scenario Outline: Validate Password Length Exceeding Maximum Limit
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then an error message should be displayed indicating the password exceeds the maximum length
  And the password field should be highlighted to indicate the error

  Examples:
    | email                | password                    | confirmation               |
    | krishna@gmail.com    | aaaaaaaaaaaaaaaaaaaaaaaaaa  | aaaaaaaaaaaaaaaaaaaaaaaaaa |

@short-password-error
Scenario Outline: User Registration Process with Short Password
  Given the user is on the Registration Page
  When the user enters a unique email address "<email>"
  And the user enters a password that is shorter than the minimum length "<password>"
  And the user confirms the password in the confirmation field "<confirmation>"
  And the user clicks the Register button
  Then the system displays an error message indicating the password is too short

  Examples:
    | email               | password | confirmation |
    | krishna@gmail.com   | short    | short        |

  Scenario Outline: Validate that the registration page shows an error message when an email without "@" is entered
    When the user enters an email address without "@" in the email field as <email>
    And the user enters a valid password in the password field as <password>
    And the user confirms the password in the confirmation field as <confirmation>
    And the user clicks the "Register" button
    Then an error message is displayed indicating the email format is invalid
    And the email field is highlighted to indicate the error

    Examples:
      | email        | password              | confirmation          |
      | abc@        | ValidPassword123!     | ValidPassword123!     |

  @empty_email_error
  Scenario Outline: User Registration Process with Empty Email Field
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then an error message should be displayed indicating the email field is required
    And the email field should be highlighted to indicate the error

    Examples:
      | password            | confirmation       |
      | ValidPassword123    | ValidPassword123    |

  @password_mismatch
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation password field
    And I click the "Register" button
    Then an error message should be displayed indicating the passwords do not match
    And the confirmation password field should be highlighted

    Examples:
      | email              | password              | confirmation_password    |
      | krishna@gmail.com  | ValidPassword123      | DifferentPassword456      |

  @invalid-email-domain
  Scenario Outline: User Registration with Invalid Email Domain
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display an error message indicating the email domain is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email      | password              | confirmation         |
      | abc@       | ValidPassword123!     | ValidPassword123!     |

@empty_confirmation_password
Scenario Outline: User Registration Process with Empty Confirmation Password
  Given the user is on the Registration Page
  When I enter a unique email address "<email>"
  And I enter a valid password "<password>"
  And I leave the confirmation password field empty
  And I click the "Register" button
  Then an error message should be displayed indicating the confirmation password field is required
  And the confirmation password field should be highlighted

  Examples:
    | email               | password           |
    | krishna@gmail.com   | ValidPassword123   |

@invalid-password
Scenario Outline: User Registration Process with Invalid Password Characters
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I confirm the password as "<confirmation>"
  And I click the "Register" button
  Then an error message should be displayed indicating the password contains invalid characters

  Examples:
    | email               | password     | confirmation   |
    | krishna@gmail.com   | !@#$%^&*     | !@#$%^&*       |

  @invalid-password-length
  Scenario Outline: User Registration Process with Exceeding Password Length
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the password is too long
    And the password field should be highlighted

    Examples:
      | email                 | password                         | confirmation                     |
      | krishna@gmail.com     | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa... | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa... |

@invalid-email-format
Scenario Outline: User Registration Process with Invalid Email Format
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then an error message should be displayed indicating the email format is invalid
  And the email field should be highlighted to indicate the error

  Examples:
    | email    | password             | confirmation         |
    | abc@!    | ValidPassword123     | ValidPassword123     |

@invalid-password
Scenario Outline: User Registration Process with Password Without Numbers
  Given the user is on the Registration Page
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I confirm the password with "<confirmation>"
  And I click the "Register" button
  Then an error message should be displayed indicating the password must contain numbers

  Examples:
    | email               | password                     | confirmation                 |
    | krishna@gmail.com   | passwordwithoutnumbers       | passwordwithoutnumbers       |

  @invalid-email-format
  Scenario Outline: User Registration Process with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email             | password                | confirmation          |
      | abc@@example.com  | ValidPassword123!       | ValidPassword123!      |

  @invalid-email
  Scenario Outline: User Registration with Email Without Domain
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password             | confirmation         |
      | abc@      | ValidPassword123!    | ValidPassword123!     |

  @invalid-password
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain special characters

    Examples:
      | email               | password       | confirmation   |
      | krishna@gmail.com   | password123    | password123     |

  @invalid-email
  Scenario Outline: Validate Registration Fails with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email    | password             | confirmation          |
      | abc@     | ValidPassword123     | ValidPassword123      |

  @invalid-password
  Scenario Outline: User Registration Process with Password Without Lowercase Letters
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain lowercase letters

    Examples:
      | email               | password       | confirmation    |
      | krishna@gmail.com   | PASSWORD123    | PASSWORD123      |

  @empty-password
  Scenario Outline: User Registration Process - Empty Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then the system displays an error message indicating the password field is required
    And the password field is highlighted to indicate the error

    Examples:
      | email              |
      | krishna@gmail.com  |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then the system should display an error message indicating the password must contain uppercase letters
    And the password field should be highlighted to indicate the error

    Examples:
      | email                       | password      | confirmation   |
      | uniqueuser@example.com     | password123   | password123     |

  @empty-email
  Scenario Outline: User Registration Fails with Empty Email
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see an error message indicating the email field is required
    And the email field should be highlighted to indicate the error

    Examples:
      | password          | confirmation       |
      | ValidPassword123  | ValidPassword123    |

  @empty_confirmation_password
  Scenario Outline: User Registration Process - Confirmation Password Field Left Empty
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then the system should display an error message indicating the confirmation password field is required
    And the confirmation password field should be highlighted to indicate the error

    Examples:
      | email               | password           |
      | krishna@gmail.com   | ValidPassword123   |

  @password_mismatch
  Scenario Outline: User Registration Fails with Password Mismatch
    Given the user navigates to the registration page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation_password>" in the confirmation field
    And the user clicks the "Register" button
    Then the system displays an error message indicating the passwords do not match
    And the confirmation password field is highlighted to indicate the error

    Examples:
      | email               | password            | confirmation_password   |
      | krishna@gmail.com   | ValidPassword123    | DifferentPassword456     |

  @short-password-validation
  Scenario Outline: User Registration Fails with Short Password
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password that is shorter than the minimum length "<password>"
    And I enter the confirmation password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password is too short

    Examples:
      | email              | password | confirmation |
      | krishna@gmail.com  | short    | short        |

  @feature_user_registration
  @duplicate_email_registration
  Scenario Outline: User Registration with Already Registered Email
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system should display an error message indicating the email is already in use
    And the email field should be highlighted to indicate the error

    Examples:
      | email               | password            | confirmation       |
      | krishna@gmail.com   | ValidPassword123!   | ValidPassword123!   |

  @invalid-password
  Scenario Outline: User Registration Fails with Long Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password is too long
    And the password field should be highlighted to indicate the error

    Examples:
      | email              | password                                             | confirmation                                          |
      | krishna@gmail.com  | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email Format
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email | password               | confirmation         |
      | abc@  | ValidPassword123!      | ValidPassword123!     |

  @invalid-email-registration
  Scenario Outline: User Registration Fails with Invalid Email Format
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email   | password              | confirmation         |
      | abc@    | ValidPassword123!     | ValidPassword123!     |

  @invalid-email-format
  Scenario Outline: User Registration Fails with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the Register button
    Then the system displays an error message indicating the email format is invalid
    And the email field is highlighted to indicate the error

    Examples:
      | email          | password              | confirmation         |
      | abc@@gmail.com | ValidPassword123!     | ValidPassword123!     |

  @invalid-email
  Scenario Outline: Validate Registration Fails with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid

    Examples:
      | email     | password              | confirmation         |
      | abc@      | ValidPassword123!     | ValidPassword123!     |

@invalid-email
Scenario Outline: Validate Registration Fails with Invalid Email
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I confirm "<confirmation>"
  And I click the "Register" button
  Then the system displays an error message indicating the email format is invalid
  And the email field is highlighted to indicate the error

  Examples:
    | email      | password             | confirmation        |
    | abc@!      | ValidPassword123     | ValidPassword123     |

  @invalid-password
  Scenario Outline: Validate Registration Fails with Password Without Uppercase
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password must contain uppercase letters
    And the password field should be highlighted to indicate the error

    Examples:
      | email                   | password      | confirmation   |
      | uniqueuser@gmail.com    | password123   | password123     |

@invalid-password
Scenario Outline: User Registration Fails with Invalid Password Characters
  Given the user is on the Registration Page
  When I enter a unique email address "<email>"
  And I enter a password that contains invalid characters "<password>"
  And I confirm the password "<confirmation>"
  And I click the "Register" button
  Then an error message should be displayed indicating the password contains invalid characters

  Examples:
    | email                | password     | confirmation   |
    | krishna@gmail.com    | !@#$%^&*     | !@#$%^&*       |
