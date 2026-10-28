
package com.napier;

/**
 * Represents a city in the world database.
 */
public class City {

    private int id;
    private String name;
    private String countryCode;
    private String district;
    private int population;

    // Country associated with the city
    private Country country;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * Gets the country associated with the city.
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the country associated with the city.
     */
    public void setCountry(Country country) {
        this.country = country;
    }
}
