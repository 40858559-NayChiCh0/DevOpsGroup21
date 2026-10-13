package com.napier;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            if (connection.isValid(2)) {
                System.out.println(
                        "Database connection succeeded.");

                CapitalCityReports reports =
                        new CapitalCityReports(connection);

                System.out.println(
                        "\nREPORT 1: All Capital Cities in the World");
                reports.getAllCapitalCities();

                System.out.println(
                        "\nREPORT 2: Capital Cities in Asia");
                reports.getCapitalCitiesByContinent("Asia");
            } else {
                System.out.println(
                        "Database connection is invalid.");
            }

        } catch (SQLException e) {
            System.err.println(
                    "Database connection failed: " + e.getMessage());

            e.printStackTrace();
        }
    }
}