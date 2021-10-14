package org.springframework.samples.petclinic.owner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Haniyeh Nasseri
 */

@RunWith(Parameterized.class)
public class PetServiceWithMockitoTest {

	private static Map<Integer,Pet> pets = new HashMap<>();

	private static PetService petService;

	private static Pet pet1;

	private static Pet pet2;

	static {
		PetTimedCache mockedPetCache = Mockito.mock(PetTimedCache.class);
		petService = new PetService(mockedPetCache, null, LoggerFactory.getLogger("test"));
		pet1 = DummyEntityGenerator.getNewDummyPet();
		pet1.setId(1);
		pet1.setName("cat1");
		pets.put(1, pet1);
		pet2 = DummyEntityGenerator.getNewDummyPet();
		pet2.setId(2);
		pet2.setName("cat2");
		pets.put(2, pet2);
		Mockito.doAnswer(invocationOnMock -> pets.get((Integer) invocationOnMock.getArgument(0)))
			.when(mockedPetCache).get(Mockito.anyInt());
	}

	public Integer searchingId;

	public Pet petSearched;

	public PetServiceWithMockitoTest(Integer searchingId, Pet petSearched){
		this.searchingId = searchingId;
		this.petSearched = petSearched;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList (new Object [][] {{1,pet1},{2,pet2},{3, null}});
	}

	@Test
	public void findPet() {
		Assert.assertEquals(petSearched, petService.findPet(searchingId));
	}
}
