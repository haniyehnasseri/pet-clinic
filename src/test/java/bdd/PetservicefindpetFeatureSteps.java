package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.utility.PetTimedCache;


public class PetservicefindpetFeatureSteps {

	@Autowired
	private PetTimedCache pets;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private Logger logger;

	@Autowired
	private PetService petService;


	private Integer searchingId;
	private Pet foundedPet;

	@Given("Searching Id for pet is {int}")
	public void searching_Id_is(Integer int1) {
		searchingId = int1;
	}

	@When("performing pet service findPet with the Id")
	public void performing_pet_service_findPet_with_the_Id() {
		foundedPet = petService.findPet(searchingId);
	}

	@Then("The returned Pet is {string}")
	public void the_returned_Pet_is(String string) {
		if (string.isEmpty()) {
			Assertions.assertNull(foundedPet);
		} else {
			Assertions.assertNotNull(foundedPet);
		}
	}

}
