package org.springframework.samples.petclinic.owner;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;

import java.util.Arrays;
import java.util.HashSet;

class OwnerTest {

	private Owner owner;

	@BeforeEach
	public void setUp(){
		owner = DummyEntityGenerator.getNewDummyOwner();
	}

	@AfterEach
	public void tearDown(){
		owner = null;
	}

	@Test
	void newOwnerTest(){
		Assert.assertEquals("firstName", owner.getFirstName());
		Assert.assertEquals("lastName", owner.getLastName());
		Assert.assertEquals("123", owner.getTelephone());
		Assert.assertEquals("city", owner.getCity());
		Assert.assertEquals("address", owner.getAddress());
		Assert.assertTrue(owner.getPets().isEmpty());
		Assert.assertNull(owner.getId());
	}

	@Test
	void addNewPetTest(){
		Pet pet = DummyEntityGenerator.getNewDummyPet();
		Assert.assertNotNull(owner.getPetsInternal());
		Assert.assertEquals(0, owner.getPetsInternal().size());
		owner.addPet(pet);
		Assert.assertEquals(1, owner.getPetsInternal().size());
		Assert.assertEquals(pet, owner.getPetsInternal().iterator().next());
	}

	@Test
	void addAlreadySavedInDatabasePetTest(){
		Pet pet = DummyEntityGenerator.getAlreadySavedInDatabaseDummyPet();
		owner.addPet(pet);
		Assert.assertNotNull(owner.getPetsInternal());
		Assert.assertEquals(0, owner.getPetsInternal().size());
		Assert.assertEquals(owner, pet.getOwner());
	}

	@Test
	void getSortedPetsTest(){
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet1.setName("aCat");
		pet2.setName("bCat");
		owner.addPet(pet1);
		owner.addPet(pet2);
		Assert.assertEquals(pet1, owner.getPets().get(0));
		pet1.setName("dCat1");
		Assert.assertEquals(pet2, owner.getPets().get(0));
		pet2.setName("dCat2");
		Assert.assertEquals(pet1, owner.getPets().get(0));
		pet2.setName("BCat");
		Assert.assertEquals(pet2, owner.getPets().get(0));
		pet1.setName("BCAS");
		Assert.assertEquals(pet1, owner.getPets().get(0));
	}

	@Test
	void removePetTest(){
		Pet pet = DummyEntityGenerator.getNewDummyPet();
		owner.addPet(pet);
		Assert.assertEquals(1, owner.getPetsInternal().size());
		owner.removePet(pet);
		Assert.assertEquals(0, owner.getPetsInternal().size());
	}

	@Test
	void getAlreadySavedInDatabasePetTest(){
		Pet newPet = DummyEntityGenerator.getNewDummyPet();
		Pet savedPet = DummyEntityGenerator.getAlreadySavedInDatabaseDummyPet();
		owner.addPet(newPet);
		owner.getPetsInternal().add(savedPet);
		Assert.assertEquals(2, owner.getPetsInternal().size());
		Assert.assertNull(owner.getPet("cat1", true));
		Assert.assertNotNull(owner.getPet("cat2", true));
	}

	@Test
	void getNewOrAlreadySavedInDatabasePetTest(){
		Pet newPet = DummyEntityGenerator.getNewDummyPet();
		Pet savedPet = DummyEntityGenerator.getAlreadySavedInDatabaseDummyPet();
		owner.addPet(newPet);
		owner.getPetsInternal().add(savedPet);
		Assert.assertEquals(2, owner.getPetsInternal().size());
		Assert.assertNotNull(owner.getPet("cat1", false));
		Assert.assertNotNull(owner.getPet("cat2", false));
		Assert.assertNotNull(owner.getPet("cat1"));
		Assert.assertNotNull(owner.getPet("cat2"));
	}

	@Test
	void setPetsInternalTest(){
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		owner.setPetsInternal(new HashSet<>(Arrays.asList(pet1,pet2)));
		Assert.assertEquals(2, owner.getPetsInternal().size());
	}



}
