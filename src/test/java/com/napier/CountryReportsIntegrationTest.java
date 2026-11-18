
package com.napier;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class CountryReportsIntegrationTest {

    private String captureOutput(Runnable report) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output));
            report.run();
        } finally {
            System.setOut(originalOut);
        }

        return output.toString();
    }

    @Test
    void testDatabaseConnection() throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }

    @Test
    void testGetAllCountries() {
        String result = captureOutput(
                CountryReports::getAllCountries);

        assertTrue(result.contains("China"));
        assertTrue(result.contains("India"));
    }

    @Test
    void testGetCountriesByContinent() {
        String result = captureOutput(
                () -> CountryReports.getCountriesByContinent("Asia"));

        assertTrue(result.contains("Asia"));
        assertTrue(result.contains("China"));
        assertFalse(result.contains("France"));
    }

    @Test
    void testGetCountriesByRegion() {
        String result = captureOutput(
                () -> CountryReports.getCountriesByRegion("Southeast Asia"));

        assertTrue(result.contains("Myanmar"));
        assertTrue(result.contains("Thailand"));
    }

    @Test
    void testGetTopNPopulatedCountries() {
        String result = captureOutput(
                () -> CountryReports.getTopNPopulatedCountries(10));

        assertTrue(result.contains("China"));
        assertTrue(result.contains("India"));
    }

    @Test
    void testGetTopNPopulatedCountriesByContinent() {
        String result = captureOutput(
                () -> CountryReports.getTopNPopulatedCountriesByContinent(
                        "Asia", 10));

        assertTrue(result.contains("China"));
        assertTrue(result.contains("India"));
    }

    @Test
    void testGetTopNPopulatedCountriesByRegion() {
        String result = captureOutput(
                () -> CountryReports.getTopNPopulatedCountriesByRegion(
                        "Southeast Asia", 10));

        assertTrue(result.contains("Myanmar"));
        assertTrue(result.contains("Thailand"));
    }

    @Test
    void testInvalidContinent() {
        String result = captureOutput(
                () -> CountryReports.getCountriesByContinent(""));

        assertTrue(result.contains("Invalid continent"));
    }

    @Test
    void testInvalidRegion() {
        String result = captureOutput(
                () -> CountryReports.getCountriesByRegion(""));

        assertTrue(result.contains("Invalid region"));
    }

    @Test
    void testInvalidNumber() {
        String result = captureOutput(
                () -> CountryReports.getTopNPopulatedCountries(0));

        assertTrue(result.contains("greater than zero"));
    }

    @Test
    void testPrintAllReports() {
        String result = captureOutput(
                CountryReports::printAllReports);

        for (int i = 1; i <= 6; i++) {
            assertTrue(result.contains("Report " + i));
        }
    }
}
