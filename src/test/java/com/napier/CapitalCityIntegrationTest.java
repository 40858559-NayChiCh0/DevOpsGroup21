package com.napier;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class CapitalCityIntegrationTest {

    private static final String BASE_SQL =
            "SELECT ci.Name, co.Name, ci.Population " +
                    "FROM country co JOIN city ci ON co.Capital = ci.ID";

    @Test
    void testAllCapitalCities() throws SQLException {
        checkReport("", null, null, CapitalCityReports::getAllCapitalCities);
    }

    @Test
    void testCapitalCitiesByContinent() throws SQLException {
        checkReport(" WHERE co.Continent = ?", "Asia", null,
                reports -> reports.getCapitalCitiesByContinent("Asia"));
    }

    @Test
    void testCapitalCitiesByRegion() throws SQLException {
        checkReport(" WHERE co.Region = ?", "Southeast Asia", null,
                reports -> reports.getCapitalCitiesByRegion("Southeast Asia"));
    }

    @Test
    void testTopNCapitalCities() throws SQLException {
        checkReport("", null, 3,
                reports -> reports.getTopNCapitalCities(3));
    }

    @Test
    void testTopNCapitalCitiesByContinent() throws SQLException {
        checkReport(" WHERE co.Continent = ?", "Asia", 3,
                reports -> reports.getTopNCapitalCitiesByContinent("Asia", 3));
    }

    @Test
    void testTopNCapitalCitiesByRegion() throws SQLException {
        checkReport(" WHERE co.Region = ?", "Southeast Asia", 3,
                reports -> reports.getTopNCapitalCitiesByRegion("Southeast Asia", 3));
    }

    @Test
    void testNoCapitalCitiesFound() throws SQLException {
        try (Connection connection = openConnection()) {
            String output = captureOutput(new CapitalCityReports(connection),
                    reports -> reports.getCapitalCitiesByRegion("__no_such_region__"));

            assertTrue(output.contains("No capital cities found."));
        }
    }

    private void checkReport(String where, String filter, Integer limit,
                             ReportAction action) throws SQLException {
        try (Connection connection = openConnection()) {
            List<String> expectedRows = new ArrayList<>();
            List<String> expectedPopulationColumns = new ArrayList<>();

            String sql = BASE_SQL + where + " ORDER BY ci.Population DESC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (filter != null) {
                    statement.setString(1, filter);
                }
                try (ResultSet rows = statement.executeQuery()) {
                    while (rows.next()) {
                        int population = rows.getInt(3);
                        expectedRows.add(String.format("%-30s %-35s %,15d",
                                rows.getString(1), rows.getString(2), population));
                        expectedPopulationColumns.add(String.format("%,15d", population));
                    }
                }
            }

            assertTrue(!expectedRows.isEmpty(),
                    "The test database must contain matching capital cities.");

            int expectedCount = limit == null
                    ? expectedRows.size() : Math.min(limit, expectedRows.size());

            String output = captureOutput(new CapitalCityReports(connection), action);
            String[] lines = output.split("\\R");

            assertEquals(expectedCount + 2, lines.length,
                    "Expected a header, separator, and the correct number of rows.");
            assertTrue(lines[0].contains("Population"));

            for (int i = 0; i < expectedCount; i++) {
                String row = lines[i + 2];
                assertTrue(expectedRows.remove(row), "Unexpected or duplicate row: " + row);
                assertTrue(row.endsWith(expectedPopulationColumns.get(i)),
                        "Incorrect population order at row " + (i + 1));
            }
        }
    }

    private Connection openConnection() throws SQLException {
        Connection connection = DBConnection.getConnection();

        if (connection == null) {
            throw new SQLException("DBConnection.getConnection() returned null.");
        }

        return connection;
    }

    private String captureOutput(CapitalCityReports reports, ReportAction action)
            throws SQLException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capture = new PrintStream(output)) {
            System.setOut(capture);
            action.run(reports);
        } finally {
            System.setOut(originalOut);
        }

        return output.toString();
    }

    @FunctionalInterface
    private interface ReportAction {
        void run(CapitalCityReports reports) throws SQLException;
    }
}