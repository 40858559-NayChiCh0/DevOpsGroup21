Use Case: Generate Capital City Reports
CHARACTERISTIC INFORMATION
Goal in Context
As a Data Analyst, I want to view all capital cities in the world ordered by population from largest to smallest, including their names, countries and populations, so that I can analyse global capital city populations.
As a Data Analyst, I want to view all capital cities in a selected continent ordered by population from largest to smallest, so that I can compare capital city populations within that continent.
As a Data Analyst, I want to view all capital cities in a selected region ordered by population from largest to smallest, so that I can analyse capital city populations regionally.
As a Data Analyst, I want to view the top N populated capital cities in the world, where N is provided, so that I can identify the world's largest capital cities.
As a Data Analyst, I want to view the top N populated capital cities in a selected continent, where N is provided, so that I can analyse the biggest capitals in that continent.
As a Data Analyst, I want to view the top N populated capital cities in a selected region, where N is provided, so that I can identify the largest capitals in that region.

Scope
Population Reporting System, covering worldwide, continent-level and region-level capital city reports, including all-capital and Top N rankings.

Level
Primary task

Preconditions
The system is running, and the MySQL database connection is available.
The city and country tables contain capital city names, country names, population data and geographic classifications.
A valid database Connection object is passed to the CapitalCityReports constructor.
For continent and region reports, the Data Analyst can provide the required continent or region.
For Top N reports, the Data Analyst can provide a valid positive integer for N.

Success Condition
The system displays the requested capital city report with the columns Name, Country, Population, ordered by population from largest to smallest.
1.	All capital cities worldwide.
2.	All capital cities in a selected continent.
3.	All capital cities in a selected region.
4.	The top N populated capital cities worldwide.
5.	The top N populated capital cities in a selected continent.
6.	The top N populated capital cities in a selected region.
      For filtered reports, only matching capital cities are displayed. Top N reports display no more than the requested number of capital cities.

Failed Condition
The report cannot be generated because of database connection errors, SQL execution errors, invalid input or unavailable matching capital city records. An appropriate error or information message is displayed where handled by the application.

Primary Actor
Data Analyst
Trigger
The Data Analyst requests a capital city population report through the Population Reporting System.

MAIN SUCCESS SCENARIO
1.	The Data Analyst selects one of the six capital city reports.
2.	The system identifies the report type and requests the continent, region or N value where required.
3.	The Data Analyst provides the required input.
4.	The system executes the corresponding SQL query against the MySQL database.
5.	The system retrieves capital city names, country names and population values.
6.	The system sorts the results by population from largest to smallest and limits the results to N for Top N reports.
7.	The system displays the report, and the Data Analyst reviews the results.

EXTENSIONS
Invalid or missing continent or region — The system displays an appropriate error or no-matching-records message.
Invalid N value — The system requests a valid positive integer or displays an error message, if implemented.
SQL execution or database connection fails — The report cannot be generated, and the calling application handles the error.
No matching capital cities are found — The system displays the message "No capital cities found."

SUB-VARIATIONS
The Data Analyst requests all capital cities worldwide.
The Data Analyst selects a continent to view all capital cities in that continent.
The Data Analyst selects a region to view all capital cities in that region.
The Data Analyst requests the top N capital cities worldwide.
The Data Analyst requests the top N capital cities in a selected continent.
The Data Analyst requests the top N capital cities in a selected region.

SCHEDULE
Due date: Release v1.0.1s
