
package com.napier;

/**
 * Represents population information for a continent,
 * region, or country.
 */
public class PopulationBreakdown {

    private final String name;
    private final long totalPopulation;
    private final long urbanPopulation;
    private final long ruralPopulation;

    public PopulationBreakdown(String name,
                               long totalPopulation,
                               long urbanPopulation) {

        if (totalPopulation < 0 || urbanPopulation < 0
                || urbanPopulation > totalPopulation) {
            throw new IllegalArgumentException(
                    "Invalid population values");
        }

        this.name = name;
        this.totalPopulation = totalPopulation;
        this.urbanPopulation = urbanPopulation;
        this.ruralPopulation =
                totalPopulation - urbanPopulation;
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

    public long getRuralPopulation() {
        return ruralPopulation;
    }

    public double getUrbanPercentage() {
        if (totalPopulation == 0) {
            return 0.0;
        }

        return urbanPopulation * 100.0 / totalPopulation;
    }

    public double getRuralPercentage() {
        if (totalPopulation == 0) {
            return 0.0;
        }

        return ruralPopulation * 100.0 / totalPopulation;
    }
}
