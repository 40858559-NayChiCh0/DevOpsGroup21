package com.napier;

/**
 * Represents a country in the world database.
 */
public class Country {


    private String code;
    private String name;
    private String continent;
    private String region;
    private int population;

    /**
     * Gets the country code.
     *
     * @return The country code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the country code.
     *
     * @param code The country code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the country name.
     *
     * @return The country name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the country name.
     *
     * @param name The country name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the continent.
     *
     * @return The continent.
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Sets the continent.
     *
     * @param continent The continent.
     */
    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * Gets the geographical region.
     *
     * @return The region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the geographical region.
     *
     * @param region The region.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the country population.
     *
     * @return The population.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets the country population.
     *
     * @param population The population.
     */
    public void setPopulation(int population) {
        this.population = population;
    }

}
