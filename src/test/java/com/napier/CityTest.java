package com.napier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void testCityProperties(){
        City city = new City();
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

}
