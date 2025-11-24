
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
