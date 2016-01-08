package org.springframework.cloud.servicebroker.hawqdb.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.servicebroker.hawqdb.repository.HawqDBRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shuklk2
 *
 */

@Component
public class Role {

	public void createRoleForInstance(String instanceId, String rolename, JdbcTemplate jdbctemplate) throws SQLException {
        jdbctemplate.update("CREATE ROLE " + rolename);
    }

    public void deleteRole(String instanceId, String rolename, JdbcTemplate jdbctemplate) throws SQLException {
        jdbctemplate.update("DROP ROLE IF EXISTS " + rolename);
    }
    

    public List<String> bindRoleToDatabase(String dbInstanceId, HawqDBRepository repository, JdbcTemplate jdbctemplate) throws SQLException {
        Map<Integer, String> parameterMap = new HashMap<Integer, String>();
        parameterMap.put(1, dbInstanceId);
    	
        String dbname = repository.findOne(dbInstanceId).getDbname();
    	
        SecureRandom random = new SecureRandom();
        String passwd = new BigInteger(130, random).toString(32);
       
        jdbctemplate.update("GRANT ALL ON DATABASE "+ dbname + " TO " + dbname);
        jdbctemplate.update("ALTER ROLE " + dbname + " LOGIN");
        jdbctemplate.update("ALTER ROLE " + dbname + " WITH PASSWORD '"+passwd+"'");
        
        List<String> list = new ArrayList<String>();
        list.add(dbname);
        list.add(passwd);
        
        return list;
    }

    public void unBindRoleFromDatabase(String dbInstanceId, HawqDBRepository repository, JdbcTemplate jdbctemplate) throws SQLException{
        Map<Integer, String> parameterMap = new HashMap<Integer, String>();
        parameterMap.put(1, dbInstanceId);
        
        String dbname = repository.findOne(dbInstanceId).getDbname();
        
        jdbctemplate.update("ALTER ROLE " + dbname + " NOLOGIN");
        jdbctemplate.update("REVOKE ALL ON DATABASE " + dbname + " FROM " + dbname);    
    }
}
