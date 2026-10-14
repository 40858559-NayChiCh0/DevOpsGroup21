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

                // Generate and display the language speakers report
                System.out.println("\nREPORT : Language Reports");
                LanguageReport.getLanguageSpeakers(connection);


                // Generate world population
                LookUp_Population.getWorldPopulation(connection);

                // Report 2: Population of Asia
                LookUp_Population.getContinentPopulation("North America", connection);
                // Report 3: Population of Southeast Asia
                LookUp_Population.getRegionPopulation("Southeast Asia", connection);


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
