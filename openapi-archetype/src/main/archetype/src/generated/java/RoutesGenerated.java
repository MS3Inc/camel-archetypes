package ${package};

import javax.annotation.Generated;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ms3_inc.camel.extensions.rest.OpenApi4jValidator;

/**
 * Generated routes are based on the OpenAPI document in src/generated/api folder.
 *
 * @author Maven Archetype (camel-oas-archetype)
 */
@Generated("com.ms3_inc.camel.archetype.oas")
@Component
public class RoutesGenerated extends BaseRestRouteBuilder{
    private final String contextPath;

    public RoutesGenerated(@Value("${camel.rest.context-path}") String contextPath) {
        super();
        this.contextPath = contextPath;
    }

    /**
     * Defines Apache Camel routes using the OpenAPI REST DSL.
     * Routes are built using a get(PATH) rest message processor.
     * Each defined route is directed to an implementation end point currently based on an operation ID in the OAS document.
     * Later, these will be auto-generated.
     */
    @Override
    public void configure() throws Exception {
        super.configure();

        restConfiguration().component("undertow");

[generated-restdsl]

    }
}
