package ${package};

import org.springframework.stereotype.Component;

/**
 * The RoutesImplementation class holds implementations for the end points configured in RoutesGenerated.
 * These routes are based on operation Ids, that correspond to an API end point:  method+path.
 * 
 * @author Maven Archetype (camel-oas-archetype)
 *
 */
@Component
public class RoutesImplementation extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        // TODO: Replace stubs for each endpoint with real implementation.  Implementation defaults to a simple response with operation Id.
[generated-routes]

        //  Used to add the currentRoute property.
        from("direct:util:setCurrentRouteInfo")
        	.setProperty("currentRoute", simple("${headers.CamelHttpMethod} ${headers.CamelHttpUri}"))
        ;
    }
}
