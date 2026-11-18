package com.napier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
class CityReportsIntegrationTest {

    private static final Comparator<City> REPORT_ORDER =
            Comparator.comparingInt(City::getPopulation).reversed()
                    .thenComparingInt(City::getId);

    private final CityReports reports = new CityReports();
    private Connection connection;

    @BeforeEach
    void openConnection() throws SQLException {
        connection = DBConnection.getConnection();
    }

    @AfterEach
    void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    void getAllCitiesReturnsEveryCityAndMapsItsCountry() throws SQLException {
        List<City> cities = reports.getAllCities(connection);
        assertReport(cities, queryCityIds("SELECT ID FROM city"));

        City tokyo = cities.stream()
                .filter(city -> city.getId() == 1532)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected Tokyo in the world report"));

        assertEquals("Tokyo", tokyo.getName());
        assertEquals("Tokyo-to", tokyo.getDistrict());
        assertEquals(7_980_230, tokyo.getPopulation());
        assertEquals("JPN", tokyo.getCountryCode());
        assertNotNull(tokyo.getCountry(), "Tokyo should have a mapped country");
        assertEquals("JPN", tokyo.getCountry().getCode());
        assertEquals("Japan", tokyo.getCountry().getName());
    }

    @Test
    void getCitiesByContinentReturnsExactlyTheCitiesInAsia() throws SQLException {
        Set<Integer> expected = queryCityIds("""
                SELECT ID FROM city
                WHERE CountryCode IN (SELECT Code FROM country WHERE Continent = ?)
                """, "Asia");

        assertReport(reports.getCitiesByContinent(connection, "Asia"), expected);
    }

    @Test
    void getCitiesByRegionReturnsExactlyTheCitiesInSoutheastAsia() throws SQLException {
        Set<Integer> expected = queryCityIds("""
                SELECT ID FROM city
                WHERE CountryCode IN (SELECT Code FROM country WHERE Region = ?)
                """, "Southeast Asia");

        assertReport(reports.getCitiesByRegion(connection, "Southeast Asia"), expected);
    }

    @Test
    void getCitiesByCountryReturnsExactlyTheCitiesInMyanmar() throws SQLException {
        Set<Integer> expected = queryCityIds(
                "SELECT ID FROM city WHERE CountryCode = ?", "MMR");

        assertReport(reports.getCitiesByCountry(connection, "Myanmar"), expected);
    }

    @Test
    void getCitiesByDistrictReturnsExactlyTheCitiesInSagaing() throws SQLException {
        Set<Integer> expected = queryCityIds(
                "SELECT ID FROM city WHERE District = ?", "Sagaing");

        assertReport(reports.getCitiesByDistrict(connection, "Sagaing"), expected);
    }

    @Test
    void geographicFiltersReturnEmptyListsForAnUnknownLocation() {
        String unknownLocation = "__no_such_location__";

        assertTrue(reports.getCitiesByContinent(connection, unknownLocation).isEmpty());
        assertTrue(reports.getCitiesByRegion(connection, unknownLocation).isEmpty());
        assertTrue(reports.getCitiesByCountry(connection, unknownLocation).isEmpty());
        assertTrue(reports.getCitiesByDistrict(connection, unknownLocation).isEmpty());
    }

    private Set<Integer> queryCityIds(String sql, String... parameters) throws SQLException {
        Set<Integer> ids = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setString(i + 1, parameters[i]);
            }
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    ids.add(rows.getInt("ID"));
                }
            }
        }
        return ids;
    }

    private static void assertReport(List<City> cities, Set<Integer> expectedIds) {
        assertFalse(expectedIds.isEmpty(), "Expected matching rows in the seeded world database");

        Set<Integer> actualIds = cities.stream()
                .map(City::getId)
                .collect(Collectors.toSet());
        assertEquals(expectedIds, actualIds, "The report should return exactly the matching cities");
        assertEquals(actualIds.size(), cities.size(), "Each city should appear once");

        for (int i = 1; i < cities.size(); i++) {
            City previous = cities.get(i - 1);
            City current = cities.get(i);
            assertTrue(REPORT_ORDER.compare(previous, current) <= 0,
                    () -> "Cities should be ordered by population descending, then ID ascending: "
                            + previous.getId() + " before " + current.getId());
        }
    }
}
