package com.napier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
class TopNCityReportsIntegrationTest {

    private final TopNCityReports reports = new TopNCityReports();
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
    void worldReportReturnsTheThreeLargestCitiesAndMapsCityDetails() {
        List<City> cities = reports.getTopNCitiesInWorld(connection, 3);
        assertCityIds(cities, 1024, 2331, 206);

        City mumbai = cities.getFirst();
        assertEquals("Mumbai (Bombay)", mumbai.getName());
        assertEquals("Maharashtra", mumbai.getDistrict());
        assertEquals(10_500_000, mumbai.getPopulation());
        assertEquals("IND", mumbai.getCountryCode());
        assertNotNull(mumbai.getCountry());
        assertEquals("IND", mumbai.getCountry().getCode());
        assertEquals("India", mumbai.getCountry().getName());
    }

    @Test
    void continentReportReturnsTheThreeLargestCitiesInEurope() {
        assertCityIds(reports.getTopNCitiesByContinent(connection, "Europe", 3),
                3580, 456, 3581);
    }

    @Test
    void regionReportReturnsTheThreeLargestCitiesInSoutheastAsia() throws SQLException {
        assertCityIds(reports.getTopNCitiesByRegion(connection, "Southeast Asia", 3),
                939, 3320, 3208);
    }

    @Test
    void countryReportReturnsTheThreeLargestCitiesInMyanmar() throws SQLException {
        List<City> cities = reports.getTopNCitiesByCountry(connection, "Myanmar", 3);
        assertCityIds(cities, 2710, 2711, 2712);

        for (City city : cities) {
            assertEquals("MMR", city.getCountryCode());
            assertNotNull(city.getCountry());
            assertEquals("Myanmar", city.getCountry().getName());
        }
    }

    @Test
    void districtReportReturnsTheThreeLargestCitiesInCalifornia() throws SQLException {
        assertCityIds(reports.getTopNCitiesByDistrict(connection, "California", 3),
                3794, 3799, 3803);
    }

    @Test
    void districtReportPrefersTheLowerCityIdWhenPopulationsTieAtTheLimit() throws SQLException {
        // Anshan (1918) and Fushun (1919) both have a population of 1,200,000.
        assertCityIds(reports.getTopNCitiesByDistrict(connection, "Liaoning", 3),
                1896, 1902, 1918);
    }

    @Test
    void districtReportReturnsAllAvailableCitiesWhenFewerThanRequested() throws SQLException {
        String district = "Sagaing";
        List<Integer> expectedIds = new ArrayList<>();

        try (var statement = connection.prepareStatement("""
                SELECT ID
                FROM city
                WHERE District = ?
                ORDER BY Population DESC, ID ASC
                """)) {
            statement.setString(1, district);
            try (var rows = statement.executeQuery()) {
                while (rows.next()) {
                    expectedIds.add(rows.getInt("ID"));
                }
            }
        }

        assertTrue(!expectedIds.isEmpty(), "Expected at least one city in Sagaing");
        int requested = expectedIds.size() + 1;
        assertCityIds(reports.getTopNCitiesByDistrict(connection, district, requested),
                expectedIds.toArray(Integer[]::new));
    }

    @Test
    void geographicReportsReturnEmptyListsForAnUnknownLocation() throws SQLException {
        String unknownLocation = "__no_such_location__";

        assertTrue(reports.getTopNCitiesByContinent(connection, unknownLocation, 3).isEmpty());
        assertTrue(reports.getTopNCitiesByRegion(connection, unknownLocation, 3).isEmpty());
        assertTrue(reports.getTopNCitiesByCountry(connection, unknownLocation, 3).isEmpty());
        assertTrue(reports.getTopNCitiesByDistrict(connection, unknownLocation, 3).isEmpty());
    }

    private static void assertCityIds(List<City> cities, Integer... expectedIds) {
        List<Integer> actualIds = cities.stream().map(City::getId).toList();
        assertEquals(List.of(expectedIds), actualIds,
                "The report should return the expected city IDs in population order");
    }
}
