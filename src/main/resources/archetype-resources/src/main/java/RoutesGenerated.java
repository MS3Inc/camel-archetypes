package ${package};

import javax.annotation.Generated;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * This is a placeholder for a class file generated from an OpenApi specification by the camel-oas-archetype.
 * This file should NOT copied over into the archetype.  Rather, it serves as an example of what will be 
 * generated in the post processing step of the archetype.
 * 
 * @author Mark Norton (mnorton@ms3-inc.com)
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
