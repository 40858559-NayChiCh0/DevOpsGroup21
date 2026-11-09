package com.napier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class CityReportsTest {
    private CityReports cityReport;
    @BeforeEach
    void setUp() {
        cityReport = new CityReports();
    }

    @Test
    void printCityReportTestNull() {
        cityReport.printCityReport(null,"TEST REPORT TITLE");
    }

    @Test
    void printCityReportTestEmpty() {
        ArrayList<City> cities = new ArrayList<>();
        cityReport.printCityReport(cities,"TEST REPORT TITLE");
    }

    @Test
    void printCityReportTestContainsNull() {

        ArrayList<City> cities = new ArrayList<>();
        cities.add(null);
        cityReport.printCityReport(cities,"TEST REPORT TITLE");
    }

    @Test
    void printCityReportTestNormal() {
        ArrayList<City> cities = new ArrayList<>();
        City city = new City();
        city.setName("Yangon");
        city.setDistrict("Yangon");

        Country country = new Country();
        country.setName("Myanmar");
        city.setPopulation(80000);
        cities.add(city);
    }
}