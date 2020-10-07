package ${package};

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configure() throws Exception {
        super.configure();

        // Stubs for API end point implementation. Replace setBody with your code.
        [generated-routes]

        //  Used to add the currentRoute property.
        from("direct:util:setCurrentRouteInfo")
        	.setProperty("currentRoute", simple("${headers.CamelHttpMethod} ${headers.CamelHttpUri}"))
        ;
    }
}
