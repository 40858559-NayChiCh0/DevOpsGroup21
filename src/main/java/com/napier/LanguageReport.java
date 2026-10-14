package com.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageReport {
    // REPORT 1: Language speakers in the world
    public static void getLanguageSpeakers(Connection connection) {

        String sql = """
                SELECT
                    cl.Language AS Language,
                    ROUND(SUM(c.Population * cl.Percentage / 100)) AS Speakers,
                    ROUND(
                        SUM(c.Population * cl.Percentage / 100) * 100.0 /
                        (SELECT SUM(Population) FROM country),
                        2
                    ) AS WorldPercentage
                FROM countrylanguage cl
                JOIN country c ON cl.CountryCode = c.Code
                WHERE cl.Language IN (
                    'Chinese', 'English', 'Hindi',
                    'Spanish', 'Arabic'
                )
                GROUP BY cl.Language
                ORDER BY Speakers DESC
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("------------------------------------------------------");
            // Display report headings
            System.out.printf("%-15s %-20s %-20s%n",
                    "Language", "Speakers", "World Population (%)");
            System.out.println("------------------------------------------------------");

            // Display language results
            while (rs.next()) {
                System.out.printf("%-15s %-20d %-20.2f%n",
                        rs.getString("Language"),
                        rs.getLong("Speakers"),
                        rs.getDouble("WorldPercentage"));
            }

        } catch (SQLException e) {
            System.err.println("Error generating language report: "
                    + e.getMessage());
        }
    }
}
