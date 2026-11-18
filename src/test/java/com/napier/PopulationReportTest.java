
package com.napier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationReportTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PopulationReport report;

    @BeforeEach
    void setUp() {

        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));

        // A database connection is not required
        // because printReport() does not use JDBC.
        report = new PopulationReport(null);
    }

    @AfterEach
    void tearDown() {

        // Restore original console output.
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // Test 1: Print continent report
    @Test
    void testPrintContinentReport() {

        List<PopulationBreakdown> reports = List.of(
                new PopulationBreakdown(
                        "Asia", 1000, 600)
        );

        report.printReport(
                "Population Breakdown by Continent",
                reports
        );

        String output = outputStream.toString();

        assertTrue(output.contains(
                "Population Breakdown by Continent"));

        assertTrue(output.contains("Asia"));
        assertTrue(output.contains("60.00%"));
        assertTrue(output.contains("40.00%"));
    }

    // Test 2: Print region report
    @Test
    void testPrintRegionReport() {

        List<PopulationBreakdown> reports = List.of(
                new PopulationBreakdown(
                        "Southeast Asia", 500, 300)
        );

        report.printReport(
                "Population Breakdown by Region",
                reports
        );

        String output = outputStream.toString();

        assertTrue(output.contains(
                "Population Breakdown by Region"));

        assertTrue(output.contains("Southeast Asia"));
        assertTrue(output.contains("60.00%"));
        assertTrue(output.contains("40.00%"));
    }

    // Test 3: Print country report
    @Test
    void testPrintCountryReport() {

        List<PopulationBreakdown> reports = List.of(
                new PopulationBreakdown(
                        "Myanmar", 1000, 400)
        );

        report.printReport(
                "Population Breakdown by Country",
                reports
        );

        String output = outputStream.toString();

        assertTrue(output.contains(
                "Population Breakdown by Country"));

        assertTrue(output.contains("Myanmar"));
        assertTrue(output.contains("40.00%"));
        assertTrue(output.contains("60.00%"));
    }

    // Test 4: Empty report
    @Test
    void testEmptyReport() {

        report.printReport(
                "Empty Population Report",
                new ArrayList<>()
        );

        String output = outputStream.toString();

        assertTrue(output.contains(
                "No population data available."));
    }

    // Test 5: Invalid population
    @Test
    void testInvalidPopulationReport() {

        List<PopulationBreakdown> reports = List.of(
                new PopulationBreakdown(
                        "Invalid Country", 100, 200)
        );

        report.printReport(
                "Invalid Population Report",
                reports
        );

        String output = outputStream.toString();
        String error = errorStream.toString();

        assertTrue(output.contains("Invalid Country"));
        assertTrue(output.contains("N/A"));

        assertTrue(error.contains(
                "Warning: Invalid population data"));
    }

    // Test 6: Multiple report rows
    @Test
    void testMultipleRows() {

        List<PopulationBreakdown> reports = List.of(
                new PopulationBreakdown(
                        "Asia", 1000, 600),
                new PopulationBreakdown(
                        "Europe", 800, 500)
        );

        report.printReport(
                "Multiple Population Report",
                reports
        );

        String output = outputStream.toString();

        assertTrue(output.contains("Asia"));
        assertTrue(output.contains("Europe"));
        assertTrue(output.contains("60.00%"));
        assertTrue(output.contains("62.50%"));
    }

    // Test 7: Report column headers
    @Test
    void testReportHeaders() {

        report.printReport(
                "Population Report",
                List.of()
        );

        String output = outputStream.toString();

        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Total Population"));
        assertTrue(output.contains("Urban %"));
        assertTrue(output.contains("Rural %"));
    }
}
