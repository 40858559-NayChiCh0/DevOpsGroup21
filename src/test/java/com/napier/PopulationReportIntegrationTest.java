
package com.napier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationReportIntegrationTest {

    private Connection connection;
    private PopulationReport report;

    /**
     * Connect to the real MySQL database
     * before each integration test.
     */
    @BeforeEach
    void setUp() throws SQLException {

        connection = DBConnection.getConnection();

        assertNotNull(connection,
                "Database connection should not be null");

        assertTrue(connection.isValid(2),
                "Database connection should be valid");

        report = new PopulationReport(connection);
    }

    /**
     * Close the database connection
     * after each integration test.
     */
    @AfterEach
    void tearDown() throws SQLException {

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Issue #50:
     * Integration test for population by continent.
     */
    @Test
    void testContinentReport() throws SQLException {

        List<PopulationBreakdown> results =
                report.getContinentReport();

        assertNotNull(results);
        assertFalse(results.isEmpty(),
                "Continent report should contain data");

        for (PopulationBreakdown population : results) {

            assertNotNull(population.getName());

            assertTrue(
                    population.getTotalPopulation() >= 0);

            assertTrue(
                    population.getUrbanPopulation() >= 0);
        }
    }

    /**
     * Issue #51:
     * Integration test for population by region.
     */
    @Test
    void testRegionReport() throws SQLException {

        List<PopulationBreakdown> results =
                report.getRegionReport();

        assertNotNull(results);
        assertFalse(results.isEmpty(),
                "Region report should contain data");

        for (PopulationBreakdown population : results) {

            assertNotNull(population.getName());

            assertTrue(
                    population.getTotalPopulation() >= 0);

            assertTrue(
                    population.getUrbanPopulation() >= 0);
        }
    }

    /**
     * Issue #52:
     * Integration test for population by country.
     */
    @Test
    void testCountryReport() throws SQLException {

        List<PopulationBreakdown> results =
                report.getCountryReport();

        assertNotNull(results);
        assertFalse(results.isEmpty(),
                "Country report should contain data");

        for (PopulationBreakdown population : results) {

            assertNotNull(population.getName());

            assertTrue(
                    population.getTotalPopulation() >= 0);

            assertTrue(
                    population.getUrbanPopulation() >= 0);
        }
    }

    /**
     * Integration test for all three reports.
     * Verifies that generateAllReports()
     * generates the three report sections.
     */
    @Test
    void testGenerateAllReports() throws SQLException {

        PrintStream originalOut = System.out;

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        try (PrintStream capture =
                     new PrintStream(
                             outputStream,
                             true,
                             StandardCharsets.UTF_8)) {

            System.setOut(capture);

            // Generate all three reports together.
            report.generateAllReports();

        } finally {

            // Restore original console output.
            System.setOut(originalOut);
        }

        String output = outputStream.toString(
                StandardCharsets.UTF_8);

        // Check continent report.
        assertTrue(output.contains(
                "Population Breakdown by Continent"));

        // Check region report.
        assertTrue(output.contains(
                "Population Breakdown by Region"));

        // Check country report.
        assertTrue(output.contains(
                "Population Breakdown by Country"));

        // Check table headers.
        assertTrue(output.contains("Total Population"));
        assertTrue(output.contains("Urban %"));
        assertTrue(output.contains("Rural %"));

        // Verify the reports appear in the correct order.
        int continentIndex = output.indexOf(
                "Population Breakdown by Continent");

        int regionIndex = output.indexOf(
                "Population Breakdown by Region");

        int countryIndex = output.indexOf(
                "Population Breakdown by Country");

        assertTrue(continentIndex < regionIndex);
        assertTrue(regionIndex < countryIndex);
        //Implemented by htss
    }
}
