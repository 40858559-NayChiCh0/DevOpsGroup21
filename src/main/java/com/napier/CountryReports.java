
package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryReports {

    // Common print method for all country reports
    public static void print(ResultSet results, String... columns)
            throws SQLException {

        // Print column headings
        for (String column : columns) {
            System.out.printf("%-35s", column);
        }

        System.out.println();

        // Print report data
        while (results.next()) {

            for (String column : columns) {

                Object value = results.getObject(column);

                if (value instanceof Number) {
                    System.out.printf("%-35s",
                            String.format("%,d",
                                    ((Number) value).longValue()));
                } else {
                    System.out.printf("%-35s", value);
                }
            }

            System.out.println();
        }
    }

    // Report 1: All countries in the world by population
    public static void getAllCountries() {

        String sql = """
                SELECT Name, Population
                FROM country
                ORDER BY Population DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            print(results, "Name", "Population");

        } catch (SQLException e) {
            System.err.println("Error retrieving countries: "
                    + e.getMessage());
        }
    }

    // Report 2: Countries in a continent by population
    public static void getCountriesByContinent(String continent) {

        if (continent == null || continent.trim().isEmpty()) {
            System.out.println("Invalid continent.");
            return;
        }

        String sql = """
                SELECT Name, Continent, Population
                FROM country
                WHERE Continent = ?
                ORDER BY Population DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, continent);

            try (ResultSet results = statement.executeQuery()) {

                print(results, "Name", "Continent", "Population");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving countries by continent: "
                    + e.getMessage());
        }
    }

    // Report 3: Countries in a selected region by population
    public static void getCountriesByRegion(String region) {

        if (region == null || region.trim().isEmpty()) {
            System.out.println("Invalid region.");
            return;
        }

        String sql = """
                SELECT Name, Region, Population
                FROM country
                WHERE Region = ?
                ORDER BY Population DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, region);

            try (ResultSet results = statement.executeQuery()) {

                print(results, "Name", "Region", "Population");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving countries by region: "
                    + e.getMessage());
        }
    }

    // Report 4: Top N most populated countries in the world
    public static void getTopNPopulatedCountries(int n) {

        if (n <= 0) {
            System.out.println("N must be greater than zero.");
            return;
        }

        String sql = """
                SELECT Code, Name, Population
                FROM country
                ORDER BY Population DESC
                LIMIT ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, n);

            try (ResultSet results = statement.executeQuery()) {

                print(results, "Code", "Name", "Population");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving top populated countries: "
                    + e.getMessage());
        }
    }

    // Report 5: Top N most populated countries in a continent
    public static void getTopNPopulatedCountriesByContinent(
            String continent, int n) {

        if (continent == null || continent.trim().isEmpty()) {
            System.out.println("Invalid continent.");
            return;
        }

        if (n <= 0) {
            System.out.println("N must be greater than zero.");
            return;
        }

        String sql = """
                SELECT Code, Name, Continent, Population
                FROM country
                WHERE Continent = ?
                ORDER BY Population DESC
                LIMIT ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, continent);
            statement.setInt(2, n);

            try (ResultSet results = statement.executeQuery()) {

                print(results, "Code", "Name",
                        "Continent", "Population");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving countries by continent: "
                    + e.getMessage());
        }
    }

    // Report 6: Top N most populated countries in a region
    public static void getTopNPopulatedCountriesByRegion(
            String region, int n) {

        if (region == null || region.trim().isEmpty()) {
            System.out.println("Invalid region.");
            return;
        }

        if (n <= 0) {
            System.out.println("N must be greater than zero.");
            return;
        }

        String sql = """
                SELECT Code, Name, Region, Population
                FROM country
                WHERE Region = ?
                ORDER BY Population DESC
                LIMIT ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, region);
            statement.setInt(2, n);

            try (ResultSet results = statement.executeQuery()) {

                print(results, "Code", "Name",
                        "Region", "Population");
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving countries by region: "
                    + e.getMessage());
        }
    }

    // Common method to execute all country reports
    public static void printAllReports() {

        System.out.println("\n===== Report 1: All Countries =====");
        getAllCountries();

        System.out.println("\n===== Report 2: Countries by Continent =====");
        getCountriesByContinent("Asia");

        System.out.println("\n===== Report 3: Countries by Region =====");
        getCountriesByRegion("Southeast Asia");

        System.out.println("\n===== Report 4: Top 10 Populated Countries =====");
        getTopNPopulatedCountries(10);

        System.out.println("\n===== Report 5: Top 10 Countries by Continent =====");
        getTopNPopulatedCountriesByContinent("Asia", 10);

        System.out.println("\n===== Report 6: Top 10 Countries by Region =====");
        getTopNPopulatedCountriesByRegion("Southeast Asia", 10);
    }
}
