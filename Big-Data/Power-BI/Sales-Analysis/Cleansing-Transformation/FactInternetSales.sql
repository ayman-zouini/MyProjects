-- cleansed FactInternetSales --
SELECT
	   [ProductKey]
      ,[OrderDateKey]
      ,[DueDateKey]
      ,[ShipDateKey]
      ,[CustomerKey]
      ,[SalesOrderNumber]
      ,[SalesAmount]
  FROM [AdventureWorksDW2022].[dbo].[FactInternetSales]
  WHERE LEFT (OrderDateKey,4) >= year(getdate()) -12
  ORDER BY
  OrderDateKey asc
