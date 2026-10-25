package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CityReports {

    /**
     * Issue 34: All cities in the world
     * Gets all cities in the world ordered by population.
     *
     * @param connection Database connection.
     * @return List of cities.
     */
    public ArrayList<City> getAllCities(Connection connection) {

        ArrayList<City> cities = new ArrayList<>();

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
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

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

                // Create a Country object
                Country country = new Country();

                country.setCode(
                        resultSet.getString("CountryCode"));
                country.setName(
                        resultSet.getString("CountryName"));

                // Associate the country with the city
                city.setCountry(country);

                cities.add(city);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to retrieve city report.", e);
        }

        return cities;
    }

    /**
     * Prints cities in a formatted table with a dynamic title.
     *
     * @param cities List of cities.
     * @param title Title of the report.
     */
    public void printCityReport(ArrayList<City> cities, String title) {

        System.out.println(
                "\n========== " + title + " ==========");

        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found.");
            return;
        }

        System.out.printf(
                "%-25s %-25s %-25s %15s%n",
                "Name", "Country", "District", "Population");

        System.out.println("-".repeat(95));

        for (City city : cities) {

            System.out.printf(
                    "%-25.25s %-25.25s %-25.25s %,15d%n",
                    city.getName(),
                    city.getCountry().getName(),
                    city.getDistrict(),
                    city.getPopulation());
        }

        System.out.println("-".repeat(95));
        System.out.println("Total cities: " + cities.size());
    }


    /**
     * Issue 35: All cities in a selected continent
     * Retrieves all cities in a selected continent,
     * ordered by population from largest to smallest.
     *
     * @param connection Active database connection.
     * @param continent Name of the selected continent.
     * @return List of cities in the selected continent.
     */
    public ArrayList<City> getCitiesByContinent(
            Connection connection, String continent) {

        ArrayList<City> cities = new ArrayList<>();

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
            WHERE country.Continent = ?
            ORDER BY city.Population DESC, city.ID ASC
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, continent);

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
                    "Failed to retrieve cities for continent: "
                            + continent, e);
        }
        return cities;
    }


    /**
     * Issue 36: Gets all cities in a selected region,
     * ordered by population from largest to smallest.
     *
     * @param connection Database connection.
     * @param region Selected region.
     * @return List of cities in the selected region.
     */
    public ArrayList<City> getCitiesByRegion(
            Connection connection, String region) {

        ArrayList<City> cities = new ArrayList<>();

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
            WHERE country.Region = ?
            ORDER BY city.Population DESC, city.ID ASC
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, region);

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
                    "Failed to retrieve cities for region: "
                            + region, e);
        }

        return cities;
    }

}