package com.napier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class LookUp_PopulationReports {



    /**
     * Generates a report showing the total population
     * of the world.
     *
     * @param connection Active database connection
     */
    public static void getWorldPopulation(Connection connection) {

        // SQL query to calculate the total world population
        String sql = """
                SELECT SUM(Population) AS TotalPopulation
                FROM country
                """;

        // Execute the SQL query
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Display report title
            System.out.println("\nWORLD POPULATION REPORT");
            System.out.println("-----------------------------------");

            // Retrieve and display world population
            if (rs.next()) {

                long totalPopulation = rs.getLong("TotalPopulation");

                // Display formatted population
                System.out.printf(
                        "World Population: %,d%n",
                        totalPopulation
                );

            } else {
                System.out.println("No population data found.");
            }

            System.out.println("-----------------------------------");

        } catch (SQLException e) {

            // Display error if SQL execution fails
            System.err.println(
                    "Error generating world population report: "
                            + e.getMessage()
            );
        }
    }
}