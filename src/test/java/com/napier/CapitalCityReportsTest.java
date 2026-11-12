package com.napier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CapitalCityReports.
 * Uses simulated JDBC objects, so Docker and MySQL are not required.
 */
class CapitalCityReportsTest {

    private TestDB db;
    private CapitalCityReports reports;

    @BeforeEach
    void setUp() {
        db = new TestDB(sampleRows(), false);
        reports = new CapitalCityReports(db.connection());
    }

    // REPORT 1: All capital cities in the world
    @Test
    void getAllCapitalCitiesTest() throws SQLException {
        String output = captureOutput(reports::getAllCapitalCities);

        assertReportHasCities(output);
        assertTrue(db.sql.contains("JOIN city ci ON co.Capital = ci.ID"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC"));
        assertFalse(db.sql.contains("WHERE"));
        assertFalse(db.sql.contains("LIMIT"));
        assertTrue(db.parameters.isEmpty());
    }

    // REPORT 2: All capital cities in a continent
    @Test
    void getCapitalCitiesByContinentTest() throws SQLException {
        String output = captureOutput(
                () -> reports.getCapitalCitiesByContinent("Asia"));

        assertReportHasCities(output);
        assertTrue(db.sql.contains("WHERE co.Continent = ?"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC"));
        assertEquals(Map.of(1, "Asia"), db.parameters);
    }

    // REPORT 3: All capital cities in a region
    @Test
    void getCapitalCitiesByRegionTest() throws SQLException {
        String output = captureOutput(
                () -> reports.getCapitalCitiesByRegion("Southeast Asia"));

        assertReportHasCities(output);
        assertTrue(db.sql.contains("WHERE co.Region = ?"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC"));
        assertEquals(Map.of(1, "Southeast Asia"), db.parameters);
    }

    // REPORT 4: Top N capital cities in the world
    @Test
    void getTopNCapitalCitiesTest() throws SQLException {
        String output = captureOutput(() -> reports.getTopNCapitalCities(2));

        assertReportHasCities(output);
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC LIMIT ?"));
        assertFalse(db.sql.contains("WHERE"));
        assertEquals(Map.of(1, 2), db.parameters);
    }

    // REPORT 5: Top N capital cities in a continent
    @Test
    void getTopNCapitalCitiesByContinentTest() throws SQLException {
        String output = captureOutput(
                () -> reports.getTopNCapitalCitiesByContinent("Asia", 2));

        assertReportHasCities(output);
        assertTrue(db.sql.contains("WHERE co.Continent = ?"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC LIMIT ?"));
        assertEquals(Map.of(1, "Asia", 2, 2), db.parameters);
    }

    // REPORT 6: Top N capital cities in a region
    @Test
    void getTopNCapitalCitiesByRegionTest() throws SQLException {
        String output = captureOutput(
                () -> reports.getTopNCapitalCitiesByRegion("Southeast Asia", 2));

        assertReportHasCities(output);
        assertTrue(db.sql.contains("WHERE co.Region = ?"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC LIMIT ?"));
        assertEquals(Map.of(1, "Southeast Asia", 2, 2), db.parameters);
    }

    // A query with no results must display the empty-report message.
    @Test
    void getAllCapitalCitiesTestNoRows() throws SQLException {
        TestDB emptyDb = new TestDB(List.of(), false);
        CapitalCityReports emptyReports = new CapitalCityReports(emptyDb.connection());

        String output = captureOutput(emptyReports::getAllCapitalCities);

        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Country"));
        assertTrue(output.contains("Population"));
        assertTrue(output.contains("No capital cities found."));
        assertEquals(1, emptyDb.executedQueries);
    }

    // Exercise the early-return branch for invalid N in all Top N reports.
    @Test
    void topNReportsTestZeroAndNegativeN() throws SQLException {
        String output = captureOutput(() -> {
            reports.getTopNCapitalCities(0);
            reports.getTopNCapitalCitiesByContinent("Asia", -1);
            reports.getTopNCapitalCitiesByRegion("Southeast Asia", 0);
        });

        assertEquals(3, occurrences(output, "N must be greater than zero."));
        assertEquals(0, db.preparedStatements);
        assertEquals(0, db.executedQueries);
    }

    // SQL exceptions are propagated to the caller by all six public reports.
    @Test
    void capitalCityReportsTestSqlErrors() {
        TestDB brokenDb = new TestDB(List.of(), true);
        CapitalCityReports brokenReports =
                new CapitalCityReports(brokenDb.connection());

        assertThrows(SQLException.class, brokenReports::getAllCapitalCities);
        assertThrows(SQLException.class,
                () -> brokenReports.getCapitalCitiesByContinent("Asia"));
        assertThrows(SQLException.class,
                () -> brokenReports.getCapitalCitiesByRegion("Southeast Asia"));
        assertThrows(SQLException.class,
                () -> brokenReports.getTopNCapitalCities(2));
        assertThrows(SQLException.class,
                () -> brokenReports.getTopNCapitalCitiesByContinent("Asia", 2));
        assertThrows(SQLException.class,
                () -> brokenReports.getTopNCapitalCitiesByRegion("Southeast Asia", 2));
    }

    // Verify try-with-resources closes both JDBC resources.
    @Test
    void capitalCityReportsTestClosesResources() throws SQLException {
        captureOutput(reports::getAllCapitalCities);
        assertEquals(1, db.closedStatements);
        assertEquals(1, db.closedResultSets);
    }

    // ================= TEST HELPERS =================

    private static void assertReportHasCities(String output) {
        assertAll(
                () -> assertTrue(output.contains("Name")),
                () -> assertTrue(output.contains("Country")),
                () -> assertTrue(output.contains("Population")),
                () -> assertTrue(output.contains("Yangon")),
                () -> assertTrue(output.contains("Myanmar")),
                () -> assertTrue(output.contains("80,000")),
                () -> assertTrue(output.contains("Mandalay")),
                () -> assertTrue(output.contains("70,000")),
                () -> assertFalse(output.contains("No capital cities found."))
        );
    }

    private static List<Map<String, Object>> sampleRows() {
        return List.of(
                Map.of("Capital", "Yangon", "Country", "Myanmar",
                        "Population", 80_000),
                Map.of("Capital", "Mandalay", "Country", "Myanmar",
                        "Population", 70_000)
        );
    }

    @FunctionalInterface
    private interface SqlAction {
        void run() throws SQLException;
    }

    private static String captureOutput(SqlAction action) throws SQLException {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        try (PrintStream stream =
                     new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(stream);
            action.run();
        } finally {
            System.setOut(original);
        }

        return bytes.toString(StandardCharsets.UTF_8);
    }

    private static int occurrences(String text, String fragment) {
        int count = 0;
        int start = 0;
        while ((start = text.indexOf(fragment, start)) >= 0) {
            count++;
            start += fragment.length();
        }
        return count;
    }

    // ================= SIMULATED JDBC DATABASE =================

    private static class TestDB {
        private final List<Map<String, Object>> rows;
        private final boolean fail;

        String sql;
        final Map<Integer, Object> parameters = new HashMap<>();
        int preparedStatements;
        int executedQueries;
        int closedStatements;
        int closedResultSets;

        TestDB(List<Map<String, Object>> rows, boolean fail) {
            this.rows = rows;
            this.fail = fail;
        }

        Connection connection() {
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> {
                        if ("prepareStatement".equals(method.getName())) {
                            if (fail) {
                                throw new SQLException("Simulated SQL error");
                            }
                            preparedStatements++;
                            sql = (String) args[0];
                            return statement();
                        }
                        return defaultValue(method.getReturnType());
                    });
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(),
                    new Class<?>[]{PreparedStatement.class},
                    (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "setString", "setInt":
                                parameters.put((Integer) args[0], args[1]);
                                return null;
                            case "executeQuery":
                                executedQueries++;
                                return resultSet();
                            case "close":
                                closedStatements++;
                                return null;
                            default:
                                return defaultValue(method.getReturnType());
                        }
                    });
        }

        private ResultSet resultSet() {
            final int[] index = {-1};
            return (ResultSet) Proxy.newProxyInstance(
                    ResultSet.class.getClassLoader(),
                    new Class<?>[]{ResultSet.class},
                    (proxy, method, args) -> {
                        switch (method.getName()) {
                            case "next":
                                index[0]++;
                                return index[0] < rows.size();
                            case "getString":
                                return (String) rows.get(index[0])
                                        .get((String) args[0]);
                            case "getInt":
                                return (Integer) rows.get(index[0])
                                        .get((String) args[0]);
                            case "close":
                                closedResultSets++;
                                return null;
                            default:
                                return defaultValue(method.getReturnType());
                        }
                    });
        }

        private static Object defaultValue(Class<?> type) {
            if (type == boolean.class) return false;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            return null;
        }
    }
}
