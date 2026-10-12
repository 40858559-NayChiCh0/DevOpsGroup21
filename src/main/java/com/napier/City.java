package com.napier;

/**
 * Represents a city in the world database.
 */
public class City {

    /**
     * The unique ID of the city.
     */
    private int id;

    /**
     * The name of the city.
     */
    private String name;

    /**
     * The country code of the city.
     */
    private String countryCode;

    /**
     * The district where the city is located.
     */
    private String district;

    /**
     * The total population of the city.
     */
    private int population;

    /**
     * Gets the city ID.
     *
     * @return The city ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the city ID.
     *
     * @param id The city ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the city name.
     *
     * @return The city name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the city name.
     *
     * @param name The city name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the country code.
     *
     * @return The country code.
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the country code.
     *
     * @param countryCode The country code.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Gets the district.
     *
     * @return The district.
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Sets the district.
     *
     * @param district The district.
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Gets the city population.
     *
     * @return The population.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets the city population.
     *
     * @param population The population.
     */
    public void setPopulation(int population) {
        this.population = population;
    }
}