package org.springframework.samples.petclinic.owner.theory;

import org.junit.Assert;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Haniyeh Nasseri
 */
@RunWith(Theories.class)
public class PetTestTheory {

	private static LocalDate produceRandomDate(){
		long minDay = LocalDate.of(2021, 1, 1).toEpochDay();
		long maxDay = LocalDate.of(2021, 5, 1).toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
		return LocalDate.ofEpochDay(randomDay);
	}

	@DataPoints
	public static Pet[] animals = {DummyEntityGenerator.getAlreadySavedInDatabaseDummyPet()};

	@DataPoints
	public static Visit[] visits = new Visit[100];

	static {
		for(int i = 0; i < 100; i++){
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
		List<Visit> sortedVisits = pet.getVisits();

		for(int i = 0; i < sortedVisits.size() - 1; i++){
			Assert.assertTrue(sortedVisits.get(i).getDate().isAfter(sortedVisits.get(i + 1).getDate()) ||
				sortedVisits.get(i).getDate().isEqual(sortedVisits.get(i+1).getDate()));
		}


	}
}
