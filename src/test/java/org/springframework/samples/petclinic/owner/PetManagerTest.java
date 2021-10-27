package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;

//
class PetManagerTest {

	// We think asserting exceptions is both state verification and behavioural verification
	// --> in this test class we assume it is behavioural

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

	// ownerRepository --> stub
	// cache --> Mock
	// Logger --> spy
	// owner differs in each test
	// pets in HashMap spy

	@BeforeAll
	static void initializePetManagerTestClassWithSomePets(){
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setName("pet1");
		pets.put(1, Mockito.spy(pet1));
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setName("pet2");
		pets.put(2, Mockito.spy(pet2));
		pets.put(3, null);
		petsNum = 3;
	}

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.doAnswer(invocationOnMock -> pets.get((Integer) invocationOnMock.getArgument(0))).when(petTimedCache).get(anyInt());
		Mockito.doAnswer(invocationOnMock -> pets.put(++petsNum, invocationOnMock.getArgument(0)))
			.when(petTimedCache).save(any(Pet.class));
		Mockito.doNothing().when(logger).info(Mockito.anyString(), any(Object.class));
		petManager = new PetManager(petTimedCache, ownerRepository, logger);
	}


	// State Verification
	// using a Stub and a Dummy
	@Test
	void findOwner_ValidOwnerId_FindsOwnerSuccessfully(){
		// setup - data (Dummy Object)
		int ownerId = 1;
		Owner owner = Mockito.mock(Owner.class);

		// setup - expectations (Stub returns a dummy object but not null in response to desired method call)
		Mockito.doReturn(owner).when(ownerRepository).findById(ownerId);

		//exercise
		Owner foundOwner = petManager.findOwner(ownerId);

		//verify
		assertNotNull(foundOwner);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(ownerId));
	}

	// State Verification
	// using a Stub
	@Test
	void findOwner_InvalidOwnerId_ReturnsNull(){
		int ownerId = 1;
		// setup - expectations (Stub returns a dummy object but null in response to desired method call)
		Mockito.doReturn(null).when(ownerRepository).findById(ownerId);
		// exercise
		Owner foundOwner = petManager.findOwner(ownerId);
		// verify
		assertNull(foundOwner);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(ownerId));
	}


	// Behavioural Verification
	@Test()
	void newPet_NullOwner_ExpectedThrowError(){
		// setup
		Owner owner = null;
		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.newPet(owner));
		Mockito.verify(logger, times(0)).info(Mockito.isA(String.class), anyInt());
	}

	// Behavioural Verification
	// used Spy Object for Owner
	@Test()
	void newPet_ValidSpyOwner_SuccessfullyAdded(){
		// setup - data (Spy Owner)
		Owner spyOwner = Mockito.spy(Owner.class);
		// setup - expectation
		Mockito.doNothing().when(spyOwner).addPet(any(Pet.class));
		// exercise
		Pet createdPet = petManager.newPet(spyOwner);
		// verify
		Mockito.verify(spyOwner).addPet(Mockito.isA(Pet.class));
		Mockito.verify(logger).info(Mockito.isA(String.class), (Object) Mockito.isNull());
	}


	// State Verification + Behavioural Verification
	// using one Mock for cache
	@Test()
	void findPet_InvalidPetId_ReturnNull(){
		// setup - data
		int searchingPetId = 5;
		// expectation in setup (for petTimeCache)
		// exercise
		Pet foundPet = petManager.findPet(searchingPetId);
		// verify
		assertNull(foundPet);
		Mockito.verify(petTimedCache).get(searchingPetId);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(searchingPetId));
	}

	// State Verification + Behavioural Verification
	// used one Mock for cache
	@Test()
	void findPet_ValidPetId_SuccessfullyFound(){
		// setup - data
		int searchingPetId = 1;

		// expectation for Mock petTimedCache in setup

		// exercise
		Pet foundPet = petManager.findPet(searchingPetId);

		// verify
		assertNotNull(foundPet);
		assertEquals("pet1", foundPet.getName());
		Mockito.verify(petTimedCache).get(searchingPetId);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(searchingPetId));
	}

	// State Verification + Behavioural Verification
	// used Dummy and Spy and Mock Objects
	@Test
	void savePet_ValidMockedOwner_SuccessfullyAdded(){
		// setup - data (Spy and Dummy)
		Owner spyOwner = Mockito.spy(Owner.class);
		Pet dummyPet = Mockito.mock(Pet.class);

		// expectation for Mock petTimedCache in setup
		Mockito.doNothing().when(spyOwner).addPet(any(Pet.class));

		// exercise
		petManager.savePet(dummyPet, spyOwner);

		// verify
		assertEquals(dummyPet,pets.get(petsNum));
		Mockito.verify(spyOwner).addPet(dummyPet);
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(0));
	}


	// State + Behavioural Verification
	// Dummy and Mock Objects
	@Test
	void savePet_NullOwner_ThrowsNullPointerException(){
		// setup - data (Dummy) -----> Mock Object returns default values for fields of class
		Pet dummyPet = Mockito.mock(Pet.class);
		// expectation for Mock petTimedCache in setup

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.savePet(dummyPet, null));
		assertNotEquals(dummyPet,pets.get(petsNum));
		Mockito.verify(logger).info(Mockito.isA(String.class), Mockito.eq(0));
	}

	// Behavioural Verification
	// Spy and Mock Objects
	@Test
	void savePet_ValidMockedOwner_NullPet_NullPointerException(){
		// setup - data (Spy)
		Owner spyOwner = Mockito.spy(Owner.class);

		// expectation for Mock petTimedCache in setup
		Mockito.doNothing().when(spyOwner).addPet(any(Pet.class));

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.savePet(null, spyOwner));
		Mockito.verify(logger, times(0)).info(Mockito.isA(String.class), anyInt());
	}


	// State Verification + Behavioural Verification
	// One Mock One Stub
	@Test
	void getOwnerPets_ExistingOwner_SuccessfullyReturnsPetsList(){

		// setup - data (Mock)
		int mockedOwnerId = 1;
		Owner mockedOwner = Mockito.mock(Owner.class);

		// expectation for Stub ownerRepository and Mock mockedOwner
		Mockito.doReturn(Arrays.asList(pets.get(2), pets.get(1))).when(mockedOwner).getPets();
		Mockito.doReturn(mockedOwner).when(ownerRepository).findById(mockedOwnerId);

		// exercise
		List<Pet> foundPets = petManager.getOwnerPets(mockedOwnerId);

		//verify
		assertEquals(2, foundPets.size());
		assertNotNull(foundPets.get(0));
		assertNotNull(foundPets.get(1));
		Mockito.verify(mockedOwner).getPets();
		Mockito.verify(logger, Mockito.atLeast(1)).info(Mockito.isA(String.class), Mockito.eq(mockedOwnerId));
	}

	// Behavioural Verification
	// one Stub
	@Test
	void getOwnerPets_InvalidOwner_ReturnsNull(){

		// setup - data
		int stubOwnerId = 1;

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(null).when(ownerRepository).findById(stubOwnerId);

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.getOwnerPets(1));
		// behavioural
		Mockito.verify(logger, times(2)).info(Mockito.anyString(), Mockito.eq(stubOwnerId));
	}

	// Behavioural Verification
	// one Stub
	@Test
	void getOwnerPetTypes_InvalidOwner_ReturnsNull(){

		// setup - data
		int stubOwnerId = 1;

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(null).when(ownerRepository).findById(stubOwnerId);

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.getOwnerPetTypes(stubOwnerId));
		Mockito.verify(logger, Mockito.atLeast(1)).info(Mockito.anyString(), Mockito.eq(stubOwnerId));
	}

	// State Verification
	// two Stubs
	@Test
	void getOwnerPetTypes_ValidStubbedOwner_EmptyPetsList_ReturnsEmptySet(){

		// setup - data (Stub Owner)
		int stubbedOwnerId = 1;
		Owner stubbedOwner = Mockito.mock(Owner.class);
		List<Pet> stubbedOwnerPetsList = new ArrayList<>();

		// expectation for Stub ownerRepository and Stub stubOwner
		Mockito.doReturn(stubbedOwner).when(ownerRepository).findById(stubbedOwnerId);
		Mockito.doReturn(stubbedOwnerPetsList).when(stubbedOwner).getPets();

		// exercise
		Set<PetType> petTypeSet = petManager.getOwnerPetTypes(stubbedOwnerId);

		//verify
		assertEquals(0, petTypeSet.size());
		Mockito.verify(logger, Mockito.atLeast(1)).info(Mockito.isA(String.class), Mockito.eq(stubbedOwnerId));
	}


	// State + Behavioural Verification
	// two Stubs
	@Test
	void getOwnerPetTypes_ValidStubbedOwner_PetsListEachDifferentPetType_ReturnsSuccessfully(){

		// setup - data (Owner Stubbed)
		int stubbedOwnerId = 1;
		Owner stubbedOwner = Mockito.mock(Owner.class);
		List<Pet> stubbedOwnerPetsList = new ArrayList<>();
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
		stubbedOwnerPetsList.add(Mockito.spy(pet1));
		stubbedOwnerPetsList.add(Mockito.spy(pet2));

		// expectation for Stub ownerRepository and Stub stubbedOwner
		Mockito.doReturn(stubbedOwner).when(ownerRepository).findById(stubbedOwnerId);
		Mockito.doReturn(stubbedOwnerPetsList).when(stubbedOwner).getPets();

		// exercise
		Set<PetType> petTypeSet = petManager.getOwnerPetTypes(stubbedOwnerId);

		//verify
		assertEquals(2, petTypeSet.size());
		// for testing for
		Mockito.verify(stubbedOwnerPetsList.get(0), times(1)).getType();
		Mockito.verify(stubbedOwnerPetsList.get(1), times(1)).getType();
		Mockito.verify(logger, Mockito.atLeastOnce()).info(Mockito.isA(String.class), Mockito.eq(stubbedOwnerId));
	}


	// State + Behavioural Verification
	// Two Stubs
	@Test
	void getOwnerPetTypes_ValidMockedOwner_PetsListSamePetType_ReturnsSuccessfully(){

		// setup - data (Owner Stub)
		int stubbedOwnerId = 1;
		Owner stubbedOwner = Mockito.mock(Owner.class);
		List<Pet> stubbedOwnerPetsList = new ArrayList<>();
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setName("pet1");
		PetType petType1 = DummyEntityGenerator.getNewDummyPetType();
		petType1.setName("petType1");
		pet1.setType(petType1);
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setName("pet2");
		pet2.setType(petType1);
		stubbedOwnerPetsList.add(Mockito.spy(pet1));
		stubbedOwnerPetsList.add(Mockito.spy(pet2));

		// expectation for Stub ownerRepository and Stub Owner
		Mockito.doReturn(stubbedOwner).when(ownerRepository).findById(stubbedOwnerId);
		Mockito.doReturn(stubbedOwnerPetsList).when(stubbedOwner).getPets();

		// exercise
		Set<PetType> petTypeSet = petManager.getOwnerPetTypes(stubbedOwnerId);

		//verify
		assertEquals(1, petTypeSet.size());
		// for testing for
		Mockito.verify(stubbedOwnerPetsList.get(0), times(1)).getType();
		Mockito.verify(stubbedOwnerPetsList.get(1), times(1)).getType();
		Mockito.verify(logger, Mockito.atLeastOnce()).info(Mockito.isA(String.class), Mockito.eq(stubbedOwnerId));
	}


	// State + Behavioural Verification
	// Dummy for Visit + Spy for pet + Mock for petTimedCache + Dummy for LocalDate
	@Test
	void getVisitsBetween_ValidPetId_ReturnsSuccessfully(){

		// setup - data (Dummy Visits + Dummy LocalDates)
		Visit visit1 = Mockito.mock(Visit.class);
		Visit visit2 = Mockito.mock(Visit.class);
		LocalDate l1 = LocalDate.now();
		LocalDate l2 = LocalDate.now();
		int petId = 1;
		List<Visit> petVisits = new ArrayList<>(Arrays.asList(visit1, visit2));

		// expectations on Spy Pet + Mock petTimedCache expectation in setup (pets in our map are spy)
		Mockito.doReturn(petVisits).when(pets.get(1)).getVisitsBetween(any(LocalDate.class), any(LocalDate.class));

		// exercise
		List<Visit> returnedVisits = petManager.getVisitsBetween(petId, l1, l2);

		// verify
		assertEquals(2, returnedVisits.size());
		assertTrue(returnedVisits.contains(visit1));
		assertTrue(returnedVisits.contains(visit2));
		Mockito.verify(pets.get(1)).getVisitsBetween(l1,l2);
		Mockito.verify(logger, times(1)).info(Mockito.isA(String.class), Mockito.eq(petId), Mockito.eq(l1), Mockito.eq(l2));
	}


	// Behavioural Verification
	// Mock for petTimedCache + Dummy for LocalDate
	@Test
	void getVisitsBetween_InvalidPetId_NullPointerException(){

		// setup - data (Dummy LocalDates)
		LocalDate l1 = LocalDate.now();
		LocalDate l2 = LocalDate.now();
		int petId = 3;

		// exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.getVisitsBetween(petId, l1, l2));
		Mockito.verify(logger, times(1)).info(Mockito.isA(String.class), Mockito.eq(petId), Mockito.eq(l1), Mockito.eq(l2));
	}

	// State + Behavioural Verification
	// Dummy for Visit + Mock for petTimedCache + Dummy for LocalDate + Spy for Pet
	@Test
	void getVisitsBetween_ValidPetId_NullDate_SuccessfullyReturns(){

		// setup - data (Dummy Visits + Dummy LocalDates)
		Visit visit1 = Mockito.mock(Visit.class);
		Visit visit2 = Mockito.mock(Visit.class);
		LocalDate l2 = LocalDate.now();
		int petId = 2;
		List<Visit> petVisits = new ArrayList<>(Arrays.asList(visit1, visit2));

		// expectations on Spy Pet + Mock petTimedCache expectation in setup (pets in our map are spy)
		Mockito.doReturn(petVisits).when(pets.get(2)).getVisitsBetween(Mockito.isNull(), any(LocalDate.class));

		// exercise
		List<Visit> returnedVisits = petManager.getVisitsBetween(petId, null, l2);

		// verify
		assertEquals(2, returnedVisits.size());
		assertTrue(returnedVisits.contains(visit1));
		assertTrue(returnedVisits.contains(visit2));
		Mockito.verify(pets.get(2)).getVisitsBetween(null,l2);
		Mockito.verify(logger, times(1)).info(Mockito.isA(String.class), Mockito.eq(petId), Mockito.isNull(), Mockito.eq(l2));

	}
}
