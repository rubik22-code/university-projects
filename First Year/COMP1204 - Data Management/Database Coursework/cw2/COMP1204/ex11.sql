CREATE TABLE date (
    dateRep TEXT NOT NULL,
    day INTEGER,
    month INTEGER,
    year INTEGER,
    PRIMARY KEY (dateRep)
);

CREATE TABLE cases (
    dateRep TEXT NOT NULL,
    geoId TEXT NOT NULL,
    cases INTEGER,
    deaths INTEGER,
    PRIMARY KEY (dateRep, geoId),

    FOREIGN KEY (dateRep)
       REFERENCES date (dateRep)
);

CREATE TABLE country (
    geoId TEXT NOT NULL,
    countriesAndTerritories TEXT NOT NULL,
    continentExp TEXT NOT NULL,
    countryterritoryCode TEXT NOT NULL,
    popData2020 INTEGER,
    PRIMARY KEY (geoId, countriesAndTerritories, continentExp, countryterritoryCode),

    FOREIGN KEY (geoId)
       REFERENCES country (geoId)
);


