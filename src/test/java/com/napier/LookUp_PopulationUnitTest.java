
package com.napier;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LookUp_PopulationUnitTest {

    private static Connection connection;

    // Capture console output
    private String captureOutput(Runnable report) {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (PrintStream testOutput = new PrintStream(output)) {

            System.setOut(testOutput);
            report.run();

        } finally {
            System.setOut(originalOut);
        }

        return output.toString();
    }

    // Connect to database before tests
    @BeforeAll
    static void setUp() throws Exception {
        connection = DBConnection.getConnection();
    }

    // Close database connection after tests
    @AfterAll
    static void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    // Test 1: World population
    @Test
    void testGetWorldPopulation() {

        String output = captureOutput(
                () -> LookUp_Population.getWorldPopulation(connection)
        );

        assertTrue(output.contains("WORLD POPULATION"));
    }

    // Test 2: Continent population
    @Test
    void testGetContinentPopulation() {

        String output = captureOutput(
                () -> LookUp_Population.getContinentPopulation(
                        "Asia", connection)
        );

        assertTrue(output.contains("CONTINENT POPULATION"));
        assertTrue(output.contains("Asia"));
    }

    // Test 3: Region population
    @Test
    void testGetRegionPopulation() {

        String output = captureOutput(
                () -> LookUp_Population.getRegionPopulation(
                        "Southeast Asia", connection)
        );

        assertTrue(output.contains("REGION POPULATION"));
    }

    // Test 4: Country population
    @Test
    void testGetCountryPopulation() {

        String output = captureOutput(
                () -> LookUp_Population.getCountryPopulation(
                        "Myanmar", connection)
        );

        assertTrue(output.contains("COUNTRY POPULATION"));
    }

    // Test 5: District population
    @Test
    void testGetDistrictPopulation() {

        String output = captureOutput(
                () -> LookUp_Population.getDistrictPopulation(
                        "California", connection)
        );

        assertTrue(output.contains("DISTRICT POPULATION"));
    }

    // Test 6: City population
    @Test
    void testGetCityPopulation() {

        String output = captureOutput(
                () -> LookUp_Population.getCityPopulation(
                        "London", connection)
        );

        assertTrue(output.contains("CITY POPULATION"));
    }

    // Test 7: Print population report with normal data
    @Test
    void testPrintWithData() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "Myanmar",
                        54000000L,
                        "COUNTRY POPULATION",
                        "Country"
                )
        );

        assertTrue(output.contains("Myanmar"));
        assertTrue(output.contains("54,000,000"));
        assertTrue(output.contains("COUNTRY POPULATION"));
    }

    // Test 8: Print population report with null name
    @Test
    void testPrintWithNullName() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        null, 0,
                        "COUNTRY POPULATION",
                        "Country"
                )
        );

        assertTrue(output.contains("No population data found."));
    }

    // Test 9: Print population report with zero population
    @Test
    void testPrintWithZeroPopulation() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "Test Country", 0,
                        "COUNTRY POPULATION",
                        "Country"
                )
        );

        assertTrue(output.contains("Test Country"));
        assertTrue(output.contains("0"));
    }

    // Test 10: Print population report with null title
    @Test
    void testPrintWithNullTitle() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "Myanmar", 54000000L,
                        null, "Country"
                )
        );

        assertTrue(output.contains("Myanmar"));
        assertTrue(output.contains("54,000,000"));
    }

    // Test 11: Print population report with null column name
    @Test
    void testPrintWithNullColumnName() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "Myanmar", 54000000L,
                        "COUNTRY POPULATION", null
                )
        );

        assertTrue(output.contains("Myanmar"));
        assertTrue(output.contains("COUNTRY POPULATION"));
    }

    // Test 12: Print world population
    @Test
    void testPrintWorldPopulation() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "World", 8000000000L,
                        "WORLD POPULATION", "World"
                )
        );

        assertTrue(output.contains("World"));
        assertTrue(output.contains("8,000,000,000"));
    }

    // Test 13: Print continent population
    @Test
    void testPrintContinentPopulation() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "Asia", 4600000000L,
                        "CONTINENT POPULATION", "Continent"
                )
        );

        assertTrue(output.contains("Asia"));
    }

    // Test 14: Print region population
    @Test
    void testPrintRegionPopulation() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "Southeast Asia", 680000000L,
                        "REGION POPULATION", "Region"
                )
        );

        assertTrue(output.contains("Southeast Asia"));
    }

    // Test 15: Print district population
    @Test
    void testPrintDistrictPopulation() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "California", 39000000L,
                        "DISTRICT POPULATION", "District"
                )
        );

        assertTrue(output.contains("California"));
    }

    // Test 16: Print city population
    @Test
    void testPrintCityPopulation() {

        String output = captureOutput(() ->
                LookUp_Population.printPopulationReport(
                        "London", 8982000L,
                        "CITY POPULATION", "City"
                )
        );

        assertTrue(output.contains("London"));
    }
}
