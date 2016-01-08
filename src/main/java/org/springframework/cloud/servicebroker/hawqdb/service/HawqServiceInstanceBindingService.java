package org.springframework.cloud.servicebroker.hawqdb.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.hawqdb.exception.HawqServiceException;
import org.springframework.cloud.servicebroker.hawqdb.repository.HawqDBRepository;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shuklk2
 *
 */

@Service
public class HawqServiceInstanceBindingService implements ServiceInstanceBindingService {
	
	private Role role;
	private HawqDBRepository repository;
	private JdbcTemplate jdbctemplate;

	@Autowired
	public HawqServiceInstanceBindingService(Role role, HawqDBRepository repository, JdbcTemplate jdbctemplate) {
		this.role = role;
		this.jdbctemplate = jdbctemplate;
		this.repository = repository;
	}
	
	
	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest bindingRequest)
			throws ServiceInstanceBindingExistsException,
			ServiceBrokerException {
		
		String serviceInstanceId = bindingRequest.getServiceInstanceId();
		
        List<String> list;
        
        try {
            list = this.role.bindRoleToDatabase(serviceInstanceId, repository, jdbctemplate);
        } catch (SQLException e) {
            throw new HawqServiceException(e.getMessage());
        }

        Connection conn;
        String jdbcUrl;
        try {
        	conn = jdbctemplate.getDataSource().getConnection();
        	jdbcUrl = conn.getMetaData().getURL();
        } catch (SQLException e) {
            throw new HawqServiceException(e.getMessage());
        }
    	
        // Remove "jdbc:" prefix from the connection JDBC URL to create an URI out of it.
        String cleanJdbcUrl = jdbcUrl.replace("jdbc:", "");

        URI uri;
        try {
        	uri = new URI(cleanJdbcUrl);
        } catch (URISyntaxException e) {
        	throw new ServiceBrokerException(e.getMessage());
        }
        
        String dbURL = String.format("postgres://%s:%s@%s:%d/%s", list.get(0), list.get(1), uri.getHost(), uri.getPort(), list.get(0));

        Map<String, Object> credentials = new HashMap<String, Object>();
        credentials.put("uri", dbURL);

        return new CreateServiceInstanceBindingResponse(credentials);
	}
 
	@Override
	public void deleteServiceInstanceBinding(
			DeleteServiceInstanceBindingRequest bindingRequest)
			throws ServiceBrokerException {
		try {
            this.role.unBindRoleFromDatabase(bindingRequest.getServiceInstanceId(), repository, jdbctemplate);
        } catch (SQLException e) {
            throw new HawqServiceException(e.getMessage());
        }
	}

}
