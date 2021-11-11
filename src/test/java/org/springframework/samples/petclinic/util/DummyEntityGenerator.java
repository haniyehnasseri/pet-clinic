package org.springframework.samples.petclinic.util;

import org.springframework.samples.petclinic.model.*;

import java.time.LocalDate;
import java.util.Date;

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
		pet.setBirthDate(new Date());
		return pet;
	}
	public static Pet getAlreadySavedInDatabaseDummyPet(){
		Pet pet = new Pet();
		pet.setName("cat2");
		pet.setBirthDate(new Date());
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
