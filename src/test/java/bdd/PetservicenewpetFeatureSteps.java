package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.utility.PetTimedCache;

public class PetservicenewpetFeatureSteps {
	@Autowired
	private PetTimedCache pets;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private Logger logger;

	@Autowired
	private PetService petService;

	private Owner owner;

	private Pet newPet;

	@Given("valid owner is provided")
	public void valid_owner_is_provided() {
		owner = Mockito.spy(owners.findById(1));
	}

	@When("performing pet service new pet for the owner")
	public void performing_pet_service_new_pet_for_the_owner() {
		newPet = petService.newPet(owner);
	}

	@Then("a new pet is added to owner")
	public void a_new_pet_is_added_to_owner() {
		Mockito.verify(owner).addPet(Mockito.isA(Pet.class));
	}

	@Then("returned pet is valid")
	public void returned_pet_is_valid() {
		Assertions.assertNotNull(newPet);
	}
}
