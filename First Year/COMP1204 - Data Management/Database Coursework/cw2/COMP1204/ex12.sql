INSERT OR IGNORE INTO date (dateRep, day, month, year)
SELECT DISTINCT dateRep, day, month, year FROM dataset;

INSERT OR IGNORE INTO cases (dateRep, geoId, cases, deaths)
SELECT DISTINCT dateRep, geoId, cases, deaths from dataset;

INSERT OR IGNORE INTO country (geoId, countriesAndTerritories, continentExp, countryterritoryCode, popData2020)
SELECT DISTINCT geoId, countriesAndTerritories, continentExp, countryterritoryCode, popData2020 from dataset;
