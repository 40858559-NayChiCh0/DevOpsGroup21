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
}
