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

                // Generate world population report
                LookUp_PopulationReports.getWorldPopulation(connection);

                // Report 2: Population of Asia
                LookUp_PopulationReports.getContinentPopulation("North America", connection);


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