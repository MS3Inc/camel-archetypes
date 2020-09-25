package ${package};

import org.apache.camel.Exchange;
//import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
//import com.datasonnet.document.MediaTypes;

/**
 * The RoutesImplementation class holds implementations for the end points configured in RoutesGenerated.
 * These routes are based on operation Ids, that correspond to an API end point:  method+path.
 * 
 * @author Maven Archetype (camel-oas-archetype)
 *
 */
@Component
public class RoutesImplementation extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configure() throws Exception {

    	//  Used to load a Data Sonnet file.
        //final String DSPATH = "resource:classpath:datasonnet/";

        //  Error handler for all exceptions thrown by the Camel routes below.
        onException(Exception.class)
                .process(exchange -> {
                    final String EXCEPTION = exchange.getProperty("CamelExceptionCaught").toString();
                    if (EXCEPTION.equals("java.lang.Exception: Not Found")) {
                        exchange.getIn().setBody("{\"message\":\"Account Not Found\"}");
                        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "404");
                    } else {
                        LOGGER.debug(EXCEPTION);

                        exchange.getIn().setBody("{\"message\":\"Bad Request\"}");
                        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "400");
                    }

                })
                .routeId("exceptionHandler")
                .handled(true)
        ;

        // Stubs for API end point implementation.  Replace setBody with your code.
INSERT-CODE-HERE

        //  Used to add the currentRoute property.
        from("direct:util:setCurrentRouteInfo")
        	.setProperty("currentRoute", simple("${headers.CamelHttpMethod} ${headers.CamelHttpUri}"))
        ;
    }
}
