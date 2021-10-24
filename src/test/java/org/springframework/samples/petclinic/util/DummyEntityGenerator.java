package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;

import java.time.LocalDate;

/**
 * @author Haniyeh Nasseri
 */
public class DummyEntityGenerator {
	public static PetType getNewDummyPetType(){
		PetType petType =  new PetType();
		petType.setName("cat");
		return petType;
	}
	public static Pet getNewDummyPet(){
		Pet pet = new Pet();
		pet.setName("cat1");
		pet.setBirthDate(LocalDate.of(2021, 10, 8));
		return pet;
	}
	public static Pet getAlreadySavedInDatabaseDummyPet(){
		Pet pet = new Pet();
		pet.setName("cat2");
		pet.setBirthDate(LocalDate.of(2020, 10, 8));
		pet.setId(1);
		return pet;
	}
	public static Owner getNewDummyOwner(){
		Owner owner = new Owner();
		owner.setTelephone("123");
		owner.setFirstName("firstName");
		owner.setLastName("lastName");
		owner.setAddress("address");
		owner.setCity("city");
		return owner;
	}
}
