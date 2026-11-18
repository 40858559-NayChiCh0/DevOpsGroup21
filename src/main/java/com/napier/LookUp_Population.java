
package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Retrieves and prints population reports.
 */
public class LookUp_Population {

    /**
     * Common method to print population reports.
     *
     * @param name Name of the selected item.
     * @param population Population value.
     * @param title Report title.
     * @param columnName Column heading.
     */
    public static void printPopulationReport(
            String name, long population, String title, String columnName) {

        System.out.println("\n========== " + title + " ==========");

        if (name == null) {
            System.out.println("No population data found.");
            return;
        }

        System.out.printf(
                "%-30s %20s%n",
                columnName, "Population");

        System.out.println("-".repeat(52));

        System.out.printf(
                "%-30s %,20d%n",
                name, population);

        System.out.println("-".repeat(52));
    }

    /**
     * Retrieves the total population of the world.
     */
    public static void getWorldPopulation(Connection connection) {

        String sql = """
                SELECT SUM(Population) AS TotalPopulation
                FROM country
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                printPopulationReport(
                        "World",
                        rs.getLong("TotalPopulation"),
                        "WORLD POPULATION",
                        "World");
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve world population.", e);
        }
    }

    /**
     * Retrieves the total population of a selected continent.
     *
     * @param continent Selected continent.
     * @param connection Database connection.
     */
    public static void getContinentPopulation(
            String continent, Connection connection) {

        String sql = """
                SELECT Continent, SUM(Population) AS TotalPopulation
                FROM country
                WHERE Continent = ?
                GROUP BY Continent
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, continent);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    printPopulationReport(
                            rs.getString("Continent"),
                            rs.getLong("TotalPopulation"),
                            "CONTINENT POPULATION",
                            "Continent");
                } else {
                    printPopulationReport(
                            null, 0,
                            "CONTINENT POPULATION",
                            "Continent");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve continent population: "
                            + continent, e);
        }
    }

    /**
     * Retrieves the total population of a selected region.
     *
     * @param region Selected region.
     * @param connection Database connection.
     */
    public static void getRegionPopulation(
            String region, Connection connection) {

        String sql = """
                SELECT Region, SUM(Population) AS TotalPopulation
                FROM country
                WHERE Region = ?
                GROUP BY Region
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    printPopulationReport(
                            rs.getString("Region"),
                            rs.getLong("TotalPopulation"),
                            "REGION POPULATION",
                            "Region");
                } else {
                    printPopulationReport(
                            null, 0,
                            "REGION POPULATION",
                            "Region");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve region population: "
                            + region, e);
        }
    }

    /**
     * Retrieves the population of a selected country.
     *
     * @param country Selected country.
     * @param connection Database connection.
     */
    public static void getCountryPopulation(
            String country, Connection connection) {

        String sql = """
                SELECT Name, Population
                FROM country
                WHERE Name = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, country);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    printPopulationReport(
                            rs.getString("Name"),
                            rs.getLong("Population"),
                            "COUNTRY POPULATION",
                            "Country");
                } else {
                    printPopulationReport(
                            null, 0,
                            "COUNTRY POPULATION",
                            "Country");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve country population: "
                            + country, e);
        }
    }

    /**
     * Retrieves the total population of a selected district.
     *
     * @param district Selected district.
     * @param connection Database connection.
     */
    public static void getDistrictPopulation(
            String district, Connection connection) {

        String sql = """
                SELECT District, SUM(Population) AS TotalPopulation
                FROM city
                WHERE District = ?
                GROUP BY District
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, district);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    printPopulationReport(
                            rs.getString("District"),
                            rs.getLong("TotalPopulation"),
                            "DISTRICT POPULATION",
                            "District");
                } else {
                    printPopulationReport(
                            null, 0,
                            "DISTRICT POPULATION",
                            "District");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve district population: "
                            + district, e);
        }
    }

    /**
     * Retrieves the population of a selected city.
     *
     * @param city Selected city.
     * @param connection Database connection.
     */
    public static void getCityPopulation(
            String city, Connection connection) {

        String sql = """
                SELECT Name, Population
                FROM city
                WHERE Name = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, city);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    printPopulationReport(
                            rs.getString("Name"),
                            rs.getLong("Population"),
                            "CITY POPULATION",
                            "City");
                } else {
                    printPopulationReport(
                            null, 0,
                            "CITY POPULATION",
                            "City");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve city population: "
                            + city, e);
        }
    }
}
