
package com.napier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PopulationBreakdownTest {

    // Test 1: Population information
    @Test
    void testPopulationDetails() {
        PopulationBreakdown report =
                new PopulationBreakdown("Asia", 1000, 600);

        assertEquals("Asia", report.getName());
        assertEquals(1000, report.getTotalPopulation());
        assertEquals(600, report.getUrbanPopulation());
    }

    // Test 2: Valid population
    @Test
    void testValidPopulation() {
        PopulationBreakdown report =
                new PopulationBreakdown("Europe", 1000, 600);

        assertTrue(report.isValid());
    }

    // Test 3: Rural population calculation
    @Test
    void testRuralPopulation() {
        PopulationBreakdown report =
                new PopulationBreakdown("Asia", 1000, 600);

        assertEquals(400, report.getRuralPopulation());
    }

    // Test 4: Urban percentage calculation
    @Test
    void testUrbanPercentage() {
        PopulationBreakdown report =
                new PopulationBreakdown("Asia", 1000, 600);

        assertEquals(60.0,
                report.getUrbanPercentage(), 0.001);
    }

    // Test 5: Rural percentage calculation
    @Test
    void testRuralPercentage() {
        PopulationBreakdown report =
                new PopulationBreakdown("Asia", 1000, 600);

        assertEquals(40.0,
                report.getRuralPercentage(), 0.001);
    }

    // Test 6: Zero population
    @Test
    void testZeroPopulation() {
        PopulationBreakdown report =
                new PopulationBreakdown("Unknown", 0, 0);

        assertTrue(report.isValid());
        assertEquals(0, report.getRuralPopulation());
        assertEquals(0.0, report.getUrbanPercentage());
        assertEquals(0.0, report.getRuralPercentage());
    }

    // Test 7: Urban exceeds total population
    @Test
    void testInvalidPopulation() {
        PopulationBreakdown report =
                new PopulationBreakdown("Invalid", 100, 200);

        assertFalse(report.isValid());
        assertEquals(-1, report.getRuralPopulation());
        assertTrue(Double.isNaN(
                report.getUrbanPercentage()));
        assertTrue(Double.isNaN(
                report.getRuralPercentage()));
    }

    // Test 8: Negative total population
    @Test
    void testNegativeTotalPopulation() {
        assertThrows(IllegalArgumentException.class,
                () -> new PopulationBreakdown(
                        "Invalid", -100, 50));
    }

    // Test 9: Negative urban population
    @Test
    void testNegativeUrbanPopulation() {
        assertThrows(IllegalArgumentException.class,
                () -> new PopulationBreakdown(
                        "Invalid", 100, -50));
    }

    // Test 10: All population is urban
    @Test
    void testFullyUrbanPopulation() {
        PopulationBreakdown report =
                new PopulationBreakdown("City", 500, 500);

        assertTrue(report.isValid());
        assertEquals(0, report.getRuralPopulation());
        assertEquals(100.0,
                report.getUrbanPercentage());
        assertEquals(0.0,
                report.getRuralPercentage());
    }
}
