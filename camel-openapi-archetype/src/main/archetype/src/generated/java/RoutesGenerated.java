package ${package};

import javax.annotation.Generated;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ms3_inc.camel.extensions.rest.OpenApi4jValidator;
import org.apache.camel.LoggingLevel;

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
     *
     * Make changes to this file with caution.
     * If the API specification changes and this file is regenerated,
     * previous changes may be overwritten.
     */
    @Override
    public void configure() throws Exception {
        super.configure();

        restConfiguration().component("undertow");

[generated-restdsl]

        from(direct("logger-helper-start"))
                .log(LoggingLevel.INFO,  createLog("start"))
        ;

        from(direct("logger-helper-end"))
                .log(LoggingLevel.INFO,  createLog("end"))
        ;

    }

    String createLog(String startOrEnd) {
        return "[system = ApacheCamel " + contextPath + "]" +
                "[route_name = ${exchangeProperty.origRouteId}]" +
                "[message = " + startOrEnd +" of ${exchangeProperty.origRouteId}]";
    }
}
