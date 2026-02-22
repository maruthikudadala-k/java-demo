
@feature_user_registration
Feature: UserRegistrationProcess

  Background: 
    Given the user is on the Registration Page

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then a success message should appear on the screen
    And a confirmation email should be received in the inbox
    And the user should be able to log in using "<email>"

    Examples:
      | email             | password            | confirmation_password  |
      | krishna@gmail.com | ValidPassword123    | ValidPassword123        |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then a success message should be displayed
    And the user should receive a confirmation email
    And the user logs in using the newly created account

    Examples:
      | email                     | password            | confirmation         |
      | uniqueuser@example.com    | validpassword123    | validpassword123      |

@invalid-password-confirmation
Scenario Outline: User Registration Process
  Given the user is on the Registration Page
  When the user enters "<email>" in the email field
  And the user enters "<password>" in the password field
  And the user enters "<confirmation_password>" in the confirmation field
  And the user clicks the Register button
  Then an error message appears indicating password mismatch
  And the account is not created
  And the user remains on the registration page

  Examples:
    | email               | password              | confirmation_password     |
    | krishna@gmail.com   | ValidPassword123      | DifferentPassword456      |

@valid-registration
Scenario Outline: User Registration Process
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then a success message should appear
  And a confirmation email should be received in the inbox
  And the user should be able to log in using the newly created account

  Examples:
    | email                | password              | confirmation          |
    | krishna@gmail.com    | StrongPassword123!    | StrongPassword123!     |

@valid-email
Scenario Outline: Validate Email Format
  When I enter "<email>" in the email field
  Then the email field accepts the unique email format

  Examples:
    | email                |
    | krishna@gmail.com    |

@valid-password
Scenario Outline: Validate Strong Password
  When I enter "<password>" in the password field
  Then the password field accepts the strong password

  Examples:
    | password              |
    | StrongPassword123!    |

@valid-confirmation
Scenario Outline: Validate Password Confirmation
  When I enter "<confirmation>" in the confirmation field
  Then the confirmation field matches the password

  Examples:
    | confirmation          |
    | StrongPassword123!     |

@initiate-registration
Scenario Outline: Initiate Registration Process
  When I click the "Register" button
  Then the registration process is initiated

  Examples:
    | action                |
    | Register             |

@success-message
Scenario Outline: Check Success Message
  Then a success message should appear

  Examples:
    | message               |
    | Registration successful |

@confirmation-email
Scenario Outline: Verify Confirmation Email
  And a confirmation email should be received in the inbox

  Examples:
    | email                |
    | krishna@gmail.com    |

@user-login
Scenario Outline: User Login
  And the user should be able to log in using the newly created account

  Examples:
    | email                | password              |
    | krishna@gmail.com    | StrongPassword123!    |

@valid-registration
Scenario Outline: User Registration Process with Valid Information
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I confirm the password with "<confirmation>"
  And I click the "Register" button
  Then a success message should appear
  And a confirmation email should be received in the inbox
  And I log in using "<email>"

  Examples:
    | email                | password               | confirmation         |
    | krishna@gmail.com    | ValidPassword123!      | ValidPassword123!     |

  @valid-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user should receive a confirmation email
    And the user logs in using the newly created account

    Examples:
      | email                    | password               | confirmation          |
      | user+test@gmail.com     | ValidPassword123!      | ValidPassword123!      |

  @unique-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user should receive a confirmation email
    And the user logs in with "<email>" and "<password>"

    Examples:
      | email                | password               | confirmation         |
      | <email>             | <password>            | <confirmation>       |
      | krishna@gmail.com    | ValidPassword123!      | ValidPassword123!     |

@valid-lowercase-email
Scenario Outline: Validate lowercase email registration
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then a success message should appear
  And a confirmation email should be received in the inbox
  And the user logs in using the newly created account

  Examples:
    | email                | password             | confirmation        |
    | <email>             | <password>          | <confirmation>      |
    | krishna@gmail.com    | ValidPassword123     | ValidPassword123     |

  @valid-email-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    And the user should be able to log in using the new account

    Examples:
      | email                     | password             | confirmation         |
      | user.name@example.com     | SecurePassword123    | SecurePassword123     |

  @valid-registration-with-hyphens
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user should receive a confirmation email
    And the user logs in using the newly created account

    Examples:
      | email                      | password             | confirmation        |
      | <email>                   | <password>          | <confirmation>       |
      | krishna-hyphen@gmail.com   | ValidPassword123     | ValidPassword123     |

  @valid-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then the user should see a success message
    And the user should receive a confirmation email
    And the user logs in using the newly created account

    Examples:
      | email               | password            | confirmation        |
      | user123@gmail.com  | ValidPassword123    | ValidPassword123     |

  @valid-uppercase-email
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then a success message should be displayed
    And a confirmation email should be received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                | password            | confirmation        |
      | KRISHNA@GMAIL.COM   | ValidPassword123    | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>"
    And the user enters "<password>"
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user should receive a confirmation email
    And the user logs in using the newly created account

    Examples:
      | email                      | password              | confirmation         |
      | krishna_smith@gmail.com   | ValidPassword123      | ValidPassword123      |

  @valid-email-plus-sign
  Scenario Outline: Validate that the system accepts an email address containing a plus sign during registration
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then I should see a success message
    And I should receive a confirmation email in the inbox
    And I log in using the newly created account

    Examples:
      | email                     | password            | confirmation         |
      | krishna+test@gmail.com    | ValidPassword123    | ValidPassword123      |

  @valid-email-registration
  Scenario Outline: User Registration Process with Valid Email
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then a success message appears indicating account creation
    And the user receives a confirmation email in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                 | password            | confirmation        |
      | krishna@gmail.com     | ValidPassword123    | ValidPassword123     |

  Scenario Outline: User Registration with Valid Email
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the "Register" button
    Then a success message is displayed
    And a confirmation email is received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email               | password            |
      | user123@gmail.com   | ValidPassword123    |

  @long-domain-email-registration
  Scenario Outline: Validate Email with Long Domain During Registration
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the "Register" button
    Then a success message should appear on the screen
    And a confirmation email should be received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                   | password              | confirmation        |
      | user@longdomainname.com | ValidPassword123!     | ValidPassword123!    |

@valid-email-registration
Scenario Outline: User Registration Process
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And the user clicks the Register button
  Then a success message should appear indicating account creation
  And a confirmation email should be received in the inbox
  And the user should be logged in using the newly created account

  Examples:
    | email           | password  | confirmation |
    | a@example.com   | Pass123   | Pass123      |
    | b@example.com   | Admin321  | Admin321     |

  @valid-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                   | password             | confirmation        |
      | user..name@gmail.com   | ValidPassword123!    | ValidPassword123!    |

  @valid-email-registration
  Scenario Outline: User Registration Process with Generic Top-Level Domain
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    And the user should be able to log in using the newly created account

    Examples:
      | email            | password            | confirmation_password  |
      | user@example     | ValidPassword123    | ValidPassword123       |

  @valid_email_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a valid email address "<email>"
    And I enter a valid password "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the "Register" button
    Then a success message should appear on the screen
    And the user should receive a confirmation email in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                    | password            | confirmation         |
      | user123@123domain.com   | SecurePassword123   | SecurePassword123     |

  @long-username-registration
  Scenario Outline: User Registration Process with Long Username
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm "<confirmation_password>"
    And I click the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                        | password               | confirmation_password      |
      | longusername@example.com     | ValidPassword123!      | ValidPassword123!          |

  @valid-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters a valid email address "<email>"
    And the user enters a valid password "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user receives a confirmation email in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                | password              | confirmation         |
      | krishna@gmail.co    | ValidPassword123      | ValidPassword123      |

@valid-email-registration
Scenario Outline: User Registration Process with Subdomain Email
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I enter "<confirmation>"
  And I click the "Register" button
  Then the user should see a success message
  And the user should receive a confirmation email
  And the user logs in using "<email>"

  Examples:
    | email                     | password            | confirmation        |
    | user@mail.example.com    | ValidPassword123    | ValidPassword123     |

  @valid-email-registration
  Scenario Outline: User Registration Process with Short Domain Email
    Given the user is on the Registration Page
    When I enter a valid email address "<email>"
    And I enter a valid password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    And the user should be logged in using the newly created account

    Examples:
      | email         | password            | confirmation        |
      | user@a.com   | ValidPassword123    | ValidPassword123     |

  @valid-email-trim
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation_password>"
    And I click the Register button
    Then I should see a success message
    And I should receive a confirmation email in the inbox
    And I log in using the account created

    Examples:
      | email                  | password            | confirmation_password  |
      | krishna@gmail.com      | validPassword123    | validPassword123        |

  Scenario Outline: User Registration with Mixed Case Email
    When the user enters a valid email address "<email>" in the email field
    And the user enters a valid password "<password>" in the password field
    And the user confirms the password "<confirmation_password>" in the confirmation field
    And the user clicks the "Register" button
    Then a success message is displayed
    And a confirmation email is received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                | password              | confirmation_password   |
      | KrIsHnA@gmail.com   | ValidPassword123!     | ValidPassword123!       |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the Register button
    Then a success message appears
    And a confirmation email is received
    And the user logs in with "<email>" and "<password>"

    Examples:
      | email      | password           | confirmation       |
      | a@b.com    | ValidPassword123   | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation_password>"
    And I click the "Register" button
    Then the user should see a success message
    And the user should receive a confirmation email
    And the user logs in using "<email>"

    Examples:
      | email                  | password           | confirmation_password |
      | user!name@gmail.com   | ValidPassword123   | ValidPassword123      |

  @validate_special_characters
  Scenario Outline: User Registration Process
    When I enter "<email>"
    And I enter "<password>"
    And I confirm "<confirmation_password>"
    And I click the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email                  | password             | confirmation_password   |
      | user!name@domain.com  | ValidPassword123!    | ValidPassword123!       |

@valid-email-registration
Scenario Outline: User Registration Process with Valid Email
  Given the user is on the Registration Page
  When I enter a valid email address "<email>"
  And I enter "<password>" in the password field
  And I confirm the password "<confirmation>"
  And I click the Register button
  Then a success message should appear
  And a confirmation email should be received in the inbox
  And the user logs in using the newly created account

  Examples:
    | email              | password              | confirmation         |
    | user!@example.com  | ValidPassword123      | ValidPassword123      |

  @valid_registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    When I log in using the newly created account
    Then the user should be successfully logged in to the platform

    Examples:
      | email             | password            | confirmation         |
      | "!@example.com"   | "ValidPassword123"  | "ValidPassword123"    |

  @duplicate-email-registration
  Scenario Outline: User Registration Process with Existing Email
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email is already in use
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email               | password            | confirmation        |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123     |

  @invalid-email-registration
  Scenario Outline: User Registration with Improperly Formatted Email Address
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm the password with "<confirmation>"
    And I click the "Register" button
    Then an error message should indicate invalid email format
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email   | password           | confirmation        |
      | abc@    | ValidPassword123   | ValidPassword123     |

  @empty_email_registration
  Scenario Outline: User Registration with Empty Email Field
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the Register button
    Then an error message should appear indicating the email field is required
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | password           |
      | ValidPassword123   |

  @empty_confirmation_password
  Scenario Outline: User Registration Process with Empty Confirmation Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then an error message should appear indicating the confirmation password field is required
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email              | password            |
      | krishna@gmail.com  | ValidPassword123    |

  @invalid-password-length
  Scenario Outline: User Registration Process with Short Password
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the password does not meet the minimum length requirement
    And the account should not be created
    And the user should remain on the registration page

    Examples:
      | email              | password | confirmation |
      | krishna@gmail.com  | short    | short        |

  @empty-password
  Scenario Outline: User Registration Process with Empty Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I leave the password field empty
    And I enter "<confirmation_password>" in the confirmation field
    And I click the Register button
    Then an error message should appear indicating the password field is required
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email               | confirmation_password |
      | krishna@gmail.com   |                      |

  Scenario Outline: Verify that the system prevents registration when the email address contains spaces
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the "Register" button
    Then an error message appears indicating spaces are not allowed in the email
    And the account is not created
    And the user remains on the registration page

    Examples:
      | email            | password            | confirmation        |
      | abc @gmail.com   | ValidPassword123    | ValidPassword123     |

  @invalid-email-registration
  Scenario Outline: User Registration Process with Invalid Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating invalid characters in the email
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email   | password              | confirmation         |
      | abc@    | ValidPassword123!     | ValidPassword123!     |

  @invalid-password
  Scenario Outline: User Registration Process with Weak Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should appear indicating the password does not meet complexity requirements
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email              | password | confirmation |
      | krishna@gmail.com  | 12345    | 12345        |

  @password_mismatch
  Scenario Outline: User Registration Process with Mismatched Passwords
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then an error message should appear indicating password mismatch
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email               | password            | confirmation_password  |
      | krishna@gmail.com   | ValidPassword123    | DifferentPassword456    |

  @invalid-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then the user should see an error message
    And the account should not be created
    And the user remains on the Registration Page

    Examples:
      | email            | password               | confirmation          |
      | abc@@example.com | ValidPassword123!      | ValidPassword123!      |

  @invalid-email-domain
  Scenario Outline: User Registration with Invalid Email Domain
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the "Register" button
    Then an error message should be displayed indicating the domain is invalid
    And the account should not be created
    And the user should remain on the Registration Page

    Examples:
      | email   | password              | confirmation         |
      | abc@    | ValidPassword123      | ValidPassword123      |

  @max-special-characters
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation_password>"
    And I click the "Register" button
    Then a success message should appear
    And I verify that a confirmation email is received in the inbox
    And I log in using the newly created account with email "<email>" and password "<password>"

    Examples:
      | email                          | password               | confirmation_password    |
      | a!@#$%^&*()_+[]{}|;':,.<>?~   | ValidPassword123!      | ValidPassword123!        |

  @max-length-password-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password
    And the user clicks the "Register" button
    Then a success message should appear indicating account creation
    And a confirmation email should be received in the inbox
    And the user logs in using the newly created account

    Examples:
      | email              | password                     |
      | krishna@gmail.com  | <maximum_length_password>    |

  @max_length_email_registration
  Scenario Outline: Verify User Registration with Maximum Length Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then a success message should appear
    And a confirmation email should be received in the inbox
    And the user should be logged in using the newly created account

    Examples:
      | email                                                                                                                                                                                                 | password            | confirmation        |
      | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | ValidPassword123! | ValidPassword123!  |

  @max_length_password_registration
  Scenario Outline: User Registration Process with Maximum Length Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then a success message should appear indicating account creation
    And a confirmation email should be received in the inbox
    And the user should be able to log in using the newly created account

    Examples:
      | email              | password                      | confirmation                  |
      | krishna@gmail.com  | aaaaaaaaaaaaaaaaaaaaaaaaaaaa... | aaaaaaaaaaaaaaaaaaaaaaaaaaaa... |

  @concurrent-registration
  Scenario Outline: User Registration Process
    Given the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    When the user clicks the "Register" button in one browser
    And the user clicks the "Register" button in another browser
    Then the user should see a success message on one screen
    And the user should see an error message on the other screen
    And only one account should be created in the database
    And the user logs in using the newly created account

    Examples:
      | email                | password             | confirmation        |
      | krishna@gmail.com    | ValidPassword123     | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm "<confirmation>"
    And I click the "Register" button
    Then the account is created successfully
    And a confirmation email is received in the inbox

    Examples:
      | email              | password            | confirmation       |
      | krishna@gmail.com  | ValidPassword123    | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password that meets the minimum length requirement "<password>"
    And I confirm the password "<confirm_password>"
    And I click the "Register" button
    Then a success message indicating account creation should be displayed

    Examples:
      | email                | password           | confirm_password     |
      | krishna@gmail.com    | validPassword123   | validPassword123     |

  @unique-email-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters a unique email address in the email field
    And the user enters a valid password in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                | password            | confirmation         |
      | <email>             | <password>         | <confirmation>       |
      | krishna@gmail.com    | ValidPassword123!   | ValidPassword123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then the account should be created successfully
    And a confirmation email should be received in the inbox

    Examples:
      | email               | password              | confirmation         |
      | <email>            | <password>           | <confirmation>        |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @valid-registration
  Scenario Outline: User Registration Process
    When the user enters a unique email address "<email>"
    And the user enters a password that includes special characters "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message

    Examples:
      | email                     | password    | confirmation |
      | uniqueuser@example.com    | P@ssw0rd!   | P@ssw0rd!    |

  @valid-registration-uppercase
  Scenario Outline: User Registration with Uppercase Letters in Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email               | password      | confirmation   |
      | krishna@gmail.com   | Password123   | Password123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | email               | password      | confirmation   |
      | krishna@gmail.com   | Password123   | Password123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see the success message displayed on the screen

    Examples:
      | email              | password      | confirmation   |
      | krishna@gmail.com  | password123   | password123     |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email              | password             | confirmation        |
      | krishna@gmail.com  | ValidPassword123     | ValidPassword123     |

  @validate_registration_with_spaces
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                        | password           | confirmation        |
      | unique_email@example.com     | validPassword123   | validPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see a success message confirming account creation

    Examples:
      | email                   | password                  | confirmation             |
      | usuario@ejemplo.com    | contraseñaSegura123      | contraseñaSegura123      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password in the confirmation field
    And I click the "Register" button
    Then a success message should be displayed confirming account creation

    Examples:
      | email                    | password                   |
      | user name@example.com    | password with spaces       |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                      | password        | confirmation_password |
      | unique_user@example.com    | Password123!    | Password123!          |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email                   | password      | confirmation  |
      | UserEmail@Example.com   | Password123   | Password123    |

  @invalid-email_password
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address with no characters in the email field
    And I enter a password with no characters in the password field
    And I confirm the password in the confirmation field
    And I click the "Register" button
    Then the user should see a success message displayed on the screen

    Examples:
      | email | password | confirmation |
      |      |          |              |

  @valid-registration-repeated-characters
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see the success message displayed on the screen

    Examples:
      | email               | password | confirmation |
      | aabbcc@gmail.com    | pppppp   | pppppp       |

  @whitespace-registration
  Scenario Outline: User Registration Process with Whitespace Inputs
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email | password | confirmation_password |
      |       |          |                      |

  @valid-registration-single-character
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email | password | confirmation |
      | a     | b       | b             |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email                 | password         | confirmation      |
      | krishnaaa@gmail.com   | passsword123     | passsword123      |

  Scenario Outline: User can register with valid email and password
    When I enter a unique email address "<email>" in the email field
    And I enter a password "<password>" in the password field
    And I confirm the password "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then I should see the success message displayed on the screen

    Examples:
      | email     | password | confirmation_password |
      | a@b.com   | abcd     | abcd                  |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email | password | confirmation |
      | abcde | 12345    | 12345        |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters a unique email address with three characters "<email>"
    And the user enters a password with three characters "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the Register button
    Then the user should see a success message

    Examples:
      | email | password | confirmation |
      | abc   | xyz     | xyz          |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter a unique email address with two characters "<email>"
    And I enter a password with two characters "<password>"
    And I confirm the password "<confirmation_password>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email | password | confirmation_password |
      | ab    | xy      | xy                    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email       | password | confirmation |
      | abc@d.com  | 123456   | 123456       |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the user should see a success message

    Examples:
      | email             | password       | confirmation   |
      | uniqueemail1     | securepass1    | securepass1     |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email         | password      | confirmation  |
      | abcdefghijk   | abcdefghijk   | abcdefghijk   |

@valid-registration
Scenario Outline: User Registration Process
  When I enter a unique email address "<email>"
  And I enter a password "<password>"
  And I confirm the password "<confirmation>"
  And I click the "Register" button
  Then the success message should be displayed

  Examples:
    | email       | password   | confirmation |
    | user@a.com  | password1  | password1    |

@valid-email
Scenario Outline: Enter Unique Email Address
  When I enter a unique email address "<email>"
  Then the email field accepts the unique email format with eight characters

  Examples:
    | email       |
    | user@a.com  |

@valid-password
Scenario Outline: Enter Valid Password
  When I enter a password "<password>"
  Then the password field accepts the valid password with eight characters

  Examples:
    | password   |
    | password1  |

@valid-confirmation
Scenario Outline: Confirm Password
  When I confirm the password "<confirmation>"
  Then the confirmation field matches the password

  Examples:
    | confirmation |
    | password1    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email       | password    | confirmation |
      | abcdefghij  | abcdefghij  | abcdefghij   |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email              | password          | confirmation         |
      | abcd1234efghij    | password12345678  | password12345678     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email          | password | confirmation |
      | abcde@f.com   | abcdefg  | abcdefg      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the user should see a success message

    Examples:
      | email          | password      | confirmation  |
      | abcd@xyz.com   | password123   | password123    |
      | uniqueemail12  | password12345 | password12345  |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message

    Examples:
      | email               | password            | confirmation        |
      | abcdefgh12345678   | abcdefgh12345678   | abcdefgh12345678    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I enter a password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email       | password   | confirmation |
      | abcdefghi   | abcdefgh   | abcdefgh     |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email               | password            | confirmation        |
      | abcdefghijklmno     | abcdefghijklmno     | abcdefghijklmno     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter a unique email address with nineteen characters "<email>"
    And I enter a password with nineteen characters "<password>"
    And I confirm the password in the confirmation field "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                     | password                    | confirmation               |
      | abcdefghijklmnoqrst      | abcdefghijklmnoqrst        | abcdefghijklmnoqrst       |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter a unique email address with eighteen characters "<email>"
    And I enter a password with eighteen characters "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the success message should be displayed

    Examples:
      | email                    | password                 | confirmation             |
      | abcdefghijklmno123      | abcdefghijklmno123      | abcdefghijklmno123      |

  @duplicate-email
  Scenario Outline: User Registration Process with Already Registered Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then the user should see an error message indicating that the email is already in use

    Examples:
      | email                | password            | confirmation        |
      | krishna@gmail.com    | ValidPassword123    | ValidPassword123     |

  @invalid-email-registration
  Scenario Outline: User Registration with Improperly Formatted Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the error message should indicate that the email format is invalid

    Examples:
      | email   | password            | confirmation        |
      | abc@    | ValidPassword123    | ValidPassword123     |

  @valid_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm the password in the confirmation field
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                | password            |
      | abcdefghijklmno.p    | abcdefghijklmno.p   |

  @empty-email
  Scenario Outline: User Registration Process with Empty Email
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the Register button
    Then the user should see an error message indicating that the email is required

    Examples:
      | password             |
      | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then a success message should be displayed

    Examples:
      | email                    | password                   | confirmation               |
      | abcdefghij1234567890    | abcdefghij1234567890      | abcdefghij1234567890      |

  @empty-password
  Scenario Outline: User Registration Process with Empty Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I leave the password field empty
    And I confirm the password in the confirmation field
    And I click the "Register" button
    Then the user should see an error message indicating that the password is required

    Examples:
      | email                |
      | krishna@gmail.com    |

  @empty_confirmation_field
  Scenario Outline: User Registration Process with Empty Password Confirmation
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I leave the confirmation field empty
    And I click the "Register" button
    Then the system should display an error message indicating that the password confirmation is required

    Examples:
      | email               | password            |
      | krishna@gmail.com   | ValidPassword123    |

@password_mismatch
Scenario Outline: User Registration with Non-Matching Passwords
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I enter "<confirmation>"
  And I click the "Register" button
  Then the user should see an error message indicating that the passwords do not match

  Examples:
    | email                | password              | confirmation         |
    | krishna@gmail.com    | ValidPassword123      | DifferentPassword456  |

  @invalid-password
  Scenario Outline: User Registration with Short Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the "Register" button
    Then the error message "The password does not meet the minimum length requirement." should be displayed

    Examples:
      | email              | password | confirmation |
      | krishna@gmail.com  | short    | short        |

  @invalid-email-registration
  Scenario Outline: User Registration Process with Consecutive Special Characters in Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the user should see an error message indicating that the email cannot contain consecutive special characters

    Examples:
      | email             | password               | confirmation         |
      | abc@@gmail.com    | ValidPassword123!      | ValidPassword123!     |

@invalid-email-registration
Scenario Outline: User Registration with Invalid Email
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I confirm the password "<confirmation>"
  And the user clicks the Register button
  Then the user should see the error message indicating invalid characters in the email

  Examples:
    | email            | password            | confirmation       |
    | abc@!#$%^&*     | ValidPassword123    | ValidPassword123    |

  @invalid-email-registration
  Scenario Outline: User Registration with Email Containing Spaces
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the Register button
    Then the error message "Email cannot contain spaces" should be displayed

    Examples:
      | email            | password            | confirmation        |
      | abc @gmail.com  | ValidPassword123    | ValidPassword123     |

  @long_password_error
  Scenario Outline: User Registration Process with Long Password
    Given the user is on the Registration Page
    When I enter a valid email address "<email>"
    And I enter a password longer than the maximum length "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the user should see the error message indicating that the password exceeds the maximum length requirement

    Examples:
      | email              | password                   | confirmation               |
      | krishna@gmail.com  | aaaaaaaaaaaaaaaaaaaaaaaaaa... | aaaaaaaaaaaaaaaaaaaaaaaaaa... |

  @mixed_case_email_error
  Scenario Outline: User Registration Process with Mixed Case Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then the error message should indicate that the email cannot contain mixed case letters

    Examples:
      | email                     | password             | confirmation         |
      | KrIsHnA@Gmail.com        | ValidPassword123     | ValidPassword123     |

  @invalid-email-registration
  Scenario Outline: User Registration Process with Invalid Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password with "<confirmation>"
    And I click the "Register" button
    Then the user should see an error message indicating that the email cannot contain repeated characters

    Examples:
      | email            | password              | confirmation          |
      | aa@domain.com    | ValidPassword123!     | ValidPassword123!     |

  @invalid-email-spacing
  Scenario Outline: User Registration with Email Address Containing Spaces
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see an error message indicating that the email cannot contain leading or trailing spaces

    Examples:
      | email              | password             | confirmation        |
      | " abc@gmail.com " | ValidPassword123!    | ValidPassword123!    |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see the error message "The email must contain more than two characters."

    Examples:
      | email | password              | confirmation          |
      | ab    | ValidPassword123!     | ValidPassword123!     |

  @whitespace-email-registration
  Scenario Outline: User Registration Process with Whitespace Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the Register button
    Then the user should see the error message indicating that the email cannot contain only whitespace

    Examples:
      | email | password              | confirmation         |
      | " "   | ValidPassword123      | ValidPassword123      |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then the error message "Email must contain more than four characters" should be displayed

    Examples:
      | email  | password            | confirmation       |
      | abcd   | ValidPassword123    | ValidPassword123    |

@invalid-email-length
Scenario Outline: User Registration Process
  Given the user is on the Registration Page
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then the registration should not be completed
  And an error message should be displayed indicating that the email must contain more than three characters

  Examples:
    | email | password             | confirmation        |
    | abc   | ValidPassword123     | ValidPassword123    |

  @invalid-email
  Scenario Outline: Verify Registration with Six Character Email Address
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then the user should see an error message indicating that the email must contain more than six characters

    Examples:
      | email    | password              | confirmation         |
      | abcdef   | ValidPassword123      | ValidPassword123     |

  @invalid-email-registration
  Scenario Outline: User Registration with Single Character Email
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the error message "The email must contain more than one character" should be displayed

    Examples:
      | email | password            | confirmation         |
      | a     | ValidPassword123    | ValidPassword123      |

  @invalid-email-registration
  Scenario Outline: User Registration with Invalid Email Length
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I confirm the password with "<confirmation>"
    And I click the "Register" button
    Then the user should see the error message indicating that the email must contain more than five characters

    Examples:
      | email     | password              | confirmation         |
      | abc@      | validPassword123      | validPassword123      |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the "Register" button
    Then the user should see the success message displayed on the screen

    Examples:
      | email              | password     |
      | krishna@gmail.com  | Password123  |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system creates the account and redirects to a success page
    And the user checks the inbox for a confirmation email

    Examples:
      | email               | password            | confirmation        |
      | krishna@gmail.com   | ValidPassword123!   | ValidPassword123!    |

@successful-registration
Scenario Outline: User Registration Process
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I enter "<confirmation_password>"
  And I click the "Register" button
  Then the success message should be displayed
  And the user should be redirected to the login page

  Examples:
    | email              | password             | confirmation_password  |
    | krishna@gmail.com  | ValidPassword123     | ValidPassword123        |

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

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message displayed on the screen
    And the user is redirected to the login page

    Examples:
      | email              | password             | confirmation        |
      | krishna@gmail.com  | ValidPassword123     | ValidPassword123     |

  @valid-registration
  Scenario Outline: User Registration Process
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user enters "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should see the success message displayed on the screen
    And the user should be redirected to the login page

    Examples:
      | email               | password            | confirmation       |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123    |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirm_password>"
    And I click the "Register" button
    Then the success message should be displayed on the screen

    Examples:
      | email                    | password        | confirm_password |
      | uniqueuser@example.com   | Password123!    | Password123!     |

  @valid-email
  Scenario Outline: Validate Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    Then the email field accepts the unique email format

    Examples:
      | email                    |
      | uniqueuser@example.com   |

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
    When I confirm the password "<confirm_password>"
    Then the confirmation field matches the password

    Examples:
      | confirm_password |
      | Password123!     |

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
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then I should see a success message displayed on the screen
    And I should be redirected to the login page

    Examples:
      | email               | password                                                                 | confirmation                                                            |
      | krishna@gmail.com   | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @valid-registration
  Scenario Outline: User Registration Process
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see a success message
    And the user should be redirected to the login page

    Examples:
      | email               | password          | confirmation      |
      | krishna@gmail.com   | Password123!      | Password123!      |

  @valid-registration
  Scenario Outline: Validate User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then the user should see a success message displayed on the screen
    And the user should be redirected to the login page

    Examples:
      | email               | password             | confirmation_password  |
      | krishna@gmail.com   | SecurePassword123    | SecurePassword123       |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    Then the email field is visible and accessible
    And the password field is visible and accessible
    And the confirmation password field is visible and accessible
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email               | password            |
      | krishna@gmail.com   | ValidPassword123    |

  @feature_user_registration
  @validate_fields_enabled
  Scenario Outline: User Registration Process
    When I navigate to the registration page
    Then the registration page loads successfully
    And the email field is enabled and can be interacted with
    And the password field is enabled and can be interacted with
    And the confirmation password field is enabled and can be interacted with
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email               | password             |
      | krishna@gmail.com   | ValidPassword123     |

  @validate_labels
  Scenario Outline: User Registration Process
    When I verify that the email field has a visible label
    And I verify that the password field has a visible label
    And I verify that the confirmation password field has a visible label
    And I enter a unique email address "<email>"
    And I enter a valid password "<password>"
    Then the registration should be successful

    Examples:
      | email             | password          |
      | krishna@gmail.com | ValidPassword123   |

  @focus-accessibility
  Scenario Outline: User Registration Process
    When I use the Tab key to focus on the email field
    And I enter "<email>" in the email field
    When I use the Tab key to focus on the password field
    And I enter "<password>" in the password field
    When I use the Tab key to focus on the confirmation password field
    And I enter "<confirmation_password>" in the confirmation password field
    Then the user should be able to register successfully

    Examples:
      | email               | password            | confirmation_password |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123      |

  @accessible_fields
  Scenario Outline: User Registration Process
    Given the user navigates to the registration page using a screen reader
    Then the screen reader announces the email field correctly
    And the screen reader announces the password field correctly
    And the screen reader announces the confirmation password field correctly
    When the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    Then the email field accepts the unique email format
    And the password field accepts the valid password

    Examples:
      | email               | password            | confirmation password  |
      | krishna@gmail.com   | ValidPassword123    | ValidPassword123        |

  @valid_registration_touch_device
  Scenario Outline: User Registration Process
    Given the user opens the registration page on a touch device
    When the user taps on the email field to focus
    And the user enters "<email>" in the email field
    And the user taps on the password field to focus
    And the user enters "<password>" in the password field
    And the user taps on the confirmation password field to focus
    Then the registration page should load successfully on touch devices

    Examples:
      | email                | password              |
      | krishna@gmail.com    | ValidPassword123      |

  Scenario Outline: Validate tooltip information for registration fields
    When the user hovers over the <field> field
    Then the tooltip for the <field> field provides information about <tooltip_info>
    
    Examples:
      | field                     | tooltip_info                          |
      | email                     | valid email format                    |
      | password                  | password requirements                 |
      | confirmation password     | matching passwords                    |

  Scenario Outline: Validate error messages for empty fields
    When the user clicks the "Register" button without filling in the fields
    Then the system displays error messages for empty fields

    Examples:
      | field                     |
      | email                     |
      | password                  |
      | confirmation password     |

  Scenario: Verify tooltips disappear after interaction
    When the user interacts with the fields
    Then the tooltips disappear after the user interacts with the fields

  Scenario Outline: Validate password character limit
    Given the user enters a unique email address "<email>"
    When the user enters a password that exceeds the maximum length "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then an error message is displayed indicating the password exceeds the maximum length
    And the password field is highlighted to indicate the error

    Examples:
      | email               | password            | confirmation        |
      | krishna@gmail.com   | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  Scenario Outline: Validate email and password retention after registration error
    When the user enters an invalid email address "<email>" in the email field
    And the user enters a valid password "<password>" in the password field
    And the user confirms the password "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system displays an error message for the invalid email
    And the email field retains the value "<email>"
    And the password field retains the value "<password>"

    Examples:
      | email   | password            | confirmation         |
      | abc@    | ValidPassword123    | ValidPassword123      |

  Scenario Outline: Validate that the registration page shows an error message when the email and password fields are empty
    Given I leave the email field empty
    And I leave the password field empty
    And I leave the confirmation password field empty
    When I click the "Register" button
    Then I should see error messages for all empty fields
    And the email field should be highlighted to indicate the error
    And the password field should be highlighted to indicate the error

    Examples:
      | email | password | confirmation_password |
      |      |          |                       |

  @valid-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then a loading indicator should be displayed while the registration request is processed
    And the loading indicator should disappear after the registration is complete

    Examples:
      | email              | password            | confirmation        |
      | krishna@gmail.com  | ValidPassword123!   | ValidPassword123!    |

  @validate_email_password_accessibility
  Scenario Outline: User Registration Process
    When the user verifies that the email field is visible and accessible
    And the user verifies that the password field is visible and accessible
    And the user verifies that the confirmation password field is visible and accessible
    And the user enters a unique email address "<email>"
    And the user enters a valid password "<password>"
    Then the user should be able to register successfully

    Examples:
      | email              | password           |
      | krishna@gmail.com  | ValidPassword123   |

  @valid-registration
  Scenario Outline: User Registration Process
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the email and password fields should be cleared after successful registration

    Examples:
      | email               | password             | confirmation         |
      | krishna@gmail.com   | ValidPassword123     | ValidPassword123      |

  @successful-registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When the user enters a unique email address "<email>"
    And the user enters a valid password "<password>"
    And the user confirms the password "<confirmation>"
    And the user clicks the "Register" button
    Then the system creates the account and displays a success message
    And the user is redirected to the login page

    Examples:
      | email               | password              | confirmation        |
      | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!    |

  @successful_registration
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation_password>" in the confirmation field
    And the user clicks the "Register" button
    Then the user should be redirected to a success page
    And the user should receive a confirmation email in the inbox

    Examples:
      | email                | password               | confirmation_password   |
      | krishna@gmail.com    | ValidPassword123!      | ValidPassword123!       |

  @validate_required_fields
  Scenario Outline: User Registration Process
    When the user verifies that the email field has a required field indicator
    And the user verifies that the password field has a required field indicator
    And the user verifies that the confirmation password field has a required field indicator
    And the user clicks the "Register" button without filling in the fields
    Then the system displays the required field indicators correctly
    And the required field indicators remain visible until the fields are filled

    Examples:
      | <email_field_required> | <password_field_required> | <confirmation_password_field_required> | <error_messages_displayed> |
      | Yes                    | Yes                       | Yes                                    | Yes                        |

@short-password-error
Scenario Outline: User Registration Process
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And I click the "Register" button
  Then an error message should be displayed indicating the password is too short
  And the password field should be highlighted to indicate the error

  Examples:
    | email              | password | confirmation |
    | krishna@gmail.com  | short    | short        |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email         | password              | confirmation          |
      | abc@          | ValidPassword123!     | ValidPassword123!     |

  @registration-error
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the "Register" button
    Then the system displays an error message indicating the email is already in use
    And the email field is highlighted to indicate the error

    Examples:
      | email              | password            | confirmation        |
      | krishna@gmail.com  | ValidPassword123    | ValidPassword123     |

  @invalid-password
  Scenario Outline: User Registration Process with Invalid Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password contains invalid characters

    Examples:
      | email              | password      | confirmation   |
      | krishna@gmail.com  | !@#$%^&*      | !@#$%^&*       |

  @empty_email
  Scenario Outline: User Registration Process with Empty Email Field
    Given the user is on the Registration Page
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms the password in the confirmation field
    And the user clicks the Register button
    Then the user should see an error message indicating the email field is required
    And the email field should be highlighted to indicate the error

    Examples:
      | password            |
      | ValidPassword123    |

  @empty-password-error
  Scenario Outline: User Registration Process with Empty Password
    Given the user enters "<email>" in the email field
    When the user leaves the password field empty
    And the user leaves the confirmation password field empty
    And the user clicks the "Register" button
    Then the system displays an error message indicating the password field is required
    And the password field is highlighted to indicate the error

    Examples:
      | email              |
      | krishna@gmail.com  |

  @invalid-email-format
  Scenario Outline: Validate Error Message for Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And the user clicks the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email  | password                | confirmation          |
      | abc@   | ValidPassword123!       | ValidPassword123!     |

  @long_password_error
  Scenario Outline: Validate Registration with Long Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password with "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password is too long

    Examples:
      | email               | password                | confirmation           |
      | krishna@gmail.com   | aaaaaaaaaaaaaaaaaaaaa...| aaaaaaaaaaaaaaaaaaaaa...|

  @empty_confirmation_password
  Scenario Outline: User Registration Process with Empty Confirmation Password
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I leave the confirmation password field empty
    And the user clicks the "Register" button
    Then the system displays an error message indicating the confirmation password field is required
    And the confirmation password field is highlighted to indicate the error

    Examples:
      | email                | password            |
      | krishna@gmail.com    | ValidPassword123    |

  @invalid-email-domain
  Scenario Outline: Validate Email with Invalid Domain
    Given the user enters "<email>" in the email field
    And the user enters "<password>" in the password field
    And the user confirms the password matches "<confirmation>"
    When the user clicks the "Register" button
    Then the user should see an error message indicating the email domain is invalid

    Examples:
      | email   | password              | confirmation           |
      | abc@    | ValidPassword123!     | ValidPassword123!      |

  @invalid-email-format
  Scenario Outline: User Registration with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email              | password                | confirmation          |
      | abc@@example.com   | ValidPassword123!       | ValidPassword123!      |

@password_mismatch
Scenario Outline: User Registration Process with Mismatched Passwords
  Given the user is on the Registration Page
  When I enter "<email>"
  And I enter "<password>"
  And I enter "<confirmation_password>"
  And I click the "Register" button
  Then an error message should be displayed indicating the passwords do not match
  And the confirmation password field should be highlighted to indicate the error

  Examples:
    | email              | password            | confirmation_password     |
    | krishna@gmail.com  | ValidPassword123    | DifferentPassword456      |

  @invalid-email
  Scenario Outline: User Registration with Invalid Email Format
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the Register button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email      | password              | confirmation         |
      | abc@!      | ValidPassword123      | ValidPassword123     |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email
    When I enter an email address "<email>"
    And I enter a valid password "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then the system displays an error message for the invalid email format
    And the email field is highlighted to indicate the error

    Examples:
      | email  | password              | confirmation         |
      | abc@   | ValidPassword123!     | ValidPassword123!     |

  Scenario Outline: Validate that the registration page shows an error message when a password without special characters is entered
    When I enter a unique email address "<email>" in the email field
    And I enter a password without special characters "<password>" in the password field
    And I confirm the password "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then I should see an error message indicating the password must contain special characters
    And the password field should be highlighted to indicate the error

  Examples:
    | email               | password       | confirmation    |
    | krishna@gmail.com   | password123    | password123      |

  Scenario Outline: Validate that the registration fails when the confirmation password field is left empty
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then I should see an error message indicating the confirmation password field is required
    And the confirmation password field should be highlighted to indicate the error

    Examples:
      | email               | password            |
      | krishna@gmail.com   | ValidPassword123    |

  Scenario Outline: Validate that the registration fails when the password and confirmation password do not match
    When I enter a unique email address "<email>" in the email field
    And I enter a valid password "<password>" in the password field
    And I enter a different password "<confirmation_password>" in the confirmation field
    And I click the "Register" button
    Then I should see an error message indicating the passwords do not match
    And the confirmation password field should be highlighted to indicate the error

    Examples:
      | email               | password            | confirmation_password   |
      | krishna@gmail.com   | ValidPassword123    | DifferentPassword456     |

  @password-error
  Scenario Outline: User Registration Process with Password Without Uppercase
    Given the user is on the Registration Page
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And the user clicks the "Register" button
    Then the system displays an error message indicating the password must contain uppercase letters
    And the password field is highlighted to indicate the error

    Examples:
      | email                      | password      | confirmation  |
      | uniqueuser@example.com     | password123   | password123    |

@duplicate-email-registration
Scenario Outline: User Registration Process with Already Registered Email
  Given the user is on the Registration Page
  When I enter "<email>" in the email field
  And I enter "<password>" in the password field
  And I enter "<confirmation>" in the confirmation field
  And the user clicks the "Register" button
  Then an error message should be displayed indicating the email is already in use

  Examples:
    | email               | password              | confirmation         |
    | krishna@gmail.com   | ValidPassword123!     | ValidPassword123!     |

  @empty_email
  Scenario Outline: User Registration Process with Empty Email
    When the user leaves the email field empty
    And the user enters "<password>" in the password field
    And the user confirms the password with "<confirmation>"
    And the user clicks the "Register" button
    Then the user should see an error message indicating the email field is required
    And the email field should be highlighted to indicate the error

    Examples:
      | password          | confirmation      |
      | ValidPassword123 | ValidPassword123  |

  @invalid-password
  Scenario Outline: User Registration Process with Password Without Numbers
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the Register button
    Then the system should display an error message indicating the password must contain numbers
    And the password field should be highlighted to indicate the error

    Examples:
      | email                | password                  | confirmation                |
      | krishna@gmail.com    | passwordwithoutnumbers    | passwordwithoutnumbers       |

@invalid-password
Scenario Outline: User Registration Process with Password Without Lowercase Letters
  Given the user is on the Registration Page
  When the user enters "<email>" in the email field
  And the user enters "<password>" in the password field
  And the user confirms the password with "<confirmation>"
  And the user clicks the Register button
  Then the system displays an error message indicating the password must contain lowercase letters
  And the password field is highlighted to indicate the error

  Examples:
    | email                | password      | confirmation  |
    | krishna@gmail.com    | PASSWORD123   | PASSWORD123    |

  @invalid-email-format
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password           | confirmation       |
      | abc@      | ValidPassword123   | ValidPassword123    |

  @empty-password
  Scenario Outline: User Registration Fails When Password is Empty
    Given the user is on the Registration Page
    When I enter a unique email address "<email>"
    And I leave the password field empty
    And I leave the confirmation password field empty
    And I click the "Register" button
    Then the system should display an error message indicating the password field is required

    Examples:
      | email             |
      | krishna@gmail.com |

  @invalid-password-length
  Scenario Outline: Validate Registration Fails with Long Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password is too long

    Examples:
      | email                 | password            | confirmation        |
      | krishna@gmail.com     | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa | aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa |

  @invalid-email-format
  Scenario Outline: User Registration Fails with Email Without Domain
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And the user clicks the Register button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email  | password             | confirmation         |
      | abc@   | ValidPassword123!    | ValidPassword123!    |

  @invalid-password
  Scenario Outline: User Registration Process
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I enter "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password contains invalid characters
    And the password field should be highlighted

    Examples:
      | email              | password     | confirmation  |
      | krishna@gmail.com  | !@#$%^&*     | !@#$%^&*      |

  @invalid-email-registration
  Scenario Outline: User Registration Fails with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email          | password             | confirmation         |
      | abc@@gmail.com | ValidPassword123!    | ValidPassword123!     |

  @invalid-password
  Scenario Outline: User Registration Process with Short Password
    Given the user is on the Registration Page
    When I enter "<email>"
    And I enter "<password>"
    And I confirm the password as "<confirmation>"
    And I click the "Register" button
    Then an error message should be displayed indicating the password is too short
    And the password field should be highlighted to indicate the error

    Examples:
      | email              | password | confirmation |
      | krishna@gmail.com  | short    | short        |

  @invalid-email
  Scenario Outline: User Registration with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system displays an error message indicating the email format is invalid
    And the email field is highlighted to indicate the error

    Examples:
      | email     | password              | confirmation          |
      | abc@      | ValidPassword123!     | ValidPassword123!     |

  @invalid-email
  Scenario Outline: User Registration Fails with Invalid Email Format
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then an error message should be displayed indicating the email format is invalid

    Examples:
      | email  | password              | confirmation         |
      | abc@   | ValidPassword123!     | ValidPassword123!     |

@invalid-password
Scenario Outline: User Registration Process with Missing Uppercase Letter
  Given the user is on the Registration Page
  When the user enters "<email>" in the email field
  And the user enters "<password>" in the password field
  And the user enters "<confirmation>" in the confirmation field
  And the user clicks the "Register" button
  Then the system displays an error message indicating the password must contain uppercase letters
  And the password field is highlighted to indicate the error

  Examples:
    | email                     | password      | confirmation  |
    | uniqueuser@gmail.com     | password123   | password123    |

  @invalid-email
  Scenario Outline: User Registration Process with Invalid Email
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I enter "<confirmation>" in the confirmation field
    And I click the "Register" button
    Then the system should display an error message indicating the email format is invalid
    And the email field should be highlighted to indicate the error

    Examples:
      | email     | password            | confirmation         |
      | abc@!     | ValidPassword123    | ValidPassword123     |
