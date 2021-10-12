package org.springframework.samples.petclinic.owner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Haniyeh Nasseri
 */

@RunWith(Parameterized.class)
public class PetServiceTest {

	private static FakePetService petService;

	private static Pet[] pets = new Pet[2];

	private static class FakePetService extends PetService{

		private final HashMap<Integer, Pet> pets;

		public FakePetService(PetTimedCache pets, OwnerRepository owners, Logger criticalLogger) {
			super(pets, owners, criticalLogger);
			this.pets = new HashMap<>();
		}

		@Override
		public Pet findPet(int petId) {
			return this.pets.get(petId);
		}

		@Override
		public void savePet(Pet pet, Owner owner) {
			pets.put(pet.getId(), pet);
			pet.setOwner(owner);
		}
	}

	static {
		petService = new FakePetService(null, null, null);
		Owner owner = DummyEntityGenerator.getNewDummyOwner();
		owner.setId(1);
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setId(1);
		pet1.setName("cat1");
		petService.savePet(pet1, owner);
		pets[0] = pet1;
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setId(2);
		pet2.setName("cat2");
		petService.savePet(pet2, owner);
		pets[1] = pet2;
	}

	public Integer searchingId;

	public Pet petSearched;

	public PetServiceTest(Integer searchingId, Pet petSearched){
		this.searchingId = searchingId;
		this.petSearched = petSearched;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList (new Object [][] {{1,pets[0]},{2,pets[1]},{3, null}});
	}

    @Test
    public void findPet() {
		Assert.assertEquals(petSearched, petService.findPet(searchingId));
    }
}
