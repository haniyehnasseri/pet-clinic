@new_pet
Feature: Adding new pet to an owner

  Scenario: we can add a new pet to a valid owner
    Given valid owner is provided
    When performing pet service new pet for the owner
    Then a new pet is added to owner
    And returned pet is valid
