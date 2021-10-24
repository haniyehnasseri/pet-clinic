/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.owner;

import java.text.ParseException;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.util.DummyEntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

/**
 * Test class for {@link PetTypeFormatter}
 *
 * @author Colin But
 */
@ExtendWith(MockitoExtension.class)
class PetTypeFormatterTests {

	@Mock
	private PetRepository pets;

	private PetTypeFormatter petTypeFormatter;

	@BeforeEach
	void setup() {
		this.petTypeFormatter = new PetTypeFormatter(pets);
	}

	@Test
	void testPrint() {
		PetType petType = new PetType();
		petType.setName("Hamster");
		String petTypeName = this.petTypeFormatter.print(petType, Locale.ENGLISH);
		assertThat(petTypeName).isEqualTo("Hamster");
	}

	@Test
	void shouldParse() throws ParseException {
		given(this.pets.findPetTypes()).willReturn(makePetTypes());
		PetType petType = petTypeFormatter.parse("Bird", Locale.ENGLISH);
		assertThat(petType.getName()).isEqualTo("Bird");
	}


	@Test
	void shouldThrowParseException() throws ParseException {
		given(this.pets.findPetTypes()).willReturn(makePetTypes());
		Assertions.assertThrows(ParseException.class, () -> {
			petTypeFormatter.parse("Fish", Locale.ENGLISH);
		});
	}



	/**
	 * Helper method to produce some sample pet types just for test purpose
	 * @return {@link Collection} of {@link PetType}
	 */
	private List<PetType> makePetTypes() {
		List<PetType> petTypes = new ArrayList<>();
		petTypes.add(new PetType() {
			{
				setName("Dog");
			}
		});
		petTypes.add(new PetType() {
			{
				setName("Bird");
			}
		});
		return petTypes;
	}


	/* Start */


	//State
	@Test
	void parse_ValidText_State_ParseException(){
		// setup - data (Spy petTypes)
		PetType petType1 = DummyEntityGenerator.getNewDummyPetType();
		petType1.setName("petType1");
		PetType petType2 = DummyEntityGenerator.getNewDummyPetType();
		petType2.setName("petType2");
		String text = "dog";

		// expectations
		Mockito.doReturn(Arrays.asList(Mockito.spy(petType1), Mockito.spy(petType2))).when(pets).findPetTypes();

		// exercise & verify
		ParseException ex = Assertions.assertThrows(ParseException.class, () -> petTypeFormatter.parse(text, null));
		Assertions.assertEquals("type not found: dog", ex.getMessage());
	}


	//Behaviour
	@Test
	void parse_ValidText_Behaviour_ParseException(){
		// setup - data (Spy petTypes)
		PetType petType1 = DummyEntityGenerator.getNewDummyPetType();
		petType1.setName("petType1");
		PetType petType2 = DummyEntityGenerator.getNewDummyPetType();
		petType2.setName("petType2");
		List<PetType> petTypes = new ArrayList<>();
		petTypes.add(Mockito.spy(petType1));
		petTypes.add(Mockito.spy(petType2));
		String text = "dog";

		// expectations
		Mockito.doReturn(petTypes).when(pets).findPetTypes();

		// exercise & verify
		ParseException ex = Assertions.assertThrows(ParseException.class, () -> petTypeFormatter.parse(text, null));
		Mockito.verify(petTypes.get(0), times(1)).getName();
		Mockito.verify(petTypes.get(1), times(1)).getName();
	}


	//State
	@Test
	void parse_ValidText_State_Founded() throws ParseException {
		// setup - data (Spy petTypes)
		PetType petType1 = DummyEntityGenerator.getNewDummyPetType();
		petType1.setName("petType1");
		PetType petType2 = DummyEntityGenerator.getNewDummyPetType();
		petType2.setName("dog");
		List<PetType> petTypes = new ArrayList<>();
		petTypes.add(Mockito.spy(petType1));
		petTypes.add(Mockito.spy(petType2));
		String text = "dog";

		// expectations
		Mockito.doReturn(petTypes).when(pets).findPetTypes();

		// exercise
		PetType foundPetType = petTypeFormatter.parse(text, null);

		//verify
		Assertions.assertTrue(petTypes.contains(foundPetType));
		Assertions.assertNotNull(foundPetType);
	}

	//Behaviour
	@Test
	void parse_ValidText_Behaviour_Founded() throws ParseException {
		// setup - data (Spy petTypes)
		PetType petType1 = DummyEntityGenerator.getNewDummyPetType();
		petType1.setName("dog");
		PetType petType2 = DummyEntityGenerator.getNewDummyPetType();
		petType2.setName("petType1");
		List<PetType> petTypes = new ArrayList<>();
		petTypes.add(Mockito.spy(petType1));
		petTypes.add(Mockito.spy(petType2));
		String text = "dog";

		// expectations
		Mockito.doReturn(petTypes).when(pets).findPetTypes();

		// exercise
		PetType foundPetType = petTypeFormatter.parse(text, null);

		//verify
		Mockito.verify(petTypes.get(0), times(1)).getName();
		Mockito.verify(petTypes.get(1), times(0)).getName();
	}

	/* Finish */
}
