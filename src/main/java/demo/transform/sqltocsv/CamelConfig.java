package demo.transform.sqltocsv;

import static org.apache.camel.language.groovy.GroovyLanguage.groovy;

import javax.sql.DataSource;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

	@Bean
	protected RouteBuilder sqlToCsvRoute(@Autowired DataSource dataSource) throws Exception {
		/**
		 * entrypoint: direct:sql-to-csv body: sql headers: sql parameters
		 * header.csv.delimitor
		 */
		return new RouteBuilder() {

			@Override
			public void configure() {
				
				
				// @formatter:off

				from("direct:sql-to-csv").routeId("sql-to-csv").to("jdbc:dataSource?useHeadersAsParameters=true")
					.multicast(new StringConcatStrategy())
						.setBody(groovy("request.headers.CamelJdbcColumnNames.join(request.headers.csvDelimiter) + \"\\n\""))
						.toD("dataformat:csv:marshal?delimiter=${header.csvDelimiter}")
					.end()
					.log("\n${body}\n");
					
				// @formatter:on
			}
		};
	}
	
	
	class StringConcatStrategy implements AggregationStrategy {
		
		public org.apache.camel.Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
			if (oldExchange == null)
				return newExchange;
			else {
				String oldBody = oldExchange.getIn().getBody(String.class);
				String newBody = newExchange.getIn().getBody(String.class);
				oldExchange.getIn().setBody(oldBody + newBody);
				return oldExchange;
			}
		}
	}
}


