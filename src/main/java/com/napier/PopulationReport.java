
package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates population breakdown reports for
 * continents, regions, and countries.
 */
public class PopulationReport {

    private final Connection connection;

    public PopulationReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves population data grouped by a column.
     *
     * @param groupColumn Continent, Region, or Name
     * @return List of population breakdowns
     */
    private List<PopulationBreakdown> getPopulationReport(
            String groupColumn) throws SQLException {

        // Only predefined grouping columns are allowed.
        if (!groupColumn.equals("Continent")
                && !groupColumn.equals("Region")
                && !groupColumn.equals("Name")) {
            throw new IllegalArgumentException(
                    "Invalid report scope");
        }

        String sql = """
            SELECT
                c.%s AS ReportName,
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
            GROUP BY c.%s
            ORDER BY c.%s
            """.formatted(
                groupColumn,
                groupColumn,
                groupColumn
        );

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


                if (total < 0 || urban < 0 || urban > total) {
                    System.err.printf(
                            "Invalid population: %s | Total: %,d | Urban: %,d%n",
                            name, total, urban
                    );
                    continue;
                }

                PopulationBreakdown report =
                        new PopulationBreakdown(name, total, urban);


                reports.add(report);
            }
        }

        return reports;
    }

    /**
     * Issue #50: Population breakdown by continent.
     */
    public List<PopulationBreakdown> getContinentReport()
            throws SQLException {

        return getPopulationReport("Continent");
    }

    /**
     * Issue #51: Population breakdown by region.
     */
    public List<PopulationBreakdown> getRegionReport()
            throws SQLException {

        return getPopulationReport("Region");
    }

    /**
     * Issue #52: Population breakdown by country.
     */
    public List<PopulationBreakdown> getCountryReport()
            throws SQLException {

        return getPopulationReport("Name");
    }

    /**
     * Displays a population report in table format.
     */
    public void printReport(
            String title,
            List<PopulationBreakdown> reports) {

        System.out.println("\n" + title);
        System.out.println("=".repeat(115));

        System.out.printf(
                "%-35s %18s %15s %10s %15s %10s%n",
                "Name",
                "Total Population",
                "Urban",
                "Urban %",
                "Rural",
                "Rural %"
        );

        System.out.println("-".repeat(115));

        if (reports.isEmpty()) {
            System.out.println(
                    "No population data available.");
        }

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
     * Generates the continent report.
     */
    public void generateContinentReport()
            throws SQLException {

        printReport(
                "Population Breakdown by Continent",
                getContinentReport()
        );
    }

    /**
     * Generates the region report.
     */
    public void generateRegionReport()
            throws SQLException {

        printReport(
                "Population Breakdown by Region",
                getRegionReport()
        );
    }

    /**
     * Generates the country report.
     */
    public void generateCountryReport()
            throws SQLException {

        printReport(
                "Population Breakdown by Country",
                getCountryReport()
        );
    }

    /**
     * Generates all three reports.
     */
    public void generateAllReports()
            throws SQLException {

        generateContinentReport();
        generateRegionReport();
        generateCountryReport();
    }
}
