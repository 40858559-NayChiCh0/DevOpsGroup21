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
}
