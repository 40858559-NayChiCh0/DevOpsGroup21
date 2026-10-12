# USE CASE: Generate Population Breakdown & Urbanization Reports

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *Data Analyst*, I want *to view the total population, urban population, and rural population with counts and percentages in each continent* so that *I can compare population distribution across continents.*

As a *Data Analyst*, I want *to view the total population, urban population, and rural population with counts and percentages in each region* so that *I can analyze population distribution across regions.*

As a *Data Analyst*, I want *to view the total population, urban population, and rural population with counts and percentages in each country* so that *I can compare population distribution across countries.*

### Scope

Covering continent, region, and country levels.

### Level

Primary task

### Preconditions

The database contains population data for countries and cities with correct geographic classifications.

The user knows the required report scope (continent, region, or country).

### Success End Condition

The system generates and displays the corresponding population reports which include the columns: Name, Total Population, Urban Population, Urban Percentage, Rural Population, Rural Percentage.

### Failed End Condition

No report is produced.

### Primary Actor

Data Analyst

### Trigger

A request for population breakdown and urbanization reports initiated by WHO internal processes or external stakeholders.

## MAIN SUCCESS SCENARIO

1. Data Analyst initiates a request for a population breakdown and urbanization report.
2. Data Analyst selects the report scope (continent, region, or country).
3. System retrieves the corresponding population data from the database.
4. System calculates the urban and rural populations with their percentages.
5. System generates and displays the population breakdown report.

## EXTENSIONS

2. **If the report scope is missing or invalid:** System requests correction or notifies the user.

3. **If population data is unavailable:** System notifies the user that no data is available.

## SUB-VARIATIONS

None

## SCHEDULE

**DUE DATE**: Release 1.0