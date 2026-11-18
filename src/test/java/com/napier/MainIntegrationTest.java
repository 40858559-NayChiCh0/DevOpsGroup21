
package com.napier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests against the real MySQL world database.
 * Start the database before running this test class.
 */
@Tag("integration")
public class MainIntegrationTest {

    private static Connection connection;
    private static CityReports cityReports;
    private static TopNCityReports topNCityReports;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DBConnection.getConnection();

        assertNotNull(connection,
                "Database connection must not be null");

        assertTrue(connection.isValid(5),
                "MySQL must be reachable");

        cityReports = new CityReports();
        topNCityReports = new TopNCityReports();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    @Test
    void testMainMethod() throws Exception {

        // Capture the console output
        java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();

        java.io.PrintStream originalOut = System.out;

        try {
            System.setOut(
                    new java.io.PrintStream(output));

            Main.main(new String[]{});

        } finally {
            System.setOut(originalOut);
        }

        String result = output.toString();

        assertTrue(result.contains(
                "Database connection succeeded."));

        assertTrue(result.contains(
                "Country Reports"));

        assertTrue(result.contains(
                "City Reports"));

        assertTrue(result.contains(
                "Top N Populated Cities Reports"));

    }

    // Test 1: Check world database connection and data
    @Test
    void testWorldDatabaseHasCities() throws SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "SELECT COUNT(*) FROM city");
             ResultSet result = statement.executeQuery()) {

            assertTrue(result.next());

            assertTrue(result.getLong(1) > 0,
                    "The world database should have cities");
        }
    }

    // Test 2: All cities in the world
    @Test
    void testGetAllCities() {

        List<City> cities =
                cityReports.getAllCities(connection);

        assertNotNull(cities);
        assertFalse(cities.isEmpty(),
                "Expected populated world cities");

        assertNotNull(cities.get(0).getName());
        assertNotNull(cities.get(0).getCountry());

        assertPopulationDescending(cities);
    }

    // Test 3: Cities in Asia
    @Test
    void testGetCitiesByContinent() {

        List<City> cities =
                cityReports.getCitiesByContinent(
                        connection, "Asia");

        assertFalse(cities.isEmpty(),
                "Asia should contain cities");

        assertPopulationDescending(cities);
    }

    // Test 4: Cities in Southeast Asia
    @Test
    void testGetCitiesByRegion() {

        List<City> cities =
                cityReports.getCitiesByRegion(
                        connection, "Southeast Asia");

        assertFalse(cities.isEmpty(),
                "Southeast Asia should contain cities");

        assertPopulationDescending(cities);
    }

    // Test 5: Cities in Myanmar
    @Test
    void testGetCitiesByCountry() {

        List<City> cities =
                cityReports.getCitiesByCountry(
                        connection, "Myanmar");

        assertFalse(cities.isEmpty(),
                "Myanmar should contain cities");

        assertTrue(cities.stream().allMatch(city ->
                city.getCountry() != null &&
                        "Myanmar".equals(
                                city.getCountry().getName())));

        assertPopulationDescending(cities);
    }

    // Test 6: Cities in California district
    @Test
    void testGetCitiesByDistrict() {

        List<City> cities =
                cityReports.getCitiesByDistrict(
                        connection, "California");

        assertFalse(cities.isEmpty(),
                "California should contain cities");

        assertTrue(cities.stream().allMatch(city ->
                "California".equals(city.getDistrict())));

        assertPopulationDescending(cities);
    }

    // Test 7: Top 5 populated cities in the world
    @Test
    void testTopFiveCitiesInWorld() {

        List<City> cities =
                topNCityReports.getTopNCitiesInWorld(
                        connection, 5);

        assertEquals(5, cities.size());

        assertPopulationDescending(cities);
    }

    // Test 8: Top 5 populated cities in Asia
    @Test
    void testTopFiveCitiesInAsia() {

        List<City> cities =
                topNCityReports.getTopNCitiesByContinent(
                        connection, "Asia", 5);

        assertEquals(5, cities.size());

        assertPopulationDescending(cities);
    }

    // Test 9: Top 5 populated cities in Southeast Asia
    @Test
    void testTopFiveCitiesInSoutheastAsia()
            throws SQLException {

        List<City> cities =
                topNCityReports.getTopNCitiesByRegion(
                        connection, "Southeast Asia", 5);

        assertEquals(5, cities.size());

        assertPopulationDescending(cities);
    }

    // Test 10: Top 5 populated cities in Myanmar
    @Test
    void testTopFiveCitiesInMyanmar()
            throws SQLException {

        List<City> cities =
                topNCityReports.getTopNCitiesByCountry(
                        connection, "Myanmar", 5);

        assertEquals(5, cities.size());

        assertTrue(cities.stream().allMatch(city ->
                city.getCountry() != null &&
                        "Myanmar".equals(
                                city.getCountry().getName())));

        assertPopulationDescending(cities);
    }

    // Test 11: Top 5 populated cities in California
    @Test
    void testTopFiveCitiesInCalifornia()
            throws SQLException {

        List<City> cities =
                topNCityReports.getTopNCitiesByDistrict(
                        connection, "California", 5);

        assertEquals(5, cities.size());

        assertTrue(cities.stream().allMatch(city ->
                "California".equals(city.getDistrict())));

        assertPopulationDescending(cities);
    }

    // Test 12: Invalid top N value
    @Test
    void testInvalidTopN() {

        assertThrows(
                IllegalArgumentException.class,
                () -> topNCityReports.getTopNCitiesInWorld(
                        connection, 0));
    }

    // Helper method: Verify population order
    private static void assertPopulationDescending(
            List<City> cities) {

        for (int i = 1; i < cities.size(); i++) {

            assertTrue(
                    cities.get(i - 1).getPopulation()
                            >= cities.get(i).getPopulation(),
                    "Cities should be sorted by descending "
                            + "population at index " + i);
        }
    }


    // ================================================
    // LOOKUP POPULATION INTEGRATION TESTS
    // ================================================

    // Test 1: World population
    @Test
    void testWorldPopulation() throws SQLException {

        LookUp_Population.getWorldPopulation(connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM country");
    }

    // Test 2: Continent population
    @Test
    void testContinentPopulation() throws SQLException {

        LookUp_Population.getContinentPopulation(
                "Asia", connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM country WHERE Continent = 'Asia'");
    }

    // Test 3: Region population
    @Test
    void testRegionPopulation() throws SQLException {

        LookUp_Population.getRegionPopulation(
                "Southeast Asia", connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM country WHERE Region = 'Southeast Asia'");
    }

    // Test 4: Country population
    @Test
    void testCountryPopulation() throws SQLException {

        LookUp_Population.getCountryPopulation(
                "Myanmar", connection);

        assertPopulationExists(
                "SELECT Population FROM country WHERE Name = 'Myanmar'");
    }

    // Test 5: District population
    @Test
    void testDistrictPopulation() throws SQLException {

        LookUp_Population.getDistrictPopulation(
                "Mandalay", connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM city WHERE District = 'Mandalay'");
    }

    // Test 6: City population
    @Test
    void testCityPopulation() throws SQLException {

        LookUp_Population.getCityPopulation(
                "London", connection);

        assertPopulationExists(
                "SELECT Population FROM city WHERE Name = 'London'");
    }

    // Test 7: Invalid continent
    @Test
    void testInvalidContinentPopulation() {

        LookUp_Population.getContinentPopulation(
                "InvalidContinent", connection);
    }

    // Test 8: Invalid region
    @Test
    void testInvalidRegionPopulation() {

        LookUp_Population.getRegionPopulation(
                "InvalidRegion", connection);
    }

    // Test 9: Invalid country
    @Test
    void testInvalidCountryPopulation() {

        LookUp_Population.getCountryPopulation(
                "InvalidCountry", connection);
    }

    // Test 10: Invalid district
    @Test
    void testInvalidDistrictPopulation() {

        LookUp_Population.getDistrictPopulation(
                "InvalidDistrict", connection);
    }

    // Test 11: Invalid city
    @Test
    void testInvalidCityPopulation() {

        LookUp_Population.getCityPopulation(
                "InvalidCity", connection);
    }

    // Test 12: Print population report
    @Test
    void testPrintPopulationReport() {

        LookUp_Population.printPopulationReport(
                "Myanmar",
                50000000,
                "TEST POPULATION",
                "Country");
    }

    // Test 13: Print population report with null name
    @Test
    void testPrintPopulationReportWithNullName() {

        LookUp_Population.printPopulationReport(
                null,
                0,
                "TEST POPULATION",
                "Country");
    }

    // Helper method: Verify population exists
    private void assertPopulationExists(String sql)
            throws SQLException {

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            assertTrue(rs.next());

            assertTrue(rs.getLong(1) > 0,
                    "Population should be greater than zero");
        }
    }

}
