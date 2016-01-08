/**
 * 
 */
package org.springframework.cloud.servicebroker.hawqdb.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.servicebroker.hawqdb.model.ServiceInstanceData;
import org.springframework.cloud.servicebroker.hawqdb.repository.HawqDBRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shuklk2
 *
 */
@Component
public class Database {

	 public String createDatabaseForInstance(String instanceId, String serviceId, String planId, String organizationGuid, String spaceGuid, HawqDBRepository repository, JdbcTemplate jdbctemplate) throws SQLException {
	        
		 	String dbname = "hawqdb";
		 	//find out count in service table
		 	long count = repository.count();
		 	dbname = dbname + String.valueOf(count+1);
		 	
		 	jdbctemplate.update("CREATE DATABASE " + dbname + " ENCODING 'UTF8'");
	        System.out.println("database creation successful");
	        
	        jdbctemplate.update("REVOKE all on database " + dbname + " from public");
	        System.out.println("roles execution successful");

	        // save this information in service broker's database (which in this case is the HAWQ database
	        // could be any other database)
	        Map<Integer, String> parameterMap = new HashMap<Integer, String>();
	        parameterMap.put(1, dbname);
	        parameterMap.put(2, instanceId);
	        parameterMap.put(3, serviceId);
	        parameterMap.put(4, planId);
	        parameterMap.put(5, organizationGuid);
	        parameterMap.put(6, spaceGuid);

	        ServiceInstanceData sid = new ServiceInstanceData();
	        sid.setDbname(dbname);
	        sid.setOrganizationguid(organizationGuid);
	        sid.setPlanid(planId);
	        sid.setServicedefinitionid(serviceId);
	        sid.setServiceinstanceid(instanceId);
	        sid.setSpaceguid(spaceGuid);
	        repository.save(sid);
	        
	        System.out.println("update in service table completed");
	        
	        return dbname;
	    }

	    public String deleteDatabase(String instanceId, HawqDBRepository repository, JdbcTemplate jdbctemplate) throws SQLException {
	        
	    	Map<Integer, String> parameterMap = new HashMap<Integer, String>();
	        parameterMap.put(1, instanceId);

	        /*
	        if somebody is connected to database, then you cannot delete a hawq database
	        ignoring this code for a moment
	        
	        Map<String, String> result = PostgreSQLDatabase.executeSelect("SELECT current_user");
	        String currentUser = null;

	        if(result != null) {
	            currentUser = result.get("current_user");
	        }

	        if(currentUser == null) {
	            logger.error("Current user for instance '" + instanceId + "' could not be found");
	        }

	        PostgreSQLDatabase.executePreparedSelect("SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = ? AND pid <> pg_backend_pid()", parameterMap);
	         */
	       
	        
	        String dbname = repository.findOne(instanceId).getDbname();
	        
		 	jdbctemplate.update("DROP DATABASE IF EXISTS "+ dbname);
	        
	        // this will not work for HAWQ database as delete is not supported.
	        // workaround is to let the entry be there (or use relational database)
	        // PostgreSQLDatabase.executePreparedUpdate("DELETE FROM loc_marketing.service WHERE serviceinstanceid=?", parameterMap);
		 	return dbname;
	    }
	    
	    
}
