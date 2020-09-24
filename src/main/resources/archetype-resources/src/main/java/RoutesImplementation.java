package ${pacakge};

import org.apache.camel.Exchange;
//import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.datasonnet.document.MediaTypes;

/**
 * This is a placeholder for a class file generated from an OpenApi specification by the camel-oas-archetype.
 * This file should NOT copied over into the archetype.  Rather, it serves as an example of what will be 
 * generated in the post processing step of the archetype.
 * 
 * The RoutesImplementation class holds implementations for the end points configured in RoutesGenerated.
 * 
 * @author Mark Norton (mnorton@ms3-inc.com)
 *
 */
@Component
public class RoutesImplementation extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configure() throws Exception {

        final String DSPATH = "resource:classpath:datasonnet/";
//        final String ACCOUNT_QUERY = "SELECT Id, Name, Type, Industry, " +
//                "BillingStreet, BillingCity, BillingState, BillingPostalCode," +
//                " Fax, Phone, Website" +
//                " FROM Account";

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

        // Example of an implementation for a REST endpoint.
INSERT-CODE-HERE

        //  Used to add the currentRoute property.
        from("direct:util:setCurrentRouteInfo")
                .setProperty("currentRoute", simple("${headers.CamelHttpMethod} ${headers.CamelHttpUri}"))
                ;
    }
}
