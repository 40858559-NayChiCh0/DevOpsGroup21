package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryReports {
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

            System.out.printf("%-35s %15s%n",
                    "Country", "Population");


            while (results.next()) {
                String name = results.getString("Name");
                int population = results.getInt("Population");

                System.out.printf("%-35s %,15d%n",
                        name, population);
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error retrieving countries: " + e.getMessage());
        }

    }
    // Report 2: Countries in a continent by population
    public static void getCountriesByContinent(String continent) {

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

                System.out.printf("%-35s %-20s %15s%n",
                        "Country", "Continent", "Population");


                boolean found = false;

                while (results.next()) {
                    found = true;

                    String name = results.getString("Name");
                    String countryContinent =
                            results.getString("Continent");
                    int population = results.getInt("Population");

                    System.out.printf("%-35s %-20s %,15d%n",
                            name, countryContinent, population);
                }

                if (!found) {
                    System.out.println(
                            "No countries found for continent: "
                                    + continent);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error retrieving countries by continent: "
                            + e.getMessage());
        }
    }
    // Report 3: Countries in a selected region by population
    public static void getCountriesByRegion(String region) {

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

                System.out.printf("%-35s %-30s %15s%n",
                        "Country", "Region", "Population");



                boolean found = false;

                while (results.next()) {
                    found = true;

                    String name = results.getString("Name");
                    String countryRegion = results.getString("Region");
                    int population = results.getInt("Population");

                    System.out.printf("%-35s %-30s %,15d%n",
                            name, countryRegion, population);
                }

                if (!found) {
                    System.out.println(
                            "No countries found in region: " + region);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error retrieving countries by region: "
                            + e.getMessage());
        }
    }
    // Display the top N most populated countries in the world
    public static void getTopNPopulatedCountries(int n) {
        String sql = "SELECT Code, Name, Population " +
                "FROM country " +
                "ORDER BY Population DESC " +
                "LIMIT ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            if (n <= 0) {
                System.out.println("N must be greater than zero.");
                return;
            }

            pstmt.setInt(1, n);

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nTop " + n + " Populated Countries");
                System.out.printf("%-10s %-35s %15s%n",
                        "Code", "Country", "Population");

                while (rs.next()) {
                    System.out.printf("%-10s %-35s %,15d%n",
                            rs.getString("Code"),
                            rs.getString("Name"),
                            rs.getLong("Population"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving top populated countries: "
                    + e.getMessage());
        }
    }
}
