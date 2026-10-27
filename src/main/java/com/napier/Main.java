package com.napier;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DBConnection.getConnection()) {
            if (!connection.isValid(2)) {
                System.err.println("Database connection is invalid.");
                return;
            }
            System.out.println("Database connection succeeded.");

            // Country reports (these methods manage their own connections).
            System.out.println("\nREPORT: Country Reports");
            System.out.println("\n===========================");
            System.out.println("\nAll Countries in the World");
            CountryReports.getAllCountries();
            System.out.println("\nCountries in Asia");
            CountryReports.getCountriesByContinent("Asia");
            System.out.println("\nCountries in Southeast Asia");
            CountryReports.getCountriesByRegion("Southeast Asia");

            // City reports.
            System.out.println("\nREPORT: City Reports");
            System.out.println("\n===========================");
            CityReports cityReports = new CityReports();
            cityReports.printCityReport(cityReports.getAllCities(connection),
                    "All Cities in the World");
            cityReports.printCityReport(cityReports.getCitiesByContinent(connection, "Asia"),
                    "Cities in Asia");
            cityReports.printCityReport(cityReports.getCitiesByRegion(connection, "Southeast Asia"),
                    "Cities in Southeast Asia");
            cityReports.printCityReport(cityReports.getCitiesByCountry(connection, "Myanmar"),
                    "Cities in Myanmar");
            cityReports.printCityReport(cityReports.getCitiesByDistrict(connection, "Mandalay"),
                    "Cities in Mandalay");

            //Top N City Reports
            TopNCityReports topNCityReports = new TopNCityReports();

            // Issue #41: Top N Populated Cities in a Selected Region
            System.out.println("\nREPORT: Top N Populated Cities in a Region");
            System.out.println("========================================");

            // Retrieve the five most populated cities
            // in Southeast Asia.
            String continent = "Asia";
            String region = "Southeast Asia";
            int regionTopN = 5;

            // Issue #39: Top N Populated Cities in the world
            cityReports.printCityReport(
                    topNCityReports.getTopNCitiesInWorld(
                            connection, regionTopN),
                    "Top " + regionTopN
                            + " Populated Cities in the World"
            );

            // Issue #40: Top N Populated Cities in
            cityReports.printCityReport(
                    topNCityReports.getTopNCitiesByContinent(
                            connection,continent, regionTopN),
                    "Top " + regionTopN
                            + " Populated Cities in " + continent
            );

            // Retrieve and display the report.
            cityReports.printCityReport(
                    topNCityReports.getTopNCitiesByRegion(
                            connection, region, regionTopN),
                    "Top " + regionTopN
                            + " Populated Cities in " + region
            );


            // Issue #42: Top N Populated Cities in a Selected Country
            System.out.println("\nREPORT: Top N Populated Cities in a Country");
            System.out.println("==========================================");

            //Top 5 most populated cities in Myanmar.
            String countryName = "Myanmar";
            int countryTopN = 5;

            // Retrieve and display the report.
            cityReports.printCityReport(
                    topNCityReports.getTopNCitiesByCountry(
                            connection, countryName, countryTopN),
                    "Top " + countryTopN
                            + " Populated Cities in " + countryName
            );



            // Capital city reports.
            System.out.println("\nREPORT: Capital Cities Reports");
            System.out.println("\n===============================");
            CapitalCityReports capitalReports = new CapitalCityReports(connection);
            System.out.println("\nCapital Cities in the World");
            capitalReports.getAllCapitalCities();
            System.out.println("\nCapital Cities in Asia");
            capitalReports.getCapitalCitiesByContinent("Asia");
            System.out.println("\nCapital Cities in Southeast Asia");
            capitalReports.getCapitalCitiesByRegion("Southeast Asia");


            // Population breakdown report by continent
            System.out.println("\nREPORT: Population Breakdown");
            System.out.println("=================================");

            PopulationReport populationReport = new PopulationReport(connection);
            populationReport.generateContinentReport();


            // Population lookup reports.
            System.out.println("\nREPORT: Population LookUp Reports");
            System.out.println("\n=================================");
            LookUp_Population.getWorldPopulation(connection);
            LookUp_Population.getContinentPopulation("Asia", connection);
            LookUp_Population.getRegionPopulation("Southeast Asia", connection);

            // Language report.
            System.out.println("\nREPORT: Language Speakers Report");
            System.out.println("\n===========================");
            LanguageReport.getLanguageSpeakers(connection);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
