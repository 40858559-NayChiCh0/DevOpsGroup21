# USE CASE: Generate Cities Reports

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *Data Analyst*, I want *all cities in the world organised by population from largest to smallest* so that *I can identify and compare the most populated cities worldwide*.

As a *Data Analyst*, I want *all cities in a selected continent organised by population from largest to smallest* so that *I can analyse and compare city populations within that continent*.

As a *Data Analyst*, I want *all cities in a selected region organised by population from largest to smallest* so that *I can analyse population distribution across cities within that region*.

As a *Data Analyst*, I want *all cities in a selected country organised by population from largest to smallest* so that *I can compare city populations and identify the most populated cities in that country*.

As a *Data Analyst*, I want *all cities in a selected district organised by population from largest to smallest* so that *I can analyse and compare city populations within that district*.

As a *Data Analyst*, I want *the top N most populated cities in the world, where N is specified by the user* so that *I can identify and compare the largest cities globally*.

As a *Data Analyst*, I want *the top N most populated cities in a selected continent, where N is specified by the user* so that *I can identify and compare the largest cities within that continent*.

As a *Data Analyst*, I want *the top N most populated cities in a selected region, where N is specified by the user* so that *I can analyse and compare the most populated cities within that region*.

As a *Data Analyst*, I want *the top N most populated cities in a selected country, where N is specified by the user* so that *I can identify the largest cities and analyse population distribution within that country*.

As a *Data Analyst*, I want *the top N most populated cities in a selected district, where N is specified by the user* so that *I can identify and compare the most populated cities within that district*.

### Scope

All cities and top N cities reports for the world, continent, region, country and district.

### Level

Primary task.

### Preconditions

The database contains city population data and the corresponding country, continent, region and district information.

The system can connect to the database.

### Success End Condition

The system generates and displays the requested report with the columns: **Name, Country, District, Population**.

Cities are ordered by population from largest to smallest. For a top N report, the system displays up to N cities.

### Failed End Condition

No report is produced because no matching cities are found or the system cannot retrieve the data.

### Primary Actor

Data Analyst.

### Trigger

The Data Analyst requests a city population report to support planning or analysis.

## MAIN SUCCESS SCENARIO

1. The Data Analyst requests a city population report.
2. The system provides the ten report options.
3. The Data Analyst selects the required report.
4. For a continent, region, country or district report, the Data Analyst provides the corresponding geographic parameter.
5. For a top N report, the Data Analyst provides N as a positive whole number.
6. The system validates the required parameters.
7. The system retrieves the matching cities from the database and orders them by population from largest to smallest.
8. For a top N report, the system limits the results to N cities.
9. The system displays the report with **Name, Country, District and Population**.

## EXTENSIONS

**4a. Geographic parameter is missing or invalid:**  
The system requests a valid continent, region, country or district, then continues validation.

**5a. N is missing or invalid:**  
The system requests a positive whole number, then continues validation.

**7a. No matching cities are found:**  
The system notifies the Data Analyst that no cities were found.

**7b. Database connection or retrieval fails:**  
The system displays an error message and does not produce a report.

**8a. N exceeds the number of matching cities:**  
The system displays all available matching cities.

## SUB-VARIATIONS

1. All cities in the world by population.
2. All cities in a selected continent by population.
3. All cities in a selected region by population.
4. All cities in a selected country by population.
5. All cities in a selected district by population.
6. Top N populated cities in the world.
7. Top N populated cities in a selected continent.
8. Top N populated cities in a selected region.
9. Top N populated cities in a selected country.
10. Top N populated cities in a selected district.

## SCHEDULE

**Due Date:** Release `v1.0.1`