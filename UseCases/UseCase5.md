# Use Case: Generate Capital City Reports Information

## CHARACTERISTIC INFORMATION

### Goal in Context

As a Data Analyst, I want to view the population of the world so that I can have a global population summary.

As a Data Analyst, I want to view the population of a continent so that I can analyse demographic scale continentally.

As a Data Analyst, I want to view the population of a region so that I can analyse population in specific regions.

As a Data Analyst, I want to view the population of a country so that I can review and analyse national population statistics.

As a Data Analyst, I want to view the population of a district so that I can analyse smaller administrative areas.

As a Data Analyst, I want to view the population of a city so that I can analyse urban population sizes.

### Scope

Population Reporting System, covering world, continent, region, country, district and city population lookups.

### Level

Primary task

### Preconditions

* The system is running and connected to the MySQL database.
* The database contains the required population information.
* The Data Analyst can select a lookup type and provide the required geographical name where applicable.

### Success Condition

The system retrieves and displays the population of the selected geographical area.

### Failed Condition

The population cannot be retrieved because of a database connection failure, SQL error, invalid input or unavailable matching records.

### Primary Actor

Data Analyst

### Trigger

The Data Analyst selects a population lookup option in the Population Reporting System.

## MAIN SUCCESS SCENARIO

1. The Data Analyst selects a population lookup type.
2. The system requests the geographical name where required.
3. The Data Analyst provides the required information.
4. The system retrieves and displays the population value.
5. The Data Analyst reviews the result.

## EXTENSIONS

* Invalid geographical name: The system displays an error message.
* No matching records found: The system displays an appropriate message.
* Database or SQL error: The system reports the error where handled by the application.

## SUB-VARIATIONS

* World: Retrieve the total world population.
* Continent: Retrieve the population of a selected continent.
* Region: Retrieve the population of a selected region.
* Country: Retrieve the population of a selected country.
* District: Retrieve the population of a selected district.
* City: Retrieve the population of a selected city.

## SCHEDULE

Due date: Release v1.0.1