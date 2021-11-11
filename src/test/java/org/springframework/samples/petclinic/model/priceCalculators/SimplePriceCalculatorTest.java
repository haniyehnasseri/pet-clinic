package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Haniyeh Nasseri
 */
class SimplePriceCalculatorTest {

	private SimplePriceCalculator simplePriceCalculator;

	Pet dummyPet;

	// first path in prime path collection
	@BeforeEach
	void setup(){
		simplePriceCalculator = new SimplePriceCalculator();
		dummyPet = DummyEntityGenerator.getNewDummyPet();
	}

    @Test
    void calcPrice_petsSizeIsOne_PetIsRare_UserIsNew_Then_ReturnTotalPriceWithDiscount() {
		// setup
		PetType mockPetType = Mockito.mock(PetType.class);
		dummyPet.setType(mockPetType);
		Mockito.doReturn(true).when(mockPetType).getRare();
		// execute
		double calculatedPrice = simplePriceCalculator.calcPrice(Collections.singletonList(dummyPet), 1000, 1000, UserType.NEW);
		// verify
		Assert.assertEquals(2090.0, calculatedPrice, 0.0);
		Mockito.verify(mockPetType, Mockito.times(1)).getRare();
    }

	@Test
	void calcPrice_petsSizeIsOne_PetIsNotRare_UserIsNotNew_Then_ReturnTotalPriceWithoutDiscount() {
		// setup
		PetType mockPetType = Mockito.mock(PetType.class);
		dummyPet.setType(mockPetType);
		Mockito.doReturn(false).when(mockPetType).getRare();
		// execute
		double calculatedPrice = simplePriceCalculator.calcPrice(Collections.singletonList(dummyPet), 1000, 1000, UserType.GOLD);
		// verify
		Assert.assertEquals(2000.0, calculatedPrice, 0.0);
		Mockito.verify(mockPetType, Mockito.times(1)).getRare();
	}

	@Test
	void calcPrice_petsSizeIsTwo_PetsAreRare_UserIsNotNew_Then_ReturnTotalPriceWithoutDiscount() {
		// setup
		Pet secondDummyPet = DummyEntityGenerator.getNewDummyPet();
		PetType mockPetType = Mockito.mock(PetType.class);
		dummyPet.setType(mockPetType);
		secondDummyPet.setType(mockPetType);
		Mockito.doReturn(true).when(mockPetType).getRare();
		// execute
		double calculatedPrice = simplePriceCalculator.calcPrice(Arrays.asList(secondDummyPet, dummyPet), 1000, 1000, UserType.GOLD);
		// verify
		Assert.assertEquals(3400.0, calculatedPrice, 0.0);
		Mockito.verify(mockPetType, Mockito.times(2)).getRare();
	}

	@Test
	void calcPrice_petsSizeIsTwo_PetsAreNotRare_UserIsNew_Then_ReturnTotalPriceWithoutDiscount() {
		// setup
		Pet secondDummyPet = DummyEntityGenerator.getNewDummyPet();
		PetType mockPetType = Mockito.mock(PetType.class);
		dummyPet.setType(mockPetType);
		secondDummyPet.setType(mockPetType);
		Mockito.doReturn(false).when(mockPetType).getRare();
		// execute
		double calculatedPrice = simplePriceCalculator.calcPrice(Arrays.asList(secondDummyPet, dummyPet), 1000, 1000, UserType.NEW);
		// verify
		Assert.assertEquals(2850.0, calculatedPrice, 0.0);
		Mockito.verify(mockPetType, Mockito.times(2)).getRare();
	}

}
