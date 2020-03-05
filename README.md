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

### References
* [test.json](test.json)
* [projects records](src/main/resources/data.sql)

