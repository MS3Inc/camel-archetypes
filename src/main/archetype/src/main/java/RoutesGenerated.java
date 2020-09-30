package ${package};

import javax.annotation.Generated;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Generated routes are based on the OpenAPI document in src/main/resources/api folder.
 * 
 * @author Maven Archetype (camel-oas-archetype)
 */
@Generated("com.ms3inc.camel-oas-archetype")
@Component
public class RoutesGenerated extends RouteBuilder {

    /**
     * Defines Apache Camel routes using the OpenAPI REST DSL.
     * Routes are built using a get(PATH) rest message processor.
     * Each defined route is directed to an implementation end point currently based on an operation ID in the OAS document.
     * Later, these will be auto-generated.
     */
    public void configure() {

INSERT-CODE-HERE
    }

}
