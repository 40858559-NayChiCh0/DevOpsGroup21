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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CityReportsTest {

    private CityReports cityReports;

    @BeforeEach
    void setUp() {
        cityReports = new CityReports();
    }

    @Test
    void printCityReportTestNull() {
        String output = captureOutput(() ->
                cityReports.printCityReport(null, "TEST REPORT"));

        assertTrue(output.contains("TEST REPORT"));
        assertTrue(output.contains("No cities found."));
    }

    @Test
    void printCityReportTestEmpty() {
        String output = captureOutput(() ->
                cityReports.printCityReport(
                        new ArrayList<>(), "TEST REPORT"));

        assertTrue(output.contains("No cities found."));
    }

    @Test
    void printCityReportTestContainsNull() {
        ArrayList<City> cities = new ArrayList<>();
        cities.add(null);

        String output = captureOutput(() ->
                cityReports.printCityReport(cities, "TEST REPORT"));

        assertTrue(output.contains("No cities found."));
    }

    @Test
    void printCityReportTestNormal() {
        ArrayList<City> cities = new ArrayList<>();

        cities.add(city(
                "Yangon", "Yangon", 80000, "Myanmar"));

        cities.add(city(
                "Mandalay", "Mandalay", 70000, "Myanmar"));

        String output = captureOutput(() ->
                cityReports.printCityReport(cities, "TEST REPORT"));

        assertTrue(output.contains("Name"));
        assertTrue(output.contains("Country"));
        assertTrue(output.contains("District"));
        assertTrue(output.contains("Population"));
        assertTrue(output.contains("Yangon"));
        assertTrue(output.contains("Mandalay"));
        assertTrue(output.contains("Myanmar"));
        assertTrue(output.contains("80,000"));
        assertTrue(output.contains("Total cities: 2"));
    }

    @Test
    void printCityReportTestMixedNullAndNormal() {
        ArrayList<City> cities = new ArrayList<>();

        cities.add(null);
        cities.add(city(
                "Yangon", "Yangon", 80000, "Myanmar"));

        String output = captureOutput(() ->
                cityReports.printCityReport(cities, "TEST REPORT"));

        assertTrue(output.contains("Yangon"));
        assertTrue(output.contains("Total cities: 1"));
    }

    @Test
    void printCityReportTestMissingFields() {
        ArrayList<City> cities = new ArrayList<>();

        cities.add(new City());

        City cityWithUnnamedCountry = new City();
        cityWithUnnamedCountry.setName("Test City");
        cityWithUnnamedCountry.setCountry(new Country());

        cities.add(cityWithUnnamedCountry);

        String output = captureOutput(() ->
                cityReports.printCityReport(cities, "TEST REPORT"));

        assertTrue(output.contains("Unknown"));
        assertTrue(output.contains("Test City"));
        assertTrue(output.contains("Total cities: 2"));
    }


    @Test
    void getAllCitiesTest() {
        TestDB db =
                new TestDB(sampleRows(), false);

        ArrayList<City> cities =
                cityReports.getAllCities(db.connection());

        assertCities(cities);
        assertTrue(db.sql.contains(
                "ORDER BY city.Population DESC"));
    }

    @Test
    void getCitiesByContinentTest() {
        TestDB db =
                new TestDB(sampleRows(), false);

        ArrayList<City> cities =
                cityReports.getCitiesByContinent(
                        db.connection(), "Asia");

        assertCities(cities);
        assertTrue(db.sql.contains(
                "country.Continent = ?"));
        assertEquals("Asia", db.parameter);
    }

    @Test
    void getCitiesByRegionTest() {
        TestDB db =
                new TestDB(sampleRows(), false);

        ArrayList<City> cities =
                cityReports.getCitiesByRegion(
                        db.connection(), "Southeast Asia");

        assertCities(cities);
        assertTrue(db.sql.contains(
                "country.Region = ?"));
        assertEquals("Southeast Asia", db.parameter);
    }

    @Test
    void getCitiesByCountryTest() {
        TestDB db =
                new TestDB(sampleRows(), false);

        ArrayList<City> cities =
                cityReports.getCitiesByCountry(
                        db.connection(), "Myanmar");

        assertCities(cities);
        assertTrue(db.sql.contains(
                "country.Name = ?"));
        assertEquals("Myanmar", db.parameter);
    }

    @Test
    void getCitiesByDistrictTest() {
        TestDB db =
                new TestDB(sampleRows(), false);

        ArrayList<City> cities =
                cityReports.getCitiesByDistrict(
                        db.connection(), "Yangon");

        assertCities(cities);
        assertTrue(db.sql.contains(
                "city.District = ?"));
        assertEquals("Yangon", db.parameter);
    }

    // =========================================
    // EMPTY RESULTS AND SQL ERRORS
    // =========================================

    @Test
    void cityReportsTestNoRows() {
        Connection connection =
                new TestDB(List.of(), false).connection();

        assertTrue(
                cityReports.getAllCities(connection).isEmpty());

        assertTrue(
                cityReports.getCitiesByContinent(
                        connection, "Asia").isEmpty());

        assertTrue(
                cityReports.getCitiesByRegion(
                        connection, "Southeast Asia").isEmpty());

        assertTrue(
                cityReports.getCitiesByCountry(
                        connection, "Myanmar").isEmpty());

        assertTrue(
                cityReports.getCitiesByDistrict(
                        connection, "Yangon").isEmpty());
    }

    @Test
    void cityReportsTestSqlErrors() {
        Connection broken =
                new TestDB(List.of(), true).connection();

        assertThrows(IllegalStateException.class,
                () -> cityReports.getAllCities(broken));

        assertThrows(IllegalStateException.class,
                () -> cityReports.getCitiesByContinent(
                        broken, "Asia"));

        assertThrows(IllegalStateException.class,
                () -> cityReports.getCitiesByRegion(
                        broken, "Southeast Asia"));

        assertThrows(IllegalStateException.class,
                () -> cityReports.getCitiesByCountry(
                        broken, "Myanmar"));

        assertThrows(IllegalStateException.class,
                () -> cityReports.getCitiesByDistrict(
                        broken, "Yangon"));
    }

    // =========================================
    // HELPER METHODS
    // =========================================

    private static void assertCities(ArrayList<City> cities) {
        assertEquals(2, cities.size());

        assertEquals(1, cities.get(0).getId());
        assertEquals("Yangon", cities.get(0).getName());
        assertEquals("MMR", cities.get(0).getCountryCode());
        assertEquals("Yangon", cities.get(0).getDistrict());
        assertEquals(80000, cities.get(0).getPopulation());

        assertEquals(
                "MMR", cities.get(0).getCountry().getCode());assertEquals(
                "Myanmar", cities.get(0).getCountry().getName());

        assertEquals("Mandalay", cities.get(1).getName());

        assertTrue(
                cities.get(0).getPopulation()
                        >= cities.get(1).getPopulation());
    }

    private static List<Map<String, Object>> sampleRows() {
        return List.of(
                Map.of(
                        "ID", 1,
                        "Name", "Yangon",
                        "CountryCode", "MMR",
                        "CountryName", "Myanmar",
                        "District", "Yangon",
                        "Population", 80000
                ),
                Map.of(
                        "ID", 2,
                        "Name", "Mandalay",
                        "CountryCode", "MMR",
                        "CountryName", "Myanmar",
                        "District", "Mandalay",
                        "Population", 70000
                )
        );
    }

    private static City city(
            String name,
            String district,
            int population,
            String countryName) {

        City city = new City();
        city.setName(name);
        city.setDistrict(district);
        city.setPopulation(population);

        Country country = new Country();
        country.setName(countryName);

        city.setCountry(country);
        return city;
    }

    private static String captureOutput(Runnable action) {
        PrintStream old = System.out;
        ByteArrayOutputStream bytes =
                new ByteArrayOutputStream();

        try (PrintStream stream = new PrintStream(
                bytes, true, StandardCharsets.UTF_8)) {

            System.setOut(stream);
            action.run();

        } finally {
            System.setOut(old);
        }

        return bytes.toString(StandardCharsets.UTF_8);
    }

    // =========================================
    // SIMULATED JDBC DATABASE
    // =========================================

    private static class TestDB {

        private final List<Map<String, Object>> rows;
        private final boolean fail;

        String sql;
        String parameter;

        TestDB(
                List<Map<String, Object>> rows,
                boolean fail) {

            this.rows = rows;
            this.fail = fail;
        }

        Connection connection() {
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class<?>[]{Connection.class},
                    (proxy, method, args) -> {

                        if ("prepareStatement".equals(
                                method.getName())) {

                            if (fail) {
                                throw new SQLException(
                                        "Simulated SQL error");
                            }

                            sql = (String) args[0];
                            return statement();
                        }

                        return defaultValue(
                                method.getReturnType());
                    }
            );
        }

        private PreparedStatement statement() {
            return (PreparedStatement) Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(),
                    new Class<?>[]{PreparedStatement.class},
                    (proxy, method, args) -> {

                        if ("setString".equals(
                                method.getName())) {

                            parameter = (String) args[1];
                            return null;
                        }

                        if ("executeQuery".equals(
                                method.getName())) {
                            return resultSet();
                        }

                        return defaultValue(
                                method.getReturnType());
                    }
            );
        }private ResultSet resultSet() {
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
                                return (String) rows.get(
                                        index[0]).get(
                                        (String) args[0]);

                            case "getInt":
                                return (Integer) rows.get(
                                        index[0]).get(
                                        (String) args[0]);

                            default:
                                return defaultValue(
                                        method.getReturnType());
                        }
                    }
            );
        }

        private static Object defaultValue(Class<?> type) {
            if (type == boolean.class) {
                return false;
            }

            if (type == int.class) {
                return 0;
            }

            if (type == long.class) {
                return 0L;
            }

            return null;
        }
    }
}