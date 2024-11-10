SELECT cases.dateRep, cases.cases
FROM cases
INNER JOIN country ON cases.geoId = country.geoId
WHERE country.geoId = 'UK'
ORDER BY DATE(cases.dateRep) ASC;