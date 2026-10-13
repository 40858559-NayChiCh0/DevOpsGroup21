# USE CASE: Generate Country Population Reports

## CHARACTERISTIC INFORMATION

### Goal in Context

* As a Data Analyst, I want to view all countries in the world organised by largest population to smallest, so that I can analyse a global population overview.
* As a Data Analyst, I want to view all countries in a continent organised by largest population to smallest, so that I can analyse population distribution in that continent.
* As a Data Analyst, I want to view all countries in a region organised by largest population to smallest, so that I can analyse population regionally.
* As a Data Analyst, I want to view the top N populated countries in the world, so that I can focus on the largest global countries.
* As a Data Analyst, I want to view the top N populated countries in a continent, so that I can focus on the most populated countries across that continent.
* As a Data Analyst, I want to view the top N populated countries in a region, so that I can study the most populated countries in that region.

### Scope

Population Reporting System, covering worldwide, continent-level and region-level country population reports, including all-country rankings and Top N rankings.

### Level

Primary task

### Preconditions

* The database contains country codes, names, population values, continent and region classifications, and capital information.
* The database connection is available.
* The programme can be executed successfully.
* For continent reports, the Data Analyst can select a continent from the available menu options.
* For region reports, the Data Analyst can select a region from the available menu options.
* For Top N reports, the Data Analyst can provide a valid positive integer for N.

### Success End Condition

The system successfully generates and displays the requested country population report:

1. All countries worldwide, ordered by population from largest to smallest.
2. All countries in a selected continent, ordered by population from largest to smallest.
3. All countries in a selected region, ordered by population from largest to smallest.
4. The top N most populated countries worldwide.
5. The top N most populated countries in a selected continent.
6. The top N most populated countries in a selected region.

The reports display the country code, country name, continent, region, population and capital. All reports are ordered by population from largest to smallest. Top N reports display no more than the requested number of countries.

### Failed End Condition

The requested report cannot be generated because of a database connection failure, SQL execution error, invalid menu selection, invalid Top N value or unavailable matching country records. The system displays an appropriate error or information message where implemented.

### Primary Actor

Data Analyst

### Trigger

The Data Analyst runs the Population Reporting System and selects a country population report to retrieve worldwide, continent-level or region-level population information.

## MAIN SUCCESS SCENARIO

1. The Data Analyst starts the Population Reporting System.
2. The system presents the available country population report options.
3. The Data Analyst selects the required report.
4. The system requests the continent, region or N value when required.
5. The Data Analyst enters the required information.
6. The system validates the input.
7. The system retrieves the matching countries from the database.
8. The system orders the results by population from largest to smallest and applies the Top N limit when required.
9. The system displays the report with the country code, country name, continent, region, population and capital.
10. The Data Analyst reviews the report.

## EXTENSIONS

* **Invalid continent or region:** The system displays an error message if input validation is implemented.
* **Invalid Top N value:** The system rejects a non-numeric, zero or negative value and displays an appropriate error message if validation is implemented.
* **No matching countries found:** The system displays an appropriate information message if implemented.
* **Database connection failure:** The system reports the connection error where handled by the application.
* **SQL execution error:** The system reports the error where handled by the application.
* **Invalid report selection:** The system displays an error message or requests a valid selection if implemented.

## SUB-VARIATIONS

* The Data Analyst can select a different continent for continent-level reports.
* The Data Analyst can select a different region for region-level reports.
* The Data Analyst can specify a different positive value for N in Top N reports.
* The worldwide all-country report retrieves all countries without requiring a continent or region selection.
* The worldwide Top N report retrieves only the N most populated countries globally.
* Continent and region Top N reports restrict results to the selected geographical scope.
* All reports present countries in descending order of population.

## SCHEDULE

**Due Date:** Release v1.0.1
