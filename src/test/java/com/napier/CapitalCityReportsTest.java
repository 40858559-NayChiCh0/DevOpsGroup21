
package com.napier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class CapitalCityReportsTest {

    private CapitalCityReports reports;

    @BeforeEach
    void setUp() {
        // No database connection is needed for invalid N tests
        reports = new CapitalCityReports(null);
    }

    @FunctionalInterface
    interface TestAction {
        void run() throws SQLException;
    }

    // Capture the report output
    private String getOutput(TestAction action)
            throws SQLException {

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        PrintStream originalOut = System.out;

        try (PrintStream printStream =
                     new PrintStream(output)) {

            System.setOut(printStream);
            action.run();

        } finally {
            System.setOut(originalOut);
        }

        return output.toString();
    }

    // Unit Test 1: Top N world capital cities, N = 0
    @Test
    void testTopNWorldZero() throws SQLException {

        String output = getOutput(
                () -> reports.getTopNCapitalCities(0)
        );

        assertTrue(output.contains(
                "N must be greater than zero."));
    }

    // Unit Test 2: Top N world capital cities, N = -1
    @Test
    void testTopNWorldNegative() throws SQLException {

        String output = getOutput(
                () -> reports.getTopNCapitalCities(-1)
        );

        assertTrue(output.contains(
                "N must be greater than zero."));
    }

    // Unit Test 3: Top N continent capital cities, N = 0
    @Test
    void testTopNContinentZero() throws SQLException {

        String output = getOutput(
                () -> reports.getTopNCapitalCitiesByContinent(
                        "Asia", 0)
        );

        assertTrue(output.contains(
                "N must be greater than zero."));
    }

    // Unit Test 4: Top N continent capital cities, N = -1
    @Test
    void testTopNContinentNegative() throws SQLException {

        String output = getOutput(
                () -> reports.getTopNCapitalCitiesByContinent(
                        "Asia", -1)
        );

        assertTrue(output.contains(
                "N must be greater than zero."));
    }

    // Unit Test 5: Top N region capital cities, N = 0
    @Test
    void testTopNRegionZero() throws SQLException {

        String output = getOutput(
                () -> reports.getTopNCapitalCitiesByRegion(
                        "Southeast Asia", 0)
        );

        assertTrue(output.contains(
                "N must be greater than zero."));
    }

    // Unit Test 6: Top N region capital cities, N = -1
    @Test
    void testTopNRegionNegative() throws SQLException {

        String output = getOutput(
                () -> reports.getTopNCapitalCitiesByRegion(
                        "Southeast Asia", -1)
        );

        assertTrue(output.contains(
                "N must be greater than zero."));
    }
}
