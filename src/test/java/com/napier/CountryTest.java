package com.napier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {
    Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
    }

    @Test
    void testGetCode(){
        country.setCode("USA");
        assertEquals("USA", country.getCode());
    }

    @Test
    void testGetName(){
        country.setName("United States");
        assertEquals("United States", country.getName());
    }

    @Test
    void testGetContinent(){
        country.setContinent("North America");
        assertEquals("North America", country.getContinent());
    }

    @Test
    void testGetRegion(){
        country.setRegion("Texas");
        assertEquals("Texas", country.getRegion());
    }
    @Test
    void testGetPopulation(){
        country.setPopulation(10000);
        assertEquals(10000, country.getPopulation());
    }
}