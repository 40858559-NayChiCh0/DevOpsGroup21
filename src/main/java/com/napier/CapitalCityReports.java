package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CapitalCityReports {

    private final Connection connection;

    public CapitalCityReports(Connection connection) {
        this.connection = connection;
    }

    // REPORT 1: All capital cities in the world
    public void getAllCapitalCities() throws SQLException {

        String sql =
                "SELECT ci.Name AS Capital, " +
                        "co.Name AS Country, " +
                        "ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "ORDER BY ci.Population DESC";

        printReport(sql,null);
    }


    // Shared method to run SQL and display results
    private void printReport(String sql, String filter)
            throws SQLException {

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql)) {

            if (filter != null) {
                stmt.setString(1, filter);
            }

            try (ResultSet rs = stmt.executeQuery()) {

                System.out.printf(
                        "%-30s %-35s %15s%n",
                        "Name", "Country", "Population");

                System.out.println(
                        "------------------------------------------------" +
                                "--------------------------------");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    City city = new City();
                    city.setName(rs.getString("Capital"));
                    city.setPopulation(rs.getInt("Population"));

                    Country country = new Country();
                    country.setName(rs.getString("Country"));

                    System.out.printf(
                            "%-30s %-35s %,15d%n",
                            city.getName(),
                            country.getName(),
                            city.getPopulation()
                    );
                }

                if (!found) {
                    System.out.println(
                            "No capital cities found.");
                }
            }
        }
    }
}
