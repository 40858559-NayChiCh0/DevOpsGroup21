package com.napier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TopNCityReportsTest {

    private TopNCityReports reports;

    @BeforeEach
    void setUp() {
        reports = new TopNCityReports();
    }

    // Issue #39: Top N cities in the world

    @Test
    void testTopNCitiesInWorld() {
        TestDB db = new TestDB(sampleRows(), false);
        ArrayList<City> cities = reports.getTopNCitiesInWorld(db.connection(), 2);

        assertCities(cities);
        assertTrue(db.sql.contains("ORDER BY city.Population DESC, city.ID ASC"));
        assertTrue(db.sql.contains("LIMIT ?"));
        assertEquals(2, db.intParameters.get(1).intValue());
    }

    @Test
    void testTopNCitiesInWorldInvalidN() {
        assertThrows(IllegalArgumentException.class,
                () -> reports.getTopNCitiesInWorld(null, 0));
        assertThrows(IllegalArgumentException.class,
                () -> reports.getTopNCitiesInWorld(null, -1));
    }

    @Test
    void testTopNCitiesInWorldEmpty() {
        TestDB db = new TestDB(List.of(), false);
        assertTrue(reports.getTopNCitiesInWorld(db.connection(), 2).isEmpty());
    }

    @Test
    void testTopNCitiesInWorldSqlError() {
        TestDB db = new TestDB(List.of(), true);
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reports.getTopNCitiesInWorld(db.connection(), 2));
        assertTrue(error.getCause() instanceof SQLException);
    }

    // Issue #40: Top N cities by continent

    @Test
    void testTopNCitiesByContinent() {
        TestDB db = new TestDB(sampleRows(), false);
        ArrayList<City> cities = reports.getTopNCitiesByContinent(db.connection(), "Asia", 2);

        assertCities(cities);
        assertTrue(db.sql.contains("WHERE country.Continent = ?"));
        assertTrue(db.sql.contains("ORDER BY city.Population DESC, city.ID ASC"));
        assertEquals("Asia", db.stringParameters.get(1));
        assertEquals(2, db.intParameters.get(2).intValue());
    }

    @Test
    void testTopNCitiesByContinentInvalidN() {
        assertThrows(IllegalArgumentException.class,
                () -> reports.getTopNCitiesByContinent(null, "Asia", 0));
        assertThrows(IllegalArgumentException.class,
                () -> reports.getTopNCitiesByContinent(null, "Asia", -5));
    }

    @Test
    void testTopNCitiesByContinentEmpty() {
        TestDB db = new TestDB(List.of(), false);
        assertTrue(reports.getTopNCitiesByContinent(db.connection(), "Asia", 2).isEmpty());
    }

    @Test
    void testTopNCitiesByContinentSqlError() {
        TestDB db = new TestDB(List.of(), true);
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> reports.getTopNCitiesByContinent(db.connection(), "Asia", 2));
        assertTrue(error.getCause() instanceof SQLException);
    }

    // Issue #41: Top N cities by region

    @Test
    void testTopNCitiesByRegion() throws SQLException {
        TestDB db = new TestDB(sampleRows(), false);
        ArrayList<City> cities = reports.getTopNCitiesByRegion(db.connection(), "Southeast Asia", 2);

        assertCities(cities);
        assertTrue(db.sql.contains("WHERE co.Region = ?"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC, ci.ID ASC"));
        assertEquals("Southeast Asia", db.stringParameters.get(1));
        assertEquals(2, db.intParameters.get(2).intValue());
    }

    @Test
    void testTopNCitiesByRegionInvalidInput() throws SQLException {
        assertTrue(reports.getTopNCitiesByRegion(null, null, 2).isEmpty());
        assertTrue(reports.getTopNCitiesByRegion(null, "", 2).isEmpty());
        assertTrue(reports.getTopNCitiesByRegion(null, "   ", 2).isEmpty());
        assertTrue(reports.getTopNCitiesByRegion(null, "Asia", 0).isEmpty());
        assertTrue(reports.getTopNCitiesByRegion(null, "Asia", -1).isEmpty());
    }

    @Test
    void testTopNCitiesByRegionEmpty() throws SQLException {
        TestDB db = new TestDB(List.of(), false);
        assertTrue(reports.getTopNCitiesByRegion(db.connection(), "Southeast Asia", 2).isEmpty());
    }

    @Test
    void testTopNCitiesByRegionSqlError() {
        TestDB db = new TestDB(List.of(), true);
        assertThrows(SQLException.class,
                () -> reports.getTopNCitiesByRegion(db.connection(), "Southeast Asia", 2));
    }

    // Issue #42: Top N cities by country

    @Test
    void testTopNCitiesByCountry() throws SQLException {
        TestDB db = new TestDB(sampleRows(), false);
        ArrayList<City> cities = reports.getTopNCitiesByCountry(db.connection(), "Myanmar", 2);

        assertCities(cities);
        assertTrue(db.sql.contains("WHERE co.Name = ?"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC, ci.ID ASC"));
        assertEquals("Myanmar", db.stringParameters.get(1));
        assertEquals(2, db.intParameters.get(2).intValue());
    }

    @Test
    void testTopNCitiesByCountryInvalidInput() throws SQLException {
        assertTrue(reports.getTopNCitiesByCountry(null, null, 2).isEmpty());
        assertTrue(reports.getTopNCitiesByCountry(null, "", 2).isEmpty());
        assertTrue(reports.getTopNCitiesByCountry(null, "   ", 2).isEmpty());
        assertTrue(reports.getTopNCitiesByCountry(null, "Myanmar", 0).isEmpty());
        assertTrue(reports.getTopNCitiesByCountry(null, "Myanmar", -1).isEmpty());
    }

    @Test
    void testTopNCitiesByCountryEmpty() throws SQLException {
        TestDB db = new TestDB(List.of(), false);
        assertTrue(reports.getTopNCitiesByCountry(db.connection(), "Myanmar", 2).isEmpty());
    }

    @Test
    void testTopNCitiesByCountrySqlError() {
        TestDB db = new TestDB(List.of(), true);
        assertThrows(SQLException.class,
                () -> reports.getTopNCitiesByCountry(db.connection(), "Myanmar", 2));
    }

    // Issue #43: Top N cities by district

    @Test
    void testTopNCitiesByDistrict() throws SQLException {
        TestDB db = new TestDB(sampleRows(), false);
        ArrayList<City> cities = reports.getTopNCitiesByDistrict(db.connection(), "Yangon", 2);

        assertCities(cities);
        assertTrue(db.sql.contains("WHERE ci.District = ?"));
        assertTrue(db.sql.contains("ORDER BY ci.Population DESC, ci.ID ASC"));
        assertEquals("Yangon", db.stringParameters.get(1));
        assertEquals(2, db.intParameters.get(2).intValue());
    }

    @Test
    void testTopNCitiesByDistrictInvalidInput() throws SQLException {
        assertTrue(reports.getTopNCitiesByDistrict(null, null, 2).isEmpty());
        assertTrue(reports.getTopNCitiesByDistrict(null, "", 2).isEmpty());
        assertTrue(reports.getTopNCitiesByDistrict(null, "   ", 2).isEmpty());
        assertTrue(reports.getTopNCitiesByDistrict(null, "Yangon", 0).isEmpty());
        assertTrue(reports.getTopNCitiesByDistrict(null, "Yangon", -1).isEmpty());
    }

    @Test
    void testTopNCitiesByDistrictEmpty() throws SQLException {
        TestDB db = new TestDB(List.of(), false);
        assertTrue(reports.getTopNCitiesByDistrict(db.connection(), "Yangon", 2).isEmpty());
    }

    @Test
    void testTopNCitiesByDistrictSqlError() {
        TestDB db = new TestDB(List.of(), true);
        assertThrows(SQLException.class,
                () -> reports.getTopNCitiesByDistrict(db.connection(), "Yangon", 2));
    }

    // Helpers

    private static void assertCities(ArrayList<City> cities) {
        assertEquals(2, cities.size());
        City first = cities.get(0);
        City second = cities.get(1);

        assertEquals(1, first.getId());
        assertEquals("Yangon", first.getName());
        assertEquals("MMR", first.getCountryCode());
        assertEquals("Yangon", first.getDistrict());
        assertEquals(80000, first.getPopulation());
        assertNotNull(first.getCountry());
        assertEquals("Myanmar", first.getCountry().getName());

        assertEquals(2, second.getId());
        assertEquals("Mandalay", second.getName());
        assertEquals(70000, second.getPopulation());
        assertTrue(first.getPopulation() >= second.getPopulation());
    }

    private static List<Map<String, Object>> sampleRows() {
        return List.of(
                Map.of("ID", 1, "Name", "Yangon", "CountryCode", "MMR",
                        "CountryName", "Myanmar", "District", "Yangon", "Population", 80000),
                Map.of("ID", 2, "Name", "Mandalay", "CountryCode", "MMR",
                        "CountryName", "Myanmar", "District", "Mandalay", "Population", 70000)
        );
    }

    // Fake JDBC connection for isolated unit testing

    private static class TestDB {
        private final List<Map<String, Object>> rows;
        private final boolean fail;
        String sql;
        final Map<Integer, String> stringParameters = new HashMap<>();
        final Map<Integer, Integer> intParameters = new HashMap<>();

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
                            case "setString":
                                stringParameters.put((Integer) args[0], (String) args[1]);
                                return null;
                            case "setInt":
                                intParameters.put((Integer) args[0], (Integer) args[1]);
                                return null;
                            case "executeQuery":
                                return resultSet();
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
                                return (String) rows.get(index[0]).get((String) args[0]);
                            case "getInt":
                                return (Integer) rows.get(index[0]).get((String) args[0]);
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
