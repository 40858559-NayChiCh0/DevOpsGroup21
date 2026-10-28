
package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates population breakdown reports
 * for continents, regions, and countries.
 */
public class PopulationReport {

    private final Connection connection;

    public PopulationReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves population data grouped by continent,
     * region, or country.
     *
     * @param groupColumn Continent, Region, or Code
     * @return List of population breakdowns
     */
    private List<PopulationBreakdown> getPopulationReport(
            String groupColumn) throws SQLException {

        // Allow only predefined grouping columns.
        if (!groupColumn.equals("Continent")
                && !groupColumn.equals("Region")
                && !groupColumn.equals("Code")) {

            throw new IllegalArgumentException(
                    "Invalid report scope");
        }

        // Display country names while grouping by country code.
        String reportName = groupColumn.equals("Code")
                ? "c.Name"
                : "c." + groupColumn;

        String sql = """
            SELECT
                %s AS ReportName,
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
            ORDER BY ReportName
            """.formatted(reportName, groupColumn);

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

        return getPopulationReport("Code");
    }

    /**
     * Displays population data in a formatted table.
     */
    public void printReport(
            String title,
            List<PopulationBreakdown> reports) {

        System.out.println("\n" + title);
        System.out.println("=".repeat(120));

        System.out.printf(
                "%-35s %18s %15s %10s %15s %10s%n",
                "Name",
                "Total Population",
                "Urban",
                "Urban %",
                "Rural",
                "Rural %"
        );

        System.out.println("-".repeat(120));

        if (reports.isEmpty()) {
            System.out.println(
                    "No population data available.");
        }

        for (PopulationBreakdown report : reports) {

            if (!report.isValid()) {

                // Display unavailable values for inconsistent data.
                System.out.printf(
                        "%-35.35s %,18d %,15d %10s %15s %10s%n",
                        report.getName(),
                        report.getTotalPopulation(),
                        report.getUrbanPopulation(),
                        "N/A",
                        "N/A",
                        "N/A"
                );

                System.err.printf(
                        "Warning: Invalid population data for %s "
                                + "(Total: %,d, Urban: %,d)%n",
                        report.getName(),
                        report.getTotalPopulation(),
                        report.getUrbanPopulation()
                );

            } else {

                System.out.printf(
                        "%-35.35s %,18d %,15d %9.2f%% %,15d %9.2f%%%n",
                        report.getName(),
                        report.getTotalPopulation(),
                        report.getUrbanPopulation(),
                        report.getUrbanPercentage(),
                        report.getRuralPopulation(),
                        report.getRuralPercentage()
                );
            }
        }

        System.out.println("=".repeat(120));
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
     * Generates all three population reports.
     */
    public void generateAllReports()
            throws SQLException {

        generateContinentReport();
        generateRegionReport();
        generateCountryReport();
    }
}
