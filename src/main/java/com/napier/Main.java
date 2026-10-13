package com.napier;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    static void main(String[] args) {

        // Change these values to generate reports for other locations.
        String continent = "Asia";

        try (Connection connection =
                     DBConnection.getConnection()) {

            if (connection.isValid(2)) {
                System.out.println(
                        "Database connection succeeded.");
                CityReports cityReport = new CityReports();

                // Issue 34: All cities in the world by population.
                System.out.println("\nIssue 1: All Cities in the World by Population");
                ArrayList<City> cities = cityReport.getAllCities(connection);
                cityReport.printCityReport(cities);

                // Issue 35: All cities in a continent(Asia) by population.
                System.out.println("\nIssue 2: All Cities in " + continent + " by Population");
                ArrayList<City> continentCities =
                        cityReport.getCitiesByContinent(connection, continent);
                cityReport.printCityReport(continentCities);
            } else {
                System.out.println(
                        "Database connection is invalid.");
            }

        } catch (SQLException e) {

            System.err.println(
                    "Database connection failed: "
                            + e.getMessage());

            e.printStackTrace();
        }
    }
}
