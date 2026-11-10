
package com.napier;

import org.junit.jupiter.api.Test;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetProvider;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Types;

import static org.junit.jupiter.api.Assertions.*;

public class CountryReportsUnitTest {

    // Test 1: Print country report data
    @Test
    void testPrintWithData() throws Exception {

        CachedRowSet results = createResults(true);

        String output = captureOutput(() -> {
            try {
                CountryReports.print(results, "Name", "Population");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        results.close();

        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Population"));
        assertTrue(output.contains("Test Country"));
        assertTrue(output.contains("1,000,000"));
    }
    // Test 2: Print empty results
    @Test
    void testPrintWithEmptyData() throws Exception {

        CachedRowSet results = createResults(false);

        String output = captureOutput(() -> {
            try {
                CountryReports.print(results, "Name", "Population");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        results.close();

        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Population"));
        assertFalse(output.contains("Test Country"));
    }

    // Test 3: Top N with zero
    @Test
    void testTopNZero() {

        String output = captureOutput(
                () -> CountryReports.getTopNPopulatedCountries(0)
        );

        assertTrue(output.contains("N must be greater than zero."));
    }

    // Test 4: Top N with negative number
    @Test
    void testTopNNegative() {

        String output = captureOutput(
                () -> CountryReports.getTopNPopulatedCountries(-5)
        );

        assertTrue(output.contains("N must be greater than zero."));
    }

    // Test 5: Top N by continent with zero
    @Test
    void testTopNContinentZero() {

        String output = captureOutput(
                () -> CountryReports.getTopNPopulatedCountriesByContinent(
                        "Asia", 0)
        );

        assertTrue(output.contains("N must be greater than zero."));
    }

    // Test 6: Top N by region with zero
    @Test
    void testTopNRegionZero() {

        String output = captureOutput(
                () -> CountryReports.getTopNPopulatedCountriesByRegion(
                        "Southeast Asia", 0)
        );

        assertTrue(output.contains("N must be greater than zero."));
    }

    // Test 7: Countries by continent with null
    @Test
    void testGetCountriesByContinentNull() {

        String output = captureOutput(
                () -> CountryReports.getCountriesByContinent(null)
        );

        assertTrue(output.contains("Invalid continent."));
    }

    // Test 8: Countries by region with null
    @Test
    void testGetCountriesByRegionNull() {

        String output = captureOutput(
                () -> CountryReports.getCountriesByRegion(null)
        );

        assertTrue(output.contains("Invalid region."));
    }

    // Test 9: Top N by continent with null
    @Test
    void testTopNContinentNull() {

        String output = captureOutput(
                () -> CountryReports.getTopNPopulatedCountriesByContinent(
                        null, 10)
        );

        assertTrue(output.contains("Invalid continent."));
    }

    // Test 10: Top N by region with null
    @Test
    void testTopNRegionNull() {

        String output = captureOutput(
                () -> CountryReports.getTopNPopulatedCountriesByRegion(
                        null, 10)
        );

        assertTrue(output.contains("Invalid region."));
    }

    // Test 11: Countries by continent with empty string
    @Test
    void testGetCountriesByContinentEmpty() {

        String output = captureOutput(
                () -> CountryReports.getCountriesByContinent("")
        );

        assertTrue(output.contains("Invalid continent."));
    }

    // Test 12: Countries by region with empty string
    @Test
    void testGetCountriesByRegionEmpty() {

        String output = captureOutput(
                () -> CountryReports.getCountriesByRegion("")
        );

        assertTrue(output.contains("Invalid region."));
    }

    // Test 13: Print all reports
    @Test
    void testPrintAllReports() {

        String output = captureOutput(
                CountryReports::printAllReports
        );

        assertTrue(output.contains("Report 1: All Countries"));
        assertTrue(output.contains("Report 2: Countries by Continent"));
        assertTrue(output.contains("Report 3: Countries by Region"));
        assertTrue(output.contains("Report 4: Top 10 Populated Countries"));
        assertTrue(output.contains("Report 5: Top 10 Countries by Continent"));
        assertTrue(output.contains("Report 6: Top 10 Countries by Region"));
    }

    // Create sample ResultSet data
    private CachedRowSet createResults(boolean addData)
            throws Exception {

        CachedRowSet results = RowSetProvider
                .newFactory().createCachedRowSet();

        RowSetMetaDataImpl metadata = new RowSetMetaDataImpl();

        metadata.setColumnCount(2);

        metadata.setColumnName(1, "Name");
        metadata.setColumnType(1, Types.VARCHAR);

        metadata.setColumnName(2, "Population");
        metadata.setColumnType(2, Types.INTEGER);

        results.setMetaData(metadata);

        if (addData) {

            results.moveToInsertRow();

            results.updateString(1, "Test Country");
            results.updateInt(2, 1000000);

            results.insertRow();
            results.moveToCurrentRow();
        }

        results.beforeFirst();

        return results;
    }

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
}
