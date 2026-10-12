package com.napier;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection con = DBConnection.getConnection()) {

            System.out.println("Database connected successfully!");

            CapitalCityReports reports = new CapitalCityReports(con);

            System.out.println(
                    "\nREPORT 1: All Capital Cities in the World");
            reports.getAllCapitalCities();

            System.out.println(
                    "\nREPORT 2: Capital Cities in Asia");
            reports.getCapitalCitiesByContinent("Asia");

            System.out.println(
                    "\nREPORT 3: Capital Cities in Southeast Asia");
            reports.getCapitalCitiesByRegion("Southeast Asia");


        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}