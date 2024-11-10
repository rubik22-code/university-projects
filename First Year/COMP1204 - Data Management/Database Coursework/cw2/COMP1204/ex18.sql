SELECT
    country.countriesAndTerritories,
    ROUND((ROUND(SUM(cases.deaths), 2) / SUM(cases.cases)) * 100, 2) AS percentage_of_death
FROM cases
INNER JOIN country ON cases.geoId = country.geoId
GROUP BY country.countriesAndTerritories
ORDER BY percentage_of_death DESC
LIMIT 10;
