package com.napier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    static City city;
    @BeforeAll
    static void init() {
        city = new City();
    }


    @Test
    void testCityProperties(){
        city.setId(1);
        city.setDistrict("Rangoon");
        city.setCountryCode("MMR");
        city.setDistrict("Rangoon");
        city.setPopulation(10000);

        assertEquals(city.getId(), 1);
        assertEquals(city.getDistrict(), "Rangoon");
        assertEquals(city.getCountryCode(), "MMR");
        assertEquals(city.getDistrict(), "Rangoon");
        assertEquals(city.getPopulation(), 10000);
    }

    @Test
    void testCountryAssociation(){
        Country country = new Country();
        country.setName("Myanmar");
        city.setCountry(country);
        assertEquals("Myanmar",city.getCountry().getName());
    }


    @Test
    void testCityName() {
        city.setName("Yangon");
        assertEquals("Yangon", city.getName());
    }

    @Test
    void testCityCountryCode() {
        city.setCountryCode("MMR");
        assertEquals("MMR", city.getCountryCode());
    }

    @Test
    void testCityPopulation() {
        city.setPopulation(5000000);
        assertEquals(5000000, city.getPopulation());
    }

    @Test
    void testCityDistrict() {
        city.setDistrict("Yangon");
        assertEquals("Yangon", city.getDistrict());
    }

    @Test
    void testCityId() {
        city.setId(1);
        assertEquals(1, city.getId());
    }


}
