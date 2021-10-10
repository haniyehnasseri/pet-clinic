package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Haniyeh Nasseri
 */

@RunWith(Parameterized.class)
public class PetServiceTest {

	public Integer searchingId;

	public String petName;

	public PetServiceTest(Integer searchingId, String petName){
		this.searchingId = searchingId;
		this.petName = petName;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList (new Object [][] {{1,"cat1"},{3,"cat3"}});
	}

    @Test
    public void findPet() {
    }
}
