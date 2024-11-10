SELECT cases.dateRep,
    SUM(cases.cases) OVER (ROWS UNBOUNDED PRECEDING),
	SUM(cases.deaths) OVER (ROWS UNBOUNDED PRECEDING)
FROM cases
INNER JOIN date ON date.dateRep = cases.dateRep
INNER JOIN country ON country.geoId = cases.geoId
WHERE cases.geoId = 'UK'
ORDER BY year,month,day;