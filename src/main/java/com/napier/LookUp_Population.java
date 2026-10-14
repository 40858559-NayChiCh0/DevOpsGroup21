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

                System.out.println("\nCONTINENT POPULATION Queries");
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
}