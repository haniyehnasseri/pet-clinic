@find_owner
Feature: Searching For Owner By their Id

  Scenario: Owner with a specific Id does not exist
    Given Searching Id is 11
    When performing pet service findOwner with the Id
    Then The returned Owner is ""

  Scenario: Owner with a specific Id exists
    Given Searching Id is 1
    When performing pet service findOwner with the Id
    Then The returned Owner is "George"
