package demo.transform.sqltocsv;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@SpringBootApplication
@RestController
public class SqlToCsvApplication {

	@Autowired
	@EndpointInject("direct:sql-to-csv")
	ProducerTemplate producerTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SqlToCsvApplication.class, args);
	}

	@PostMapping(path= "", consumes = "application/json", produces = "text/plain")
	public String convertSqlToCsv(@RequestBody SQL2CSVRequest request) throws Exception {
		Assert.notNull(request, "Accept json for sql, sqlParams, csvDelimiter");
		
		Map<String, Object> headers = new HashMap<>();
		headers.put("csvDelimiter", StringUtils.isEmpty(request.getCsvDelimiter())? ",": request.getCsvDelimiter());
		if(!CollectionUtils.isEmpty(request.getSqlParams())) {
			headers.putAll(request.getSqlParams());
		}
		
		return (String)producerTemplate.requestBodyAndHeaders(request.getSql(), headers);
	}
}

@Data
class SQL2CSVRequest{
	private String sql;
	private Map<String, Object> sqlParams;
	private String csvDelimiter;
}

