
package com.napier;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection connection =
                     DBConnection.getConnection()) {

            if (connection != null && connection.isValid(2)) {

                System.out.println(
                        "Database connection succeeded.");

                CapitalCityReports reports =
                        new CapitalCityReports(connection);

                // REPORT 1: All capital cities in the world
                System.out.println(
                        "\nREPORT 1: All Capital Cities in the World");

                reports.getAllCapitalCities();


                // REPORT 2: All capital cities in Asia
                System.out.println(
                        "\nREPORT 2: Capital Cities in Asia");

                reports.getCapitalCitiesByContinent("Asia");


                // REPORT 3: All capital cities in Southeast Asia
                System.out.println(
                        "\nREPORT 3: Capital Cities in Southeast Asia");

                reports.getCapitalCitiesByRegion("Southeast Asia");


                // REPORT 4: Top 10 capital cities in the world
                System.out.println(
                        "\nREPORT 4: Top 10 Capital Cities in the World");

                reports.getTopNCapitalCities(10);

            } else {
                System.out.println(
                        "Database connection is invalid.");
            }

        } catch (SQLException e) {

            System.err.println(
                    "Database connection failed: " +
                            e.getMessage());

            e.printStackTrace();
        }
    }
}
