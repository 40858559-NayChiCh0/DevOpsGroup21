
package com.napier;

/**
 * Represents population information for a continent,
 * region, or country.
 */
public class PopulationBreakdown {

    private final String name;
    private final long totalPopulation;
    private final long urbanPopulation;

    public PopulationBreakdown(String name,
                               long totalPopulation,
                               long urbanPopulation) {

        if (totalPopulation < 0 || urbanPopulation < 0) {
            throw new IllegalArgumentException(
                    "Population cannot be negative");
        }

        this.name = name;
        this.totalPopulation = totalPopulation;
        this.urbanPopulation = urbanPopulation;
    }

    public String getName() {
        return name;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public long getUrbanPopulation() {
        return urbanPopulation;
    }

    /**
     * Checks whether the population values are consistent.
     */
    public boolean isValid() {
        return urbanPopulation <= totalPopulation;
    }

    /**
     * Calculates rural population.
     * Returns -1 when population data is inconsistent.
     */
    public long getRuralPopulation() {

        if (!isValid()) {
            return -1;
        }

        return totalPopulation - urbanPopulation;
    }

    /**
     * Calculates urban population percentage.
     * Returns NaN when the data is inconsistent.
     */
    public double getUrbanPercentage() {

        if (!isValid()) {
            return Double.NaN;
        }

        if (totalPopulation == 0) {
            return 0.0;
        }

        return urbanPopulation * 100.0 / totalPopulation;
    }

    /**
     * Calculates rural population percentage.
     * Returns NaN when the data is inconsistent.
     */
    public double getRuralPercentage() {

        if (!isValid()) {
            return Double.NaN;
        }

        if (totalPopulation == 0) {
            return 0.0;
        }

        return getRuralPopulation() * 100.0 / totalPopulation;
    }
}
