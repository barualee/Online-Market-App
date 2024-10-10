package com.onlinemarket.OnlinemarketProjectBackend.setting.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.onlinemarket.OnlinemarketProjectBackend.setting.country.CountryRepository;
import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;
import com.onlinemarket.OnlinemarketProjectCommon.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllertests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    StateRepository stateRepo;

    @Test
    @WithMockUser(username="lee", password="abcd@1234", roles = "Admin")
    public void testListByCountries() throws Exception {
        Integer countryId = 1;

        String url = "/states/list_by_country/" + countryId;

        MvcResult result = mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();

        State[] states = objectMapper.readValue(jsonResponse, State[].class);
        assertThat(states).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username="lee", password="abcd@1234", roles = "Admin")
    public void testCreateState() throws Exception {
        Integer countryId = 2;
        Country country = countryRepo.findById(countryId).get();
        State state = new State("Quebec", country);

        String url = "/states/save";

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
            .content(objectMapper.writeValueAsString(state))
            .with(csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();
        Integer stateId = Integer.parseInt(jsonResponse);
        Optional<State> findById = stateRepo.findById(stateId);

        assertThat(findById.isPresent());
    }

}
