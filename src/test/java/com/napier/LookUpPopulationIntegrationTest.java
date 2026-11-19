
package com.napier;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class LookUpPopulationIntegrationTest {

    private static Connection connection;

    // Connect to the real World database
    @BeforeAll
    static void setUp() throws SQLException {
        connection = DBConnection.getConnection();
    }

    // Close database connection
    @AfterAll
    static void tearDown() throws SQLException {

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    // ================================================
    // LOOKUP POPULATION INTEGRATION TESTS
    // ================================================

    // Test 1: World population
    @Test
    void testWorldPopulation() throws SQLException {

        LookUpPopulation.getWorldPopulation(connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM country");
    }

    // Test 2: Continent population
    @Test
    void testContinentPopulation() throws SQLException {

        LookUpPopulation.getContinentPopulation(
                "Asia", connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM country WHERE Continent = 'Asia'");
    }

    // Test 3: Region population
    @Test
    void testRegionPopulation() throws SQLException {

        LookUpPopulation.getRegionPopulation(
                "Southeast Asia", connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM country WHERE Region = 'Southeast Asia'");
    }

    // Test 4: Country population
    @Test
    void testCountryPopulation() throws SQLException {

        LookUpPopulation.getCountryPopulation(
                "Myanmar", connection);

        assertPopulationExists(
                "SELECT Population FROM country WHERE Name = 'Myanmar'");
    }

    // Test 5: District population
    @Test
    void testDistrictPopulation() throws SQLException {

        LookUpPopulation.getDistrictPopulation(
                "Mandalay", connection);

        assertPopulationExists(
                "SELECT SUM(Population) FROM city WHERE District = 'Mandalay'");
    }

    // Test 6: City population
    @Test
    void testCityPopulation() throws SQLException {

        LookUpPopulation.getCityPopulation(
                "London", connection);

        assertPopulationExists(
                "SELECT Population FROM city WHERE Name = 'London'");
    }

    // ================================================
    // INVALID INPUT INTEGRATION TESTS
    // ================================================

    // Test 7: Invalid continent
    @Test
    void testInvalidContinentPopulation() {

        LookUpPopulation.getContinentPopulation(
                "InvalidContinent", connection);
    }

    // Test 8: Invalid region
    @Test
    void testInvalidRegionPopulation() {

        LookUpPopulation.getRegionPopulation(
                "InvalidRegion", connection);
    }

    // Test 9: Invalid country
    @Test
    void testInvalidCountryPopulation() {

        LookUpPopulation.getCountryPopulation(
                "InvalidCountry", connection);
    }

    // Test 10: Invalid district
    @Test
    void testInvalidDistrictPopulation() {

        LookUpPopulation.getDistrictPopulation(
                "InvalidDistrict", connection);
    }

    // Test 11: Invalid city
    @Test
    void testInvalidCityPopulation() {

        LookUpPopulation.getCityPopulation(
                "InvalidCity", connection);
    }

    // ================================================
    // POPULATION REPORT TESTS
    // ================================================

    // Test 12: Print population report
    @Test
    void testPrintPopulationReport() {

        LookUpPopulation.printPopulationReport(
                "Myanmar",
                50000000,
                "TEST POPULATION",
                "Country");
    }

    // Test 13: Print population report with null name
    @Test
    void testPrintPopulationReportWithNullName() {

        LookUpPopulation.printPopulationReport(
                null,
                0,
                "TEST POPULATION",
                "Country");
    }

    // ================================================
    // HELPER METHOD
    // ================================================

    // Verify that population exists in the database
    private void assertPopulationExists(String sql)
            throws SQLException {

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            assertTrue(rs.next(),
                    "Population record should exist");

            assertTrue(rs.getLong(1) > 0,
                    "Population should be greater than zero");
        }
    }
}
