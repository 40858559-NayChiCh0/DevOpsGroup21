
package com.napier;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageReportTest {

    private ResultSet createResultSet(
            String[] languages,
            long[] speakers,
            double[] percentages) {

        final int[] index = {-1};

        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class<?>[]{ResultSet.class},
                (proxy, method, args) -> {

                    switch (method.getName()) {
                        case "next":
                            index[0]++;
                            return index[0] < languages.length;

                        case "getString":
                            return languages[index[0]];

                        case "getLong":
                            return speakers[index[0]];

                        case "getDouble":
                            return percentages[index[0]];

                        case "close":
                            return null;

                        default:
                            throw new UnsupportedOperationException(
                                    method.getName());
                    }
                });
    }

    private Connection createConnection(ResultSet resultSet) {

        PreparedStatement statement =
                (PreparedStatement) Proxy.newProxyInstance(
                        PreparedStatement.class.getClassLoader(),
                        new Class<?>[]{PreparedStatement.class},
                        (proxy, method, args) -> {

                            switch (method.getName()) {
                                case "executeQuery":
                                    return resultSet;

                                case "close":
                                    return null;

                                default:
                                    throw new UnsupportedOperationException(
                                            method.getName());
                            }
                        });

        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {

                    switch (method.getName()) {
                        case "prepareStatement":
                            return statement;

                        case "close":
                            return null;

                        default:
                            throw new UnsupportedOperationException(
                                    method.getName());
                    }
                });
    }

    private String captureReport(Connection connection) {

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        PrintStream original = System.out;

        try {
            System.setOut(new PrintStream(
                    output, true, StandardCharsets.UTF_8));

            LanguageReport.getLanguageSpeakers(connection);

        } finally {
            System.setOut(original);
        }

        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void testLanguageReportWithData() {

        ResultSet rs = createResultSet(
                new String[]{"Chinese"},
                new long[]{1200000000L},
                new double[]{20.50});

        String output = captureReport(createConnection(rs));

        assertTrue(output.contains("Chinese"));
        assertTrue(output.contains("1200000000"));
        assertTrue(output.contains("20.50"));
    }

    @Test
    public void testLanguageReportWithNoData() {

        ResultSet rs = createResultSet(
                new String[]{},
                new long[]{},
                new double[]{});

        String output = captureReport(createConnection(rs));

        assertTrue(output.contains("Language"));
        assertTrue(output.contains("Speakers"));
        assertFalse(output.contains("Chinese"));
    }

    @Test
    public void testMultipleLanguages() {

        ResultSet rs = createResultSet(
                new String[]{"Chinese", "English", "Hindi"},
                new long[]{
                        1200000000L,
                        500000000L,
                        400000000L
                },
                new double[]{20.50, 8.50, 6.75});

        String output = captureReport(createConnection(rs));

        assertTrue(output.contains("Chinese"));
        assertTrue(output.contains("English"));
        assertTrue(output.contains("Hindi"));
        assertTrue(output.contains("1200000000"));
        assertTrue(output.contains("500000000"));
        assertTrue(output.contains("400000000"));
    }

    @Test
    public void testDecimalPercentage() {

        ResultSet rs = createResultSet(
                new String[]{"Spanish"},
                new long[]{400000000L},
                new double[]{6.256});

        String output = captureReport(createConnection(rs));

        assertTrue(output.contains("Spanish"));
        assertTrue(output.contains("6.26"));
    }

    @Test
    public void testZeroSpeakers() {

        ResultSet rs = createResultSet(
                new String[]{"Arabic"},
                new long[]{0L},
                new double[]{0.0});

        String output = captureReport(createConnection(rs));

        assertTrue(output.contains("Arabic"));
        assertTrue(output.contains("0.00"));
    }

    @Test
    public void testSQLException() {

        Connection connection =
                (Connection) Proxy.newProxyInstance(
                        Connection.class.getClassLoader(),
                        new Class<?>[]{Connection.class},
                        (proxy, method, args) -> {
                            if (method.getName()
                                    .equals("prepareStatement")) {
                                throw new SQLException(
                                        "Test database error");
                            }
                            return null;
                        });

        ByteArrayOutputStream error =
                new ByteArrayOutputStream();

        PrintStream original = System.err;

        try {
            System.setErr(new PrintStream(
                    error, true, StandardCharsets.UTF_8));

            LanguageReport.getLanguageSpeakers(connection);

        } finally {
            System.setErr(original);
        }

        assertTrue(error.toString(StandardCharsets.UTF_8)
                .contains("Error generating language report"));
    }
}
