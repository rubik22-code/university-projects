SELECT country.countriesAndTerritories,
       ROUND(ROUND(SUM(cases.cases) * 100, 2) / country.popData2020, 2),
       ROUND(ROUND(SUM(cases.deaths) * 100, 2) / country.popData2020, 2)
FROM cases
INNER JOIN country on cases.geoId = country.geoId
GROUP BY country.geoId