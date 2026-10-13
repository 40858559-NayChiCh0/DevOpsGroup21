package com.napier;

import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main(String[] args) {

        try (Connection connection =
                     DBConnection.getConnection()) {

            if (connection.isValid(2)) {
                System.out.println(
                        "Database connection succeeded.");
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

        System.out.printf("Yu Ya Kyaw Feature -> Develop");

        System.out.println(
                "\nReport 1: All the countries in the world organised by largest population to smallest.");

        CountryReports.getAllCountries();
        System.out.println("----------------------------------------------------------------------------------------------------------------");

        System.out.println(
                "Report 2: All the countries in a continent organised by largest population to smallest. (Continent of the Asia )");
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        CountryReports.getCountriesByContinent("Asia");
    }
}
