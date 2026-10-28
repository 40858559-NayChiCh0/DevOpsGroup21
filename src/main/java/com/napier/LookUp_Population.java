package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LookUp_Population {

    /**
     *
     * @param connection Active database connection
     */
    public static void getWorldPopulation(Connection connection) {

        String sql = """
                SELECT SUM(Population) AS TotalPopulation
                FROM country
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\nWORLD POPULATION");
            System.out.println("-----------------------------------");

            if (rs.next()) {
                long totalPopulation = rs.getLong("TotalPopulation");
                System.out.printf("World Population: %,d%n", totalPopulation);
            } else {
                System.out.println("No population data found.");
            }

            System.out.println("-----------------------------------");

        } catch (SQLException e) {
            System.err.println("Error generating world population: " + e.getMessage());
        }
    }

    /**
     *
     * @param continent Name of the selected continent
     * @param connection Active database connection
     */
    public static void getContinentPopulation(String continent, Connection connection) {

        String sql = """
                SELECT Continent,
                       SUM(Population) AS TotalPopulation
                FROM country
                WHERE Continent = ?
                GROUP BY Continent
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, continent);

            try (ResultSet rs = stmt.executeQuery()) {

                System.out.println("\nCONTINENT POPULATION QUERIES");
                System.out.println("-------------------------------------------");
                System.out.printf("%-20s %20s%n", "Continent", "Population");
                System.out.println("-------------------------------------------");

                if (rs.next()) {
                    String continentName = rs.getString("Continent");
                    long totalPopulation = rs.getLong("TotalPopulation");

                    System.out.printf("%-20s %,20d%n", continentName, totalPopulation);
                } else {
                    System.out.println("No population data found for continent: " + continent);
                }

                System.out.println("-------------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("Error generating continent population queries: " + e.getMessage());
        }
    }
    /**
     * Retrieves the total population of a selected region.
     *
     * @param region Name of the selected region
     * @param connection Active database connection
     */
    public static void getRegionPopulation(String region, Connection connection) {

        String sql = """
            SELECT Region,
                   SUM(Population) AS TotalPopulation
            FROM country
            WHERE Region = ?
            GROUP BY Region
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery()) {

                System.out.println("\nREGION POPULATION");
                System.out.println("-------------------------------------------");
                System.out.printf("%-20s %20s%n", "Region", "Population");
                System.out.println("-------------------------------------------");

                if (rs.next()) {
                    String regionName = rs.getString("Region");
                    long totalPopulation = rs.getLong("TotalPopulation");

                    System.out.printf("%-20s %,20d%n",
                            regionName, totalPopulation);
                } else {
                    System.out.println(
                            "No population data found for region: " + region);
                }

                System.out.println("-------------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error generating region population: " + e.getMessage());
        }
    }
    /**
     * Retrieves the population of a selected country.
     *
     * @param country Name of the selected country
     * @param connection Active database connection
     */
    public static void getCountryPopulation(String country, Connection connection) {

        String sql = """
            SELECT Name, Population
            FROM country
            WHERE Name = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, country);

            try (ResultSet rs = stmt.executeQuery()) {

                System.out.println("\nCOUNTRY POPULATION");
                System.out.println("-------------------------------------------");
                System.out.printf("%-30s %20s%n", "Country", "Population");
                System.out.println("-------------------------------------------");

                if (rs.next()) {
                    String countryName = rs.getString("Name");
                    long population = rs.getLong("Population");

                    System.out.printf("%-30s %,20d%n",
                            countryName, population);
                } else {
                    System.out.println(
                            "No population data found for country: " + country);
                }

                System.out.println("-------------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error generating country population: " + e.getMessage());
        }
    }
    /**
     * Retrieves the population of a selected district.
     *
     * @param district Name of the selected district
     * @param connection Active database connection
     */
    public static void getDistrictPopulation(String district, Connection connection) {

        String sql = """
                SELECT District, SUM(Population) AS TotalPopulation
                FROM city
                WHERE District = ?
                GROUP BY District
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, district);

            try (ResultSet rs = stmt.executeQuery()) {

                System.out.println("\nDISTRICT POPULATION");
                System.out.println("-------------------------------------------");
                System.out.printf("%-30s %20s%n", "District", "Population");
                System.out.println("-------------------------------------------");

                if (rs.next()) {
                    String districtName = rs.getString("District");
                    long population = rs.getLong("TotalPopulation");

                    System.out.printf("%-30s %,20d%n",
                            districtName, population);
                } else {
                    System.out.println(
                            "No population data found for district: " + district);
                }

                System.out.println("-------------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error generating district population: " + e.getMessage());
        }
    }

}
