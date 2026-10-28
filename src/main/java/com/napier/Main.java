
package com.napier;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection connection =
                     DBConnection.getConnection()) {

            if (connection.isValid(2)) {

                System.out.println(
                        "Database connection succeeded.");

                // Population Breakdown & Urbanization Reports
                PopulationReport report =
                        new PopulationReport(connection);

                // Issue #50: Continent report
                report.generateContinentReport();

                // Issue #51: Region report
                report.generateRegionReport();

                // Issue #52: Country report
                report.generateCountryReport();

            } else {

                System.err.println(
                        "Database connection is invalid.");
            }

        } catch (SQLException | IllegalArgumentException e) {

            System.err.println(
                    "Database connection or report failed: "
                            + e.getMessage());

            e.printStackTrace();
        }
    }
}
