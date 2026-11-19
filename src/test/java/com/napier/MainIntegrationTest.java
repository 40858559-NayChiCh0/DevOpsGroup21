
package com.napier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests against the real MySQL world database.
 * Start the database before running this test class.
 */
@Tag("integration")
public class MainIntegrationTest {

    private static Connection connection;
    private static CityReports cityReports;
    private static TopNCityReports topNCityReports;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DBConnection.getConnection();

        assertNotNull(connection,
                "Database connection must not be null");

        assertTrue(connection.isValid(5),
                "MySQL must be reachable");

        cityReports = new CityReports();
        topNCityReports = new TopNCityReports();
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    @Test
    void testMainMethod() {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        try (PrintStream capturedOut = new PrintStream(stdout, true, StandardCharsets.UTF_8);
             PrintStream capturedErr = new PrintStream(stderr, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOut);
            System.setErr(capturedErr);

            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        String errors = stderr.toString(StandardCharsets.UTF_8);
        assertTrue(errors.lines().allMatch(line -> line.isBlank()
                        || line.startsWith("Warning: Invalid population data for ")),
                () -> "Unexpected error from Main:\n" + errors);

        String output = stdout.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Database connection succeeded."));

        List<String> reportHeadings = output.lines()
                .filter(line -> line.startsWith("REPORT: "))
                .toList();

        assertEquals(List.of(
                "REPORT: Country Reports",
                "REPORT: City Reports",
                "REPORT: Top N Populated Cities Reports",
                "REPORT: Capital Cities Reports",
                "REPORT: Population Breakdown",
                "REPORT: Population LookUp Reports",
                "REPORT: Language Speakers Report"
        ), reportHeadings);

        assertTrue(output.lines().anyMatch(line -> line.startsWith("Chinese ")),
                "Expected the Chinese language row from the seeded world database");
    }
}
