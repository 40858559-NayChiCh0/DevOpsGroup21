package com.napier;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    static void main(String[] args) {

        // Change these values to generate reports for other locations.
        String continent = "Asia";
        String region = "Eastern Asia";
        String countryName = "Myanmar";

        try (Connection connection =
                     DBConnection.getConnection()) {

            if (connection.isValid(2)) {
                System.out.println(
                        "Database connection succeeded.");
                CityReports cityReport = new CityReports();


                // Issue 34: All cities in the world by population.
                ArrayList<City> cities =
                        cityReport.getAllCities(connection);

                cityReport.printCityReport(
                        cities,
                        "ALL CITIES IN THE WORLD BY POPULATION");

                // Issue 35: All cities in a continent(Asia) by population.
                ArrayList<City> continentCities =
                        cityReport.getCitiesByContinent(
                                connection, continent);

                cityReport.printCityReport(
                        continentCities,
                        "ALL CITIES IN " + continent.toUpperCase()
                                + " BY POPULATION");

                // Issue 36: All cities in a region by population.
                ArrayList<City> regionCities =
                        cityReport.getCitiesByRegion(
                                connection, region);

                cityReport.printCityReport(
                        regionCities,
                        "ALL CITIES IN " + region.toUpperCase()
                                + " BY POPULATION");

                // Issue 37: All cities in a selected country
                ArrayList<City> countryCities =
                        cityReport.getCitiesByCountry(
                                connection, countryName);
                cityReport.printCityReport(countryCities,
                        "ALL CITIES IN " + countryName.toUpperCase()
                                + "BY POPULATION");

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
