/**
 * 
 */
package org.springframework.cloud.servicebroker.hawqdb.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author shuklk2
 *
 */

@Entity
@Table(schema="hawqdbschema", name="service")
public class ServiceInstanceData implements Serializable{

	private static final long serialVersionUID = 1L;

	private String dbname;
	
	@Id
	private String serviceinstanceid;
	
	private String servicedefinitionid;
	private String planid;
	private String organizationguid;
	private String spaceguid;
	
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getServiceinstanceid() {
		return serviceinstanceid;
	}
	public void setServiceinstanceid(String serviceinstanceid) {
		this.serviceinstanceid = serviceinstanceid;
	}
	public String getServicedefinitionid() {
		return servicedefinitionid;
	}
	public void setServicedefinitionid(String servicedefinitionid) {
		this.servicedefinitionid = servicedefinitionid;
	}
	public String getPlanid() {
		return planid;
	}
	public void setPlanid(String planid) {
		this.planid = planid;
	}
	public String getOrganizationguid() {
		return organizationguid;
	}
	public void setOrganizationguid(String organizationguid) {
		this.organizationguid = organizationguid;
	}
	public String getSpaceguid() {
		return spaceguid;
	}
	public void setSpaceguid(String spaceguid) {
		this.spaceguid = spaceguid;
	}
	
	
	
	
	
}
