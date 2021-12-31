package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.utility.PetTimedCache;

public class PetservicefindownerFeatureSteps {

	@Autowired
	private PetTimedCache pets;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private Logger logger;

	@Autowired
	private PetService petService;

	private Integer searchingId;

	private Owner foundedOwner;

	@Given("Searching Id is {int}")
	public void searching_Id_is(Integer int1) {
		searchingId = int1;
	}

	@When("performing pet service findOwner with the Id")
	public void performing_pet_service_findOwner_with_the_Id() {
		foundedOwner = petService.findOwner(searchingId);
	}

	@Then("The returned Owner is {string}")
	public void the_returned_Owner_is(String string) {
		if (string.isEmpty()) {
			Assertions.assertNull(foundedOwner);
		} else {
			Assertions.assertNotNull(foundedOwner);
		}
	}
}
