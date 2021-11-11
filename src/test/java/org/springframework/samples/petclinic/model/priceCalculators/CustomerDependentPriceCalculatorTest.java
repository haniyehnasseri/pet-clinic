package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Haniyeh Nasseri
 */
class CustomerDependentPriceCalculatorTest {

	private CustomerDependentPriceCalculator customerDependentPriceCalculator;

	// first path in prime path collection
	@BeforeEach
	void setup(){
		customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
	}


    @Test
    void calcPrice_TwoYoungPetsOneRareOneNot_UserIsGold_ReturnPrice() {

    	Pet youngRarePet = DummyEntityGenerator.getNewDummyPet();
    	youngRarePet.setName("Rare");
    	youngRarePet.setBirthDate(Date.from(Instant.now()));

		Pet youngNotRarePet = DummyEntityGenerator.getNewDummyPet();
		youngNotRarePet.setName("NonRare");
		youngNotRarePet.setBirthDate(Date.from(Instant.now()));

    	PetType mockedPetType = Mockito.mock(PetType.class);
    	youngRarePet.setType(mockedPetType);
    	youngNotRarePet.setType(mockedPetType);

    	Mockito.when(mockedPetType.getRare()).thenReturn(true).thenReturn(false);

    	double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(youngRarePet, youngNotRarePet), 1000, 1000, UserType.GOLD);

		Assert.assertEquals(3304.0, calculatedPrice, 0.0);

    }

	@Test
	void calcPrice_TwoOldPetsOneRareOneNot_UserIsGold_ReturnPrice() {

		Pet youngRarePet = DummyEntityGenerator.getNewDummyPet();
		youngRarePet.setName("Rare");
		youngRarePet.setBirthDate(new GregorianCalendar(2010, 1, 1).getTime());

		Pet youngNotRarePet = DummyEntityGenerator.getNewDummyPet();
		youngNotRarePet.setName("NonRare");
		youngNotRarePet.setBirthDate(new GregorianCalendar(2010, 1, 1).getTime());

		PetType mockedPetType = Mockito.mock(PetType.class);
		youngRarePet.setType(mockedPetType);
		youngNotRarePet.setType(mockedPetType);

		Mockito.when(mockedPetType.getRare()).thenReturn(true).thenReturn(false);

		double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(youngRarePet, youngNotRarePet), 1000, 1000, UserType.NEW);

		Assert.assertEquals(2200.0, calculatedPrice, 0.0);
	}

	@Test
	void calcPrice_SixYoungPetsAllRare_UserIsNew_ReturnPrice() {

		Pet youngRarePet1 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet1.setName("Rare1");
		youngRarePet1.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet2 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet2.setName("Rare2");
		youngRarePet2.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet3 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet3.setName("Rare3");
		youngRarePet3.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet4 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet4.setName("Rare4");
		youngRarePet4.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet5 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet5.setName("Rare5");
		youngRarePet5.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet6 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet6.setName("Rare6");
		youngRarePet6.setBirthDate(Date.from(Instant.now()));

		PetType mockedPetType = Mockito.mock(PetType.class);
		youngRarePet1.setType(mockedPetType);
		youngRarePet2.setType(mockedPetType);
		youngRarePet3.setType(mockedPetType);
		youngRarePet4.setType(mockedPetType);
		youngRarePet5.setType(mockedPetType);
		youngRarePet6.setType(mockedPetType);

		Mockito.when(mockedPetType.getRare()).thenReturn(true);

		double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(youngRarePet1, youngRarePet2, youngRarePet3, youngRarePet4, youngRarePet5, youngRarePet6),
			1000, 1000, UserType.NEW);

		Assert.assertEquals(10576.0, calculatedPrice, 0.0);
	}

	@Test
	void calcPrice_SixYoungPetsAllRare_UserIsNotNew_ReturnPrice() {

		Pet youngRarePet1 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet1.setName("Rare1");
		youngRarePet1.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet2 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet2.setName("Rare2");
		youngRarePet2.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet3 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet3.setName("Rare3");
		youngRarePet3.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet4 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet4.setName("Rare4");
		youngRarePet4.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet5 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet5.setName("Rare5");
		youngRarePet5.setBirthDate(Date.from(Instant.now()));

		Pet youngRarePet6 = DummyEntityGenerator.getNewDummyPet();
		youngRarePet6.setName("Rare6");
		youngRarePet6.setBirthDate(Date.from(Instant.now()));

		PetType mockedPetType = Mockito.mock(PetType.class);
		youngRarePet1.setType(mockedPetType);
		youngRarePet2.setType(mockedPetType);
		youngRarePet3.setType(mockedPetType);
		youngRarePet4.setType(mockedPetType);
		youngRarePet5.setType(mockedPetType);
		youngRarePet6.setType(mockedPetType);

		Mockito.when(mockedPetType.getRare()).thenReturn(true);

		double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(youngRarePet1, youngRarePet2, youngRarePet3, youngRarePet4, youngRarePet5, youngRarePet6),
			1000, 1000, UserType.GOLD);

		Assert.assertEquals(8864.0, calculatedPrice, 0.0);
	}
}
