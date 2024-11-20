-- clensed DimCustomer --
SELECT c.CustomerKey AS CustomerKey
      ,c.FirstName AS [First Name]
      ,c.LastName AS [Last Name]
	  ,c.FirstName + ' ' + c.LastName AS [Full Name]
      ,CASE c.Gender WHEN 'M' THEN 'Male' WHEN 'F' THEN 'Female' END AS Gender
      ,c.DateFirstPurchase AS [Date First Purchase]
	  ,g.City AS [Cutomer City]
  FROM dbo.DimCustomer AS c
  LEFT JOIN dbo.DimGeography AS g ON c.GeographyKey = g.GeographyKey
  ORDER BY CustomerKey