
package com.napier;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            if (connection.isValid(2)) {

                System.out.println("Database connection succeeded.");

                System.out.println("\n--------------------------------------------------------------------------------------");
                System.out.println(
                        "Report 1: All the countries in the world organised by largest population to smallest.");
                System.out.println("--------------------------------------------------------------------------------------");

                CountryReports.getAllCountries();
                System.out.println("----------------------------------------------------------------------------------------------------------------");

                System.out.println(
                        "Report 2: All the countries in a continent organised by largest population to smallest. (Continent of the Asia )");
                System.out.println("----------------------------------------------------------------------------------------------------------------");
                CountryReports.getCountriesByContinent("Asia");

                System.out.println("-------------------------------------------------------------------------------------------------------------------");
                System.out.println(
                        "Report 3: All the countries in a region organised by largest population to smallest. (Region of the Southeast Asia)");
                System.out.println("-------------------------------------------------------------------------------------------------------------------");
                CountryReports.getCountriesByRegion("Southeast Asia");

            } else {
                System.out.println("Database connection is invalid.");
            }

        } catch (SQLException e) {

            System.err.println("Database connection failed: "
                    + e.getMessage());

            e.printStackTrace();
        }

    }
}
