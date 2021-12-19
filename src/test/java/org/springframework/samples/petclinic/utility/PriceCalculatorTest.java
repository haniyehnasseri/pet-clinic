package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorTest {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void calcPriceTest_1() {
		Pet pet1 = Mockito.spy(Pet.class);
		Pet pet2 = Mockito.spy(Pet.class);
		Pet pet3 = Mockito.spy(Pet.class);
		Pet pet4 = Mockito.spy(Pet.class);
		Pet pet5 = Mockito.spy(Pet.class);

		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet1).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet2).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet3).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet4).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet5).getBirthDate();
		Visit myVisit = Mockito.spy(Visit.class);
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth())).when(myVisit).getDate();
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet1).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet2).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet3).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet4).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet5).getVisitsUntilAge(2);

		double total = PriceCalculator.calcPrice(Arrays.asList(pet1,pet2,pet3,pet4,pet5), 1000, 1000);

		Assertions.assertEquals(32560.0, total);

	}


	@Test
	void calcPriceTest_2() {
		Pet pet1 = Mockito.spy(Pet.class);
		Pet pet2 = Mockito.spy(Pet.class);
		Pet pet3 = Mockito.spy(Pet.class);
		Pet pet4 = Mockito.spy(Pet.class);
		Pet pet5 = Mockito.spy(Pet.class);

		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet1).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet2).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet3).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet4).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 2, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet5).getBirthDate();
		Visit myVisit = Mockito.spy(Visit.class);
		Mockito.doReturn(LocalDate.now().minusDays(100)).when(myVisit).getDate();
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet1).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet2).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet3).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet4).getVisitsUntilAge(2);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet5).getVisitsUntilAge(2);

		double total = PriceCalculator.calcPrice(Arrays.asList(pet1,pet2,pet3,pet4,pet5), 1000, 1000);

		Assertions.assertEquals(17120.0, total);

	}


	@Test
	void calcPriceTest_3() {
		Pet pet1 = Mockito.spy(Pet.class);
		Pet pet2 = Mockito.spy(Pet.class);
		Pet pet3 = Mockito.spy(Pet.class);
		Pet pet4 = Mockito.spy(Pet.class);
		Pet pet5 = Mockito.spy(Pet.class);

		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet1).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet2).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet3).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet4).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet5).getBirthDate();
		Visit myVisit = Mockito.spy(Visit.class);
		Mockito.doReturn(LocalDate.now().minusDays(100)).when(myVisit).getDate();
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet1).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet2).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet3).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet4).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.singletonList(myVisit)).when(pet5).getVisitsUntilAge(1);

		double total = PriceCalculator.calcPrice(Arrays.asList(pet1,pet2,pet3,pet4,pet5), 1000, 1000);

		Assertions.assertEquals(17120.0, total);

	}


	@Test
	void calcPriceTest_4() {
		Pet pet1 = Mockito.spy(Pet.class);
		Pet pet2 = Mockito.spy(Pet.class);
		Pet pet3 = Mockito.spy(Pet.class);
		Pet pet4 = Mockito.spy(Pet.class);
		Pet pet5 = Mockito.spy(Pet.class);

		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet1).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet2).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet3).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet4).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet5).getBirthDate();
		Visit myVisit = Mockito.spy(Visit.class);
		Mockito.doReturn(LocalDate.now().minusDays(100)).when(myVisit).getDate();
		Mockito.doReturn(Collections.emptyList()).when(pet1).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet2).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet3).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet4).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet5).getVisitsUntilAge(1);

		double total = PriceCalculator.calcPrice(Arrays.asList(pet1,pet2,pet3,pet4,pet5), 1000, 1000);

		Assertions.assertEquals(16120.0, total);

	}


	@Test
	void calcPriceTest_5() {
		Pet pet1 = Mockito.spy(Pet.class);
		Pet pet2 = Mockito.spy(Pet.class);
		Pet pet3 = Mockito.spy(Pet.class);
		Pet pet4 = Mockito.spy(Pet.class);
		Pet pet5 = Mockito.spy(Pet.class);

		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet1).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet2).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet3).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet4).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet5).getBirthDate();
		Visit myVisit = Mockito.spy(Visit.class);
		Mockito.doReturn(LocalDate.now().minusDays(100)).when(myVisit).getDate();
		Mockito.doReturn(Collections.emptyList()).when(pet1).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet2).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet3).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet4).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet5).getVisitsUntilAge(1);

		double total = PriceCalculator.calcPrice(Arrays.asList(pet1,pet2,pet3,pet4,pet5), 0, 0);

		Assertions.assertEquals(0.0, total);

	}


	@Test
	void calcPriceTest_6() {
		Pet pet1 = Mockito.spy(Pet.class);
		Pet pet2 = Mockito.spy(Pet.class);
		Pet pet3 = Mockito.spy(Pet.class);
		Pet pet4 = Mockito.spy(Pet.class);
		Pet pet5 = Mockito.spy(Pet.class);
		Pet pet6 = Mockito.spy(Pet.class);

		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet1).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet2).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet3).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet4).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet5).getBirthDate();
		Mockito.doReturn(LocalDate.of(LocalDate.now().getYear() - 1, LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth())).when(pet6).getBirthDate();
		Visit myVisit = Mockito.spy(Visit.class);
		Mockito.doReturn(LocalDate.now().minusDays(100)).when(myVisit).getDate();
		Mockito.doReturn(Collections.emptyList()).when(pet1).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet2).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet3).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet4).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet5).getVisitsUntilAge(1);
		Mockito.doReturn(Collections.emptyList()).when(pet6).getVisitsUntilAge(1);

		double total = PriceCalculator.calcPrice(Arrays.asList(pet1,pet2,pet3,pet4,pet5,pet6), 1000, 1000);

		Assertions.assertEquals(34920.0, total);

	}
}
