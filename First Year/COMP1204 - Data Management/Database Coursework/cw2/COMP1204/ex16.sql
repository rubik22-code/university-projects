SELECT cases.geoId, cases.dateRep, cases.cases, cases.deaths
FROM cases
INNER JOIN country on cases.geoId = country.geoId