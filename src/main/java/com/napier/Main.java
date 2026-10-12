package com.napier;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main(String[] args) {

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
    }
}
