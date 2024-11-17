package com.chadicloud.ai.llmspring.config;

//import com.datastax.astra.client.DataAPIClient;
//import com.datastax.astra.client.Database;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.temporal.ChronoUnit;
@Configuration
public class AstraConfig {

    private static final Logger log = LoggerFactory.getLogger(AstraConfig.class);
    @Value("${astra.api.application-token}")
    private String astraToken;
    @Value("${astra.database.url}")
    private String astraDbUrl;
    @Value("${astra.database.keyspace}")
    private String keyspace;

//    @Bean
//    public Database astraDb() {
//        DataAPIClient client = new DataAPIClient(astraToken);
//        log.info("Connected to AstraDB");
//        Database db = client.getDatabase(astraDbUrl, keyspace);
//        log.info("Connected to Database.");
//        return db;
//    }
}
