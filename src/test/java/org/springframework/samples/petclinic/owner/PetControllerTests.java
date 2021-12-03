package org.springframework.samples.petclinic.owner;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {PetController.class})
@AutoConfigureMockMvc(addFilters = false)
class PetControllerTests {
	// TODO
	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	@MockBean
	private PetRepository pets;

	@MockBean
	private OwnerRepository owners;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	private Owner spyOwner;

	@BeforeEach
	void setup() {
	}

	@Test
	void initCreationFormTest() throws Exception {
		spyOwner = Mockito.spy(Owner.class);
		Mockito.doReturn(spyOwner).when(petService).findOwner(Mockito.anyInt());
		Pet dummyPet = new Pet();
		Mockito.doReturn(dummyPet).when(petService).newPet(Mockito.any(Owner.class));
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/owners/1/pets/new")).andExpect(
				MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("pet", dummyPet))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM)).andDo(print())
			.andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("<html>"));
	}

	@Test
	void processCreationFormTest_ValidPet_HasName_New_OwnerHasIt() throws Exception {
		// first if enters
		spyOwner = Mockito.spy(Owner.class);
		Mockito.doReturn(Mockito.mock(Pet.class)).when(spyOwner).getPet(anyString(), Mockito.eq(true));
		Mockito.doReturn(spyOwner).when(petService).findOwner(Mockito.anyInt());
		// valid
		Pet pet = new Pet();
		pet.setName("pet");
		pet.setBirthDate(LocalDate.now());
		pet.setType(Mockito.mock(PetType.class));
		mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/new").flashAttr("pet", pet)).andExpect(
				MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("pet", pet))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));

		Mockito.verify(spyOwner).addPet(Mockito.isA(Pet.class));
	}

	@Test
	void processCreationFormTest_ValidPet_NameNotEmpty_New_OwnerDoesntHaveIt() throws Exception {
		// first if doesn't enter
		spyOwner = Mockito.spy(Owner.class);
		Mockito.doReturn(null).when(spyOwner).getPet(anyString(), Mockito.eq(true));
		Mockito.doReturn(spyOwner).when(petService).findOwner(Mockito.anyInt());
		// valid pet
		Pet pet = new Pet();
		pet.setName("pet");
		pet.setType(Mockito.mock(PetType.class));
		pet.setBirthDate(LocalDate.now());
		Mockito.doNothing().when(petService).savePet(Mockito.any(Pet.class), Mockito.any(Owner.class));
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/new").flashAttr("pet", pet)).andExpect(
			MockMvcResultMatchers.status().is3xxRedirection()).andDo(print()).andReturn();

		Assertions.assertNull(Objects.requireNonNull(result.getModelAndView()).getModel().get("pet"));
		Assertions.assertTrue(Objects.requireNonNull(result.getResponse().getRedirectedUrl()).contains("/owners/1"));
		Mockito.verify(petService).savePet(Mockito.any(Pet.class), Mockito.any(Owner.class));
		Mockito.verify(spyOwner).addPet(Mockito.isA(Pet.class));
	}

	@Test
	void initUpdateFormTest() throws Exception {
		Pet dummyPet = new Pet();
		dummyPet.setName("pet");
		Mockito.doReturn(dummyPet).when(petService).findPet(1);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/owners/1/pets/1/edit")).andExpect(
				MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("pet", dummyPet))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM)).andDo(print())
			.andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("<html>"));
	}

	@Test
	void processUpdateForm_ValidPet() throws Exception {
		spyOwner = Mockito.spy(Owner.class);
		Mockito.doReturn(spyOwner).when(petService).findOwner(Mockito.anyInt());
		// valid pet
		Pet pet = new Pet();
		pet.setName("pet");
		pet.setType(Mockito.mock(PetType.class));
		pet.setBirthDate(LocalDate.now());
		Mockito.doNothing().when(petService).savePet(Mockito.any(Pet.class), Mockito.any(Owner.class));
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/1/edit").flashAttr("pet", pet)).andExpect(
			MockMvcResultMatchers.status().is3xxRedirection()).andDo(print()).andReturn();

		Assertions.assertNull(Objects.requireNonNull(result.getModelAndView()).getModel().get("pet"));
		Assertions.assertTrue(Objects.requireNonNull(result.getResponse().getRedirectedUrl()).contains("/owners/1"));
		Mockito.verify(petService).savePet(Mockito.any(Pet.class), Mockito.any(Owner.class));

	}


	@Test
	void processUpdateForm_InValidPet() throws Exception {
		spyOwner = Mockito.spy(Owner.class);
		Mockito.doReturn(spyOwner).when(petService).findOwner(Mockito.anyInt());
		// Invalid pet
		Pet pet = new Pet();
		pet.setName("pet");
		pet.setBirthDate(LocalDate.now());
		Pet spyPet = Mockito.spy(pet);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/1/edit").flashAttr("pet", spyPet)).andExpect(
				MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("pet", spyPet))
			.andExpect(MockMvcResultMatchers.view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM)).andDo(print()).andReturn();

		Assertions.assertNotNull(Objects.requireNonNull(result.getModelAndView()).getModel().get("pet"));
		Mockito.verify(spyPet).setOwner(spyOwner);

	}

}
