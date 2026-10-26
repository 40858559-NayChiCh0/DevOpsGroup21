package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TopNCityReports {

    /**
     * Issue 39: Top N populated cities in the world.
     * Retrieves the top N cities ordered by population
     * from largest to smallest.
     *
     * @param connection Database connection.
     * @param n Number of cities to retrieve.
     * @return List of top N populated cities.
     */
    public ArrayList<City> getTopNCitiesInWorld(
            Connection connection, int n) {

        ArrayList<City> cities = new ArrayList<>();

        if (n <= 0) {
            throw new IllegalArgumentException(
                    "N must be greater than zero.");
        }

        String sql = """
        SELECT city.ID,
               city.Name,
               city.CountryCode,
               country.Name AS CountryName,
               city.District,
               city.Population
        FROM city
        JOIN country
            ON city.CountryCode = country.Code
        ORDER BY city.Population DESC, city.ID ASC
        LIMIT ?
        """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, n);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    City city = new City();

                    city.setId(resultSet.getInt("ID"));
                    city.setName(resultSet.getString("Name"));
                    city.setCountryCode(
                            resultSet.getString("CountryCode"));
                    city.setDistrict(
                            resultSet.getString("District"));
                    city.setPopulation(
                            resultSet.getInt("Population"));

                    Country country = new Country();

                    country.setCode(
                            resultSet.getString("CountryCode"));
                    country.setName(
                            resultSet.getString("CountryName"));

                    city.setCountry(country);
                    cities.add(city);
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve top " + n
                            + " populated cities in the world.", e);
        }

        return cities;
    }


    /**
     * Issue #41: Top N Populated Cities in a Selected Region.
     * <p>
     * Retrieves the top N most populated cities in a specified region.
     * Results are sorted from largest to smallest population.
     *
     * @param connection Active MySQL database connection
     * @param region     Region selected by the user
     * @param n          Maximum number of cities to return
     * @return List of cities ordered by population
     * @throws SQLException If a database error occurs
     */
    public ArrayList<City> getTopNCitiesByRegion(
            Connection connection, String region, int n)
            throws SQLException {

        ArrayList<City> cities = new ArrayList<>();

        // Validate the selected region and number of cities.
        if (region == null || region.trim().isEmpty() || n <= 0) {
            System.out.println("Invalid region or N value.");
            return cities;
        }

        // Join city and country to filter by region.
        // Retrieve the country name for displaying the report.
        String sql =
                "SELECT ci.ID, ci.Name, ci.CountryCode, " +
                        "ci.District, ci.Population, " +
                        "co.Name AS CountryName " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Region = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC " +
                        "LIMIT ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind parameters safely.
            statement.setString(1, region);
            statement.setInt(2, n);

            // Execute the query.
            try (ResultSet rs = statement.executeQuery()) {

                // Convert each result row into a City object.
                while (rs.next()) {

                    City city = new City();

                    city.setId(rs.getInt("ID"));
                    city.setName(rs.getString("Name"));
                    city.setCountryCode(rs.getString("CountryCode"));
                    city.setDistrict(rs.getString("District"));
                    city.setPopulation(rs.getInt("Population"));

                    // Populate the Country object to avoid
                    // NullPointerException when printing the report.
                    Country country = new Country();
                    country.setName(rs.getString("CountryName"));
                    city.setCountry(country);

                    // Add the city to the report.
                    cities.add(city);
                }
            }
        }

        return cities;
    }

}
