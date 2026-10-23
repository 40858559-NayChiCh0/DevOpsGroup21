
package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates population breakdown reports for continents.
 */
public class PopulationReport {

    private final Connection connection;

    public PopulationReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves population data for each continent.
     *
     * @return List of population breakdowns
     */
    public List<PopulationBreakdown> getContinentReport()
            throws SQLException {

        String sql = """
            SELECT
                c.Continent AS ReportName,
                SUM(c.Population) AS TotalPopulation,
                SUM(COALESCE(cp.UrbanPopulation, 0))
                    AS UrbanPopulation
            FROM country c
            LEFT JOIN (
                SELECT
                    CountryCode,
                    SUM(Population) AS UrbanPopulation
                FROM city
                GROUP BY CountryCode
            ) cp ON c.Code = cp.CountryCode
            GROUP BY c.Continent
            ORDER BY c.Continent
            """;

        List<PopulationBreakdown> reports =
                new ArrayList<>();

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {
            while (resultSet.next()) {

                String name =
                        resultSet.getString("ReportName");

                long total =
                        resultSet.getLong("TotalPopulation");

                long urban =
                        resultSet.getLong("UrbanPopulation");

                PopulationBreakdown report =
                        new PopulationBreakdown(
                                name, total, urban);

                reports.add(report);
            }
        }

        return reports;
    }

    /**
     * Displays population data in a formatted table.
     */
    public void printReport(
            String title,
            List<PopulationBreakdown> reports) {

        System.out.println("\n" + title);
        System.out.println("=".repeat(115));

        System.out.printf(
                "%-35s %18s %15s %10s %15s %10s%n",
                "Continent",
                "Total Population",
                "Urban",
                "Urban %",
                "Rural",
                "Rural %"
        );

        System.out.println("-".repeat(115));

        for (PopulationBreakdown report : reports) {

            System.out.printf(
                    "%-35s %,18d %,15d %9.2f%% %,15d %9.2f%%%n",
                    report.getName(),
                    report.getTotalPopulation(),
                    report.getUrbanPopulation(),
                    report.getUrbanPercentage(),
                    report.getRuralPopulation(),
                    report.getRuralPercentage()
            );
        }

        System.out.println("=".repeat(115));
    }

    /**
     * Generates the continent population breakdown report.
     */
    public void generateContinentReport() throws SQLException {

        printReport(
                "Population Breakdown by Continent",
                getContinentReport()
        );
    }
}
