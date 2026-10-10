package com.napier;

/**
 * Represents a country in the world database.
 */
public class Country {

    /**
     * unique three-letter country code.
     */
    private String code;

    /**
     * name of the country.
     */
    private String name;

    /**
     * continent where the country is located.
     */
    private String continent;

    /**
     * geographical region of the country.
     */
    private String region;

    /**
     * total population of the country.
     */
    private int population;

    /**
     * Gets country code.
     *
     * @return  country code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets country code.
     *
     * @param code country code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets country name.
     *
     * @return country name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets country name.
     *
     * @param name country name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets continent.
     *
     * @return continent.
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Sets continent.
     *
     * @param continent continent.
     */
    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * Gets geographical region.
     *
     * @return region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets geographical region.
     *
     * @param region region.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets country population.
     *
     * @return population.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets country population.
     *
     * @param population population.
     */
    public void setPopulation(int population) {
        this.population = population;
    }

}
