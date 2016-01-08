package org.springframework.cloud.servicebroker.hawqdb.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {
	
	@Bean
	public Catalog catalog() {
		return new Catalog(Collections.singletonList(
				new ServiceDefinition(
						"hawq_db", //id
						"hawqdatabase",  //name
						"HAWQ Database for dev use", //desc
						true, //is_bindable
						false, 
						getPlans(), //plans
						getTags(), //tags
						getServiceDefinitionMetadata(),
						null, 
						null))); 

	}
	
    /* Used by Pivotal CF console */

	   private static List<String> getTags() {
	        return Arrays.asList("HAWQ Database", "PostgreSQL", "Database on Hadoop");
	    }

	    private static Map<String, Object> getServiceDefinitionMetadata() {
	        Map<String, Object> sdMetadata = new HashMap<String, Object>();
	        sdMetadata.put("displayName", "HAWQDatabase");
	        sdMetadata.put("imageUrl", "http://hawq.incubator.apache.org/images/logo-hawq.png");
	        sdMetadata.put("longDescription", "HAWQ Database on Hadoop");
	        sdMetadata.put("providerDisplayName", "Pivotal");
	        sdMetadata.put("documentationUrl", "http://hawq.docs.pivotal.io/index.html");
	        sdMetadata.put("supportUrl", "http://hawq.docs.pivotal.io/index.html");
	        return sdMetadata;
	    }

	    private static List<Plan> getPlans() {
	        Plan basic = new Plan("hawq-basic-plan", "basic",
	                "Multitenant HAWQ DB-as-a-Service", getBasicPlanMetadata(), true);
	        
	        /*
	        Plan adv = new Plan("hawq-adv-plan", "advanced",
	                "Plan providing HAWQ database with 100GB storage.", getAdvPlanMetadata());
	        
	        */
	        
	        // return Arrays.asList(basic, adv);
	        return Arrays.asList(basic);
	    }

	    private static Map<String, Object> getBasicPlanMetadata() {
	        Map<String, Object> planMetadata = new HashMap<String, Object>();
	        planMetadata.put("bullets", getBasicPlanBullets());
	        return planMetadata;
	    }

	    private static List<String> getBasicPlanBullets() {
	        return Arrays.asList("HAWQ database", "Limited storage", "Shared instance", "Development use");
	    }
	    
	    /*
	    private static Map<String, Object> getAdvPlanMetadata() {
	        Map<String, Object> planMetadata = new HashMap<String, Object>();
	        planMetadata.put("bullets", getAdvPlanBullets());
	        return planMetadata;
	    }
	    

	    private static List<String> getAdvPlanBullets() {
	        return Arrays.asList("HAWQ database", "Limited storage", "Shared instance", "Testing use");
	    }
	    */
}