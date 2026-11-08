package com.napier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    City city = new City();

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
}
