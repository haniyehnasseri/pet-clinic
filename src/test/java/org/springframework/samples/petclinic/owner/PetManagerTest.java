package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

class PetManagerTest {

	//Spy
	@Spy
	private Logger logger;

	//Stub
	@Mock
	private OwnerRepository ownerRepository;

	//Mock
	@Mock
	private PetTimedCache petTimedCache;

	private static Map<Integer, Pet> pets = new HashMap<>();
	private static int petsNum = 0;

	private PetManager petManager;

	@BeforeAll
	static void initializePetManagerTestClassWithSomePets(){
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setName("pet1");
		pets.put(1, pet1);
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setName("pet2");
		pets.put(2, pet2);
		pets.put(3, null);
		petsNum = 3;
	}

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.doAnswer(invocationOnMock -> pets.get((Integer) invocationOnMock.getArgument(0))).when(petTimedCache).get(anyInt());
		Mockito.doAnswer(invocationOnMock -> pets.put(++petsNum, invocationOnMock.getArgument(0)))
			.when(petTimedCache).save(Mockito.any(Pet.class));
		Mockito.doNothing().when(logger).info(Mockito.anyString(),Mockito.any(Object.class));
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
	}


	// State Verification
	// using a Stub and a Dummy
	// Mockisty
	@Test
	void findOwner_ValidOwnerId_FindsOwnerSuccessfully(){
		// setup - data (Dummy Object)
		int ownderId = 1;
		Owner owner = Mockito.mock(Owner.class);
		// setup - expectations (Stub returns a dummy object but not null in response to desired method call)
		Mockito.doReturn(owner).when(ownerRepository).findById(anyInt());
		//exercise
		Owner foundOwner = petManager.findOwner(ownderId);
		//verify
		assertNotNull(foundOwner);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(ownderId));
	}

	// State Verification
	// using a Stub
	// Mockisty
	@Test
	void findOwner_InvalidValidOwnerId_ReturnsNull(){
		int ownerId = 1;
		// setup - expectations (Stub returns a dummy object but not null in response to desired method call)
		Mockito.doReturn(null).when(ownerRepository).findById(anyInt());
		// exercise
		Owner foundOwner = petManager.findOwner(ownerId);
		// verify
		assertNull(foundOwner);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(ownerId));
	}


	// State Verification
	@Test()
	void newPet_NullOwner_ExpectedThrowError(){
		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.newPet(null));
		Mockito.verify(logger, Mockito.times(0)).info(Mockito.isA(String.class), anyInt());
	}

	// State Verification + Behavioural Verification
	// Mockisty
	// used Spy Object for Owner
	@Test()
	void newPet_ValidSpyOwner_SuccessfullyAdded(){
		// setup - data (Spy Owner)
		Owner spyOwner = Mockito.spy(Owner.class);
		// setup - expectation
		Mockito.doNothing().when(spyOwner).addPet(Mockito.any(Pet.class));
		// exercise
		Pet createdPet = petManager.newPet(spyOwner);
		// verify
		assertNotNull(spyOwner);
		Mockito.verify(spyOwner).addPet(Mockito.isA(Pet.class));
		assertNotNull(createdPet);
		Mockito.verify(logger).info(Mockito.isA(String.class), (Object) Mockito.eq(null));
	}


	// State Verification + Behavioural Verification
	// Mockisty
	// using one Mock
	@Test()
	void findPet_InvalidPetId_ReturnNull(){
		// setup - data
		int searchingPetId = 5;
		// expectation in setup
		// exercise
		Pet foundPet = petManager.findPet(searchingPetId);
		// verify
		Mockito.verify(petTimedCache).get(5);
		assertNull(foundPet);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(searchingPetId));
	}

	// State Verification + Behavioural Verification
	// Mockisty
	// used one Mock
	@Test()
	void findPet_ValidPetId_SuccessfullyFound(){
		// setup - data
		int searchingPetId = 1;
		// expectation for Mock petTimedCache in setup
		// exercise
		Pet foundPet = petManager.findPet(searchingPetId);
		// verify
		Mockito.verify(petTimedCache).get(1);
		assertNotNull(foundPet);
		assertEquals("pet1", foundPet.getName());
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(searchingPetId));
	}

	// State Verification + Behavioural Verification
	// used Dummy and Spy Objects
	// Mockisty
	@Test
	void savePet_ValidMockedOwner_SuccessfullyAdded(){
		// setup - data (Spy and Dummy)
		Owner spyOwner = Mockito.spy(Owner.class);
		Pet dummyPet = Mockito.mock(Pet.class);

		// expectation for Mock petTimedCache in setup
		Mockito.doNothing().when(spyOwner).addPet(Mockito.any(Pet.class));

		// exercise
		petManager.savePet(dummyPet, spyOwner);

		// verify
		Mockito.verify(spyOwner).addPet(dummyPet);
		assertEquals(dummyPet,pets.get(petsNum));
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(0));
	}


	// State Verification
	// Dummy and Mock Objects
	// Mockisty
	@Test
	void savePet_NullOwner_ThrowsNullPointerException(){
		// setup - data (Dummy)
		Pet dummyPet = Mockito.mock(Pet.class);
		// expectation for Mock petTimedCache in setup

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.savePet(dummyPet, null));
		assertNotEquals(dummyPet,pets.get(petsNum));
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(0));
	}

	// State Verification
	// Spy and Mock Objects
	// Mockisty
	@Test
	void savePet_ValidMockedOwner_NullPet_NullPointerException(){
		// setup - data (Spy)
		Owner spyOwner = Mockito.spy(Owner.class);

		// expectation for Mock petTimedCache in setup
		Mockito.doNothing().when(spyOwner).addPet(Mockito.any(Pet.class));

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.savePet(null, spyOwner));
		Mockito.verify(logger, Mockito.times(0)).info(Mockito.isA(String.class), anyInt());
	}


	// State Verification + Behavioural Verification
	// One Mock One Stub
	// Mockisty
	@Test
	void getOwnerPets_ExistingOwner_SuccessfullyReturnsPetsList(){

		// setup - data (Mock)
		Owner mockedOwner = Mockito.mock(Owner.class);
		int mockedOwnerId = 1;

		// expectation for Stub ownerRepository and Mock stubOwner
		Mockito.doReturn(Arrays.asList(pets.get(2), pets.get(1))).when(mockedOwner).getPets();
		Mockito.doReturn(mockedOwner).when(ownerRepository).findById(mockedOwnerId);

		// exercise
		List<Pet> foundPets = petManager.getOwnerPets(mockedOwnerId);

		//verify
		assertEquals(2, foundPets.size());
		assertNotNull(foundPets.get(0));
		assertNotNull(foundPets.get(1));
		Mockito.verify(mockedOwner).getPets();
		Mockito.verify(logger).info("finding the owner's pets by id {}", mockedOwnerId);
	}

	// State Verification
	// one Stub
	// Mockisty
	@Test
	void getOwnerPets_InvalidOwner_ReturnsNull(){

		// setup - data
		int stubOwnerId = 1;

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(null).when(ownerRepository).findById(stubOwnerId);

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.getOwnerPets(1));
		Mockito.verify(logger).info("finding the owner's pets by id {}", stubOwnerId);
	}

	// State Verification
	// one Stub
	// Mockisty
	@Test
	void getOwnerPetTypes_InvalidOwner_ReturnsNull(){

		// setup - data
		int stubOwnerId = 1;

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(null).when(ownerRepository).findById(stubOwnerId);

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.getOwnerPetTypes(1));
		Mockito.verify(logger).info("finding the owner's petTypes by id {}", stubOwnerId);
	}

	// State + Behavioural Verification
	// one Stub One Mock
	// Mockisty
	@Test
	void getOwnerPetTypes_ValidMockedOwner_EmptyPetsList_ReturnsEmptySet(){

		// setup - data (Owner Mocked)
		int mockedOwnerId = 1;
		Owner mockedOwner = Mockito.mock(Owner.class);
		List<Pet> mockedOwnerPetsList = new ArrayList<>();

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(mockedOwner).when(ownerRepository).findById(mockedOwnerId);
		Mockito.doReturn(mockedOwnerPetsList).when(mockedOwner).getPets();

		// exercise
		Set<PetType> petTypeSet = petManager.getOwnerPetTypes(mockedOwnerId);

		//verify
		assertEquals(0, petTypeSet.size());
		Mockito.verify(mockedOwner, Mockito.times(1)).getPets();
		Mockito.verify(logger).info("finding the owner's petTypes by id {}", mockedOwnerId);
	}


	// State + BehaviouralVerification
	// one Stub One Mock
	// Mockisty
	@Test
	void getOwnerPetTypes_ValidMockedOwner_PetsListEachDifferentPetType_ReturnsSuccessfully(){

		// setup - data (Owner Mocked)
		int mockedOwnerId = 1;
		Owner mockedOwner = Mockito.mock(Owner.class);
		List<Pet> mockedOwnerPetsList = new ArrayList<>();
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setName("pet1");
		PetType petType1 = DummyEntityGenerator.getNewDummyPetType();
		petType1.setName("petType1");
		pet1.setType(petType1);
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setName("pet2");
		PetType petType2 = DummyEntityGenerator.getNewDummyPetType();
		petType2.setName("petType2");
		pet2.setType(petType2);
		mockedOwnerPetsList.add(pet1);
		mockedOwnerPetsList.add(pet2);
		PropertyComparator.sort(mockedOwnerPetsList, new MutableSortDefinition("name", true, true));
		List<Pet> sortedMockedOwnerPetsListCollections = Collections.unmodifiableList(mockedOwnerPetsList);

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(mockedOwner).when(ownerRepository).findById(mockedOwnerId);
		Mockito.doReturn(sortedMockedOwnerPetsListCollections).when(mockedOwner).getPets();

		// exercise & verify
		Set<PetType> petTypeSet = petManager.getOwnerPetTypes(mockedOwnerId);

		//verify
		assertEquals(2, petTypeSet.size());
		assertEquals(2, sortedMockedOwnerPetsListCollections.size());
		Mockito.verify(mockedOwner, Mockito.times(1)).getPets();
		Mockito.verify(logger).info("finding the owner's petTypes by id {}", mockedOwnerId);
	}


	// State + Behavioural Verification
	// one Stub One Mock
	// Mockisty
	@Test
	void getOwnerPetTypes_ValidMockedOwner_PetsListSamePetType_ReturnsSuccessfully(){

		// setup - data (Owner Mocked)
		int mockedOwnerId = 1;
		Owner mockedOwner = Mockito.mock(Owner.class);
		List<Pet> mockedOwnerPetsList = new ArrayList<>();
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setName("pet1");
		PetType petType1 = DummyEntityGenerator.getNewDummyPetType();
		petType1.setName("petType1");
		pet1.setType(petType1);
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setName("pet2");
		pet2.setType(petType1);
		mockedOwnerPetsList.add(pet1);
		mockedOwnerPetsList.add(pet2);
		PropertyComparator.sort(mockedOwnerPetsList, new MutableSortDefinition("name", true, true));
		List<Pet> sortedMockedOwnerPetsListCollections = Collections.unmodifiableList(mockedOwnerPetsList);

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(mockedOwner).when(ownerRepository).findById(mockedOwnerId);
		Mockito.doReturn(sortedMockedOwnerPetsListCollections).when(mockedOwner).getPets();

		// exercise & verify
		Set<PetType> petTypeSet = petManager.getOwnerPetTypes(mockedOwnerId);

		//verify
		assertEquals(1, petTypeSet.size());
		assertEquals(2, sortedMockedOwnerPetsListCollections.size());
		Mockito.verify(mockedOwner, Mockito.times(1)).getPets();
		Mockito.verify(logger).info("finding the owner's petTypes by id {}", mockedOwnerId);
	}

}
