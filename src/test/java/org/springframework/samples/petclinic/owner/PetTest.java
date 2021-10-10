package org.springframework.samples.petclinic.owner;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Haniyeh Nasseri
 */
@RunWith(Theories.class)
public class PetTest {

	private static LocalDate produceRandomDate(){
		long minDay = LocalDate.of(2019, 1, 1).toEpochDay();
		long maxDay = LocalDate.of(2020, 1, 1).toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
		return LocalDate.ofEpochDay(randomDay);
	}

	@DataPoints
	public static Pet[] animals = {DummyEntityGenerator.getAlreadySavedInDatabaseDummyPet()};

	@DataPoints
	public static Visit[] visits = new Visit[50];

	static {
		for(int i = 0; i < 50; i++){
			Visit newVisit = new Visit();
			newVisit.setDescription("visit" + i);
			newVisit.setId(i);
			newVisit.setDate(produceRandomDate());
			visits[i] = newVisit;
		}
	}


	@Theory
	public void getVisitedSorted(Pet pet, Visit visit)
	{
		pet.addVisit(visit);
		Assume.assumeTrue(pet.getVisits().size() > 5);
		List<Visit> sortedVisits = pet.getVisits();
		Assert.assertTrue(sortedVisits.get(0).getDate().isAfter(sortedVisits.get(1).getDate()) ||
			sortedVisits.get(0).getDate().isEqual(sortedVisits.get(1).getDate()));

		Assert.assertTrue(sortedVisits.get(1).getDate().isAfter(sortedVisits.get(2).getDate()) ||
			sortedVisits.get(1).getDate().isEqual(sortedVisits.get(2).getDate()));

		Assert.assertTrue(sortedVisits.get(2).getDate().isAfter(sortedVisits.get(3).getDate()) ||
			sortedVisits.get(2).getDate().isEqual(sortedVisits.get(3).getDate()));

		Assert.assertTrue(sortedVisits.get(3).getDate().isAfter(sortedVisits.get(4).getDate()) ||
			sortedVisits.get(3).getDate().isEqual(sortedVisits.get(4).getDate()));

		Assert.assertTrue(sortedVisits.get(4).getDate().isAfter(sortedVisits.get(5).getDate()) ||
			sortedVisits.get(4).getDate().isEqual(sortedVisits.get(5).getDate()));

	}
}
