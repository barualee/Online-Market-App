package com.onlinemarket.OnlinemarketProjectBackend.setting.country;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.onlinemarket.OnlinemarketProjectCommon.entity.Country;

//it is not unit test, it is intuition test
@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository countryRepo;
    
    @Test
    @WithMockUser(username="lee", password="abcd", roles = "Admin")
    public void testListCountries() throws Exception {

        String url = "/countries/list";

        MvcResult result = mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString(); 
        System.out.println(jsonResponse);

        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
        for(Country country : countries){
            System.out.println(country);
        }

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username="lee", password="abcd@1234", roles = "Admin")
    public void testCreateCountry() throws Exception {

        String url = "/countries/save";

        String countryName = "United Kingdom";
        Country ctry = new Country(countryName, "UK");

        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
            .content(objectMapper.writeValueAsString(ctry))
            .with(csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn();
        
        String jsonResponse = result.getResponse().getContentAsString();
        
        System.out.println("Country ID: "+ jsonResponse);
        Integer countryId = Integer.parseInt(jsonResponse);
        Optional<Country> findById = countryRepo.findById(countryId);

        assertThat(findById.isPresent());
        Country saved = findById.get();
        assertThat(saved.getName()).isEqualTo(countryName);
    }
}
