package ${package};

import com.datasonnet.document.MediaTypes;
import org.apache.camel.LoggingLevel;
import org.springframework.stereotype.Component;

/**
 * The RoutesImplementation class holds implementations for the end points configured in RoutesGenerated.
 * These routes are based on operation Ids, that correspond to an API end point:  method+path.
 * 
 * @author Maven Archetype (camel-openapi-archetype)
 *
 */
@Component
public class RoutesImplementation extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        // TODO: Replace stubs for each endpoint with real implementation.  Implementation defaults to a simple response with operation Id.
[generated-routes]

        from(direct("logger-helper"))
            .log(LoggingLevel.INFO,"[system = ApacheCamel {{camel.rest.context-path}}]" +
            "[route_name = ${exchangeProperty.origRouteId}]" +
            "[message = ${exchangeProperty.startOrEnd} of ${exchangeProperty.origRouteId}]")
        ;

    }
}
