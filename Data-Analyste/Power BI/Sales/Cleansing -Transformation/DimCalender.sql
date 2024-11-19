-- cleansed DimDate --
SELECT 
  [DateKey], 
  [FullDateAlternateKey] AS Date, 
  [EnglishDayNameOfWeek] AS Day, 
  [WeekNumberOfYear] AS WeekNr, 
  [EnglishMonthName] AS Month, 
  LEFT([EnglishMonthName], 3) AS MonthShort, 
  [MonthNumberOfYear] AS MonthNr, 
  [CalendarQuarter] AS Quater, 
  [CalendarYear] AS Year 
FROM 
  [AdventureWorksDW2022].[dbo].[DimDate] 
where 
  CalendarYear >= 2012