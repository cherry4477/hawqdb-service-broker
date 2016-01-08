package org.springframework.cloud.servicebroker.hawqdb.service;

import java.sql.SQLException;

import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.hawqdb.exception.HawqServiceException;
import org.springframework.cloud.servicebroker.hawqdb.repository.HawqDBRepository;
import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationRequest;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationResponse;
import org.springframework.cloud.servicebroker.model.OperationState;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shuklk2
 *
 */

@Service
public class HawqServiceInstanceService implements ServiceInstanceService {

	private Database db;
	private Role role;
	private HawqDBRepository repository;
	private JdbcTemplate jdbctemplate;
	
	@Autowired
	public HawqServiceInstanceService(Database db, Role role, HawqDBRepository repository, JdbcTemplate jdbctemplate) {
		this.db = db;
		this.role = role;
		this.repository = repository;
		this.jdbctemplate = jdbctemplate;
	}
	
	
   @Override
	public CreateServiceInstanceResponse createServiceInstance(
			CreateServiceInstanceRequest serviceInstReq)
			throws ServiceInstanceExistsException, ServiceBrokerException {
		String serviceDefId = serviceInstReq.getServiceDefinitionId();
		String serviceInstId = serviceInstReq.getServiceInstanceId();
		String planId = serviceInstReq.getPlanId();
		String organizationGuid = serviceInstReq.getOrganizationGuid();
		String spaceGuid = serviceInstReq.getSpaceGuid();
		try {
        	System.out.println("serviceinstanceid: "+serviceInstId);
        	System.out.println("planId: "+planId);
        	
        	
            String dbname = db.createDatabaseForInstance(serviceInstId, serviceDefId, planId, organizationGuid, spaceGuid, repository, jdbctemplate);
            System.out.println("Done with database creation");
            
            role.createRoleForInstance(serviceInstId, dbname, jdbctemplate);
        } catch (SQLException e) {
        	throw new HawqServiceException(e.getMessage());
        } 
		return new CreateServiceInstanceResponse(false);
	}
    
    
	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(
			DeleteServiceInstanceRequest delServiceInstReq) throws ServiceBrokerException {
		
		String serviceInstId = delServiceInstReq.getServiceInstanceId();
        try {
            String dbname = db.deleteDatabase(serviceInstId, repository, jdbctemplate);
            role.deleteRole(serviceInstId, dbname, jdbctemplate);
        } catch (SQLException e) {
            throw new HawqServiceException(e.getMessage());
        }
        return new DeleteServiceInstanceResponse();
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(
			UpdateServiceInstanceRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
		return new GetLastServiceOperationResponse(OperationState.SUCCEEDED);
	}

	@Bean
	public BrokerApiVersion brokerApiVersion() {
	    return new BrokerApiVersion();
	}
}