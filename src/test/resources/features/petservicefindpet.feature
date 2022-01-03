@find_pet
  Feature: Searching For Pets By Their Id

    Scenario: Pet with a specific Id does not exist
      Given Searching Id for pet is 20
      When performing pet service findPet with the Id
      Then The returned Pet is ""

    Scenario: Pet with a specific Id exists
      Given Searching Id for pet is 1
      When performing pet service findPet with the Id
      Then The returned Pet is "dog"
