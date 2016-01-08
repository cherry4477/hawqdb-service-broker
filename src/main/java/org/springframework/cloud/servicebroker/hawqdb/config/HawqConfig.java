package org.springframework.cloud.servicebroker.hawqdb.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HawqConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(HawqConfig.class);
	
    @Value("${MASTER_JDBC_URL}")
    private String jdbcUrl;

    @Bean
    public Connection jdbc() {
        try {
            Connection conn = DriverManager.getConnection(this.jdbcUrl);
            
            String schema = "CREATE SCHEMA hawqdbschema";
            Statement createSchema = conn.createStatement();
            createSchema.execute(schema);
            
            String serviceTable = "CREATE TABLE hawqdbschema.service (dbname character varying(200), " +
            						"serviceinstanceid character varying(200), " +
            						"servicedefinitionid character varying(200), " +
            						"planid character varying(200), " + 
            						"organizationguid character varying(200), " +
            						"spaceguid character varying(200))";
            
            Statement createServiceTable = conn.createStatement();
            createServiceTable.execute(serviceTable);
            System.out.println("Successfully created services table");
            return conn;
        } catch (SQLException e) {
            logger.error("Error while creating initial 'service' table", e);
            return null;
        }
    }
	
}
