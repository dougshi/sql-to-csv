# SQL to CSV App
A Spring Boot Application for converting sql to csv file 
- Apache Camel
  - camel-jdbc
    the sql support named parameters
  - camel-csv
- Spring MVC

### Start the app:
The app creates a H2 database with projects table

```bash 
mvn spring-boot:run
``` 

### Test the app:

test.json template
```json
{
	"sql": "select name, description from projects where name = :?name",
	"sqlParams": {"name": "sql-to-csv"},
	"csvDelimiter": ";"
}
```

```bash
# test the sql
curl -XPOST -H "Content-Type: application/json" -d @test.json http://localhost:8080/sql2csv/

# result
NAME;DESCRIPTION
sql-to-csv;app to convert sql output into csv format

```

### Comparison camel vs python
Comparing following two solutions the python looks more concise and straight forward. 

* camel java solution
  the workflow in Camel Java DSL: 
  ```java
  from("direct:sql-to-csv").routeId("sql-to-csv").to("jdbc:dataSource?useHeadersAsParameters=true")
					.multicast(new StringConcatStrategy())
						.setBody(groovy("request.headers.CamelJdbcColumnNames.join(request.headers.csvDelimiter) + \"\\n\""))
						.toD("dataformat:csv:marshal?delimiter=${header.csvDelimiter}")
					.end()
					.log("\n${body}\n");
  ```

* [python](https://code.adonline.id.au/sql-to-csv-via-python/) 
  ```python
    connection = sqlite3.connect('/path/to/database.sql')
    cursor = connection.cursor()
    # Execute the query
    cursor.execute('select * from mydata')
    # Get Header Names (without tuples)
    colnames = [desc[0] for desc in cursor.description]
    # Get data in batches
    while True:
        # Read the data
        df = pd.DataFrame(cursor.fetchall())
        # We are done if there are no data
        if len(df) == 0:
            break
        # Let us write to the file
        else:
            df.to_csv(f, header=colnames)

    # Clean up
    ...
  ```




### References
* [test.json](test.json)
* [projects records](src/main/resources/data.sql)

