/**
 * 
 */
package org.springframework.cloud.servicebroker.hawqdb.repository;

import org.springframework.cloud.servicebroker.hawqdb.model.ServiceInstanceData;
import org.springframework.data.repository.CrudRepository;

/**
 * @author shuklk2
 *
 */
public interface HawqDBRepository extends CrudRepository<ServiceInstanceData, String>{

}
