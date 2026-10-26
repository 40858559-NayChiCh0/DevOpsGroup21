
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

        printReport(sql, null);
    }

    // REPORT 2: All capital cities in a continent
    public void getCapitalCitiesByContinent(String continent)
            throws SQLException {

        String sql =
                "SELECT ci.Name AS Capital, " +
                        "co.Name AS Country, " +
                        "ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC";

        printReport(sql, continent);
    }

    // REPORT 3: All capital cities in a region
    public void getCapitalCitiesByRegion(String region)
            throws SQLException {

        String sql =
                "SELECT ci.Name AS Capital, " +
                        "co.Name AS Country, " +
                        "ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "WHERE co.Region = ? " +
                        "ORDER BY ci.Population DESC";

        printReport(sql, region);
    }

    // REPORT 4: Top N capital cities in the world
    public void getTopNCapitalCities(int n)
            throws SQLException {

        String sql =
                "SELECT ci.Name AS Capital, " +
                        "co.Name AS Country, " +
                        "ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "ORDER BY ci.Population DESC " +
                        "LIMIT ?";

        printTopNReport(sql, null, n);
    }

    // REPORT 5: Top N capital cities in a continent
    public void getTopNCapitalCitiesByContinent(
            String continent, int n) throws SQLException {

        String sql =
                "SELECT ci.Name AS Capital, " +
                        "co.Name AS Country, " +
                        "ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC " +
                        "LIMIT ?";

        printTopNReport(sql, continent, n);
    }
    // REPORT 6: Top N capital cities in a region
    public void getTopNCapitalCitiesByRegion(
            String region, int n) throws SQLException {

        String sql =
                "SELECT ci.Name AS Capital, " +
                        "co.Name AS Country, " +
                        "ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "WHERE co.Region = ? " +
                        "ORDER BY ci.Population DESC " +
                        "LIMIT ?";

        printTopNReport(sql, region, n);
    }
    // Shared method for Reports 1, 2 and 3
    private void printReport(String sql, String filter)
            throws SQLException {

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql)) {

            if (filter != null) {
                stmt.setString(1, filter);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                displayResults(rs);
            }
        }
    }

    // Shared method for Reports 4 and 5
    private void printTopNReport(
            String sql, String filter, int n)
            throws SQLException {

        if (n <= 0) {
            System.out.println("N must be greater than zero.");
            return;
        }

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql)) {

            if (filter != null) {
                // Report 5: continent and N
                stmt.setString(1, filter);
                stmt.setInt(2, n);
            } else {
                // Report 4: N only
                stmt.setInt(1, n);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                displayResults(rs);
            }
        }
    }

    // Print Name, Country and Population
    private void displayResults(ResultSet rs)
            throws SQLException {

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
            System.out.println("No capital cities found.");
        }
    }
}
