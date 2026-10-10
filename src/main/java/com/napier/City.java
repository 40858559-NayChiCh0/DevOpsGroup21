package com.napier;

/**
 * Represents a city in the world database.
 */
public class City {

    /**
     * Unique ID of the city.
     */
    private int id;

    /**
     * Name of the city.
     */
    private String name;

    /**
     * Country code of the city.
     */
    private String countryCode;

    /**
     * District where the city is located.
     */
    private String district;

    /**
     * Total population of the city.
     */
    private int population;

    /**
     * Gets city ID.
     *
     * @return city ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets city ID.
     *
     * @param id city ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets city name.
     *
     * @return city name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets city name.
     *
     * @param name city name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets country code.
     *
     * @return country code.
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets country code.
     *
     * @param countryCode country code.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Gets district.
     *
     * @return district.
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Sets district.
     *
     * @param district district.
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Gets city population.
     *
     * @return population.
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Sets city population.
     *
     * @param population population.
     */
    public void setPopulation(int population) {
        this.population = population;
    }
}