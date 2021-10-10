package org.springframework.samples.petclinic.owner.theory;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;

/**
 * @author Haniyeh Nasseri
 */

@RunWith(Theories.class)
public class OwnerTestTheory {

	private static Owner owner = DummyEntityGenerator.getNewDummyOwner();

	static {
		Pet pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setName("cat1");
		Pet pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setName("cat2");
		Pet pet3 = DummyEntityGenerator.getNewDummyPet();
		pet3.setName("cat3");
		owner.addPet(pet1);
		owner.addPet(pet2);
		owner.addPet(pet3);
		pet1.setId(1);
		pet2.setId(2);
	}


	@DataPoints
	public static boolean[] ignoreStates = {true, false};

	@DataPoints
	public static String[] names = {"cat1", "cat2", "cat3"};

	@Theory
	public void getNewPetTest(String name, boolean ignore){
		Assume.assumeTrue(name.equals("cat3"));
		if(ignore)
			Assert.assertNull(owner.getPet(name, true));
		else
			Assert.assertEquals("cat3", owner.getPet(name, false).getName());
	}

	@Theory
	public void getAlreadySavedInDatabasePetTest(String name, boolean ignore){
		Assume.assumeFalse(name.equals("cat3"));
		Assert.assertNotNull(owner.getPet(name, ignore));
		Assert.assertEquals(name, owner.getPet(name, false).getName());
	}


}
