
# Use Case: Generate Language Speakers and Top N City Reports

## CHARACTERISTIC INFORMATION

### Goal in Context
As a Data Analyst, I want to view the number of people who speak Chinese, English, Hindi, Spanish, and Arabic, sorted from greatest to smallest, including their percentage of the world population, so that I can compare language populations globally.

### Scope
Population Reporting System, covering language speaker statistics.

### Level
Primary Task

### Preconditions
- The system is running and the MySQL database connection is available.
- The `country` and `countrylanguage` tables contain the required country population and language percentage data.
- The `city` and `country` tables contain the required city names, populations, country details, and district information.

### Success Condition
The system successfully generates the requested report.

1. **Language Speakers Report:** Displays Chinese, English, Hindi, Spanish, and Arabic, their estimated total numbers of speakers, and their percentages of the world population, sorted by number of speakers from greatest to smallest.

### Failed Condition
The requested report cannot be generated because of a database connection failure, SQL execution error, invalid input, or unavailable matching data. The system displays an appropriate error or information message where implemented.

### Primary Actor
Data Analyst

### Trigger
The Population Reporting System starts and automatically executes the Language Speakers Report method, or the Data Analyst requests one of the Top N city reports.

---

## MAIN SUCCESS SCENARIO

1. The system retrieves the required language and population data, calculates speaker totals and percentages, and displays the languages in descending order.

---

## EXTENSIONS

- **1a.** Database connection or SQL error — The system displays an error message.
- **1b.** Required data is unavailable — The system displays an appropriate message.

---

## SUB-VARIATIONS

- The Language Speakers Report is displayed in the console rather than through a graphical user interface.

---

## SCHEDULE

**Due Date:** Release `v1.0.1`
