package ${package};

import org.springframework.stereotype.Component;

/**
 * The RoutesImplementation class holds implementations for the end points configured in RoutesScheduled.
 * 
 * @author Maven Archetype (camel-scheduled-archetype)
 *
 */
@Component
public class RoutesImplementation extends BaseRouteBuilder {
    //private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configure() throws Exception {
    	super.configure();
        //final String DSPATH = "resource:classpath:datasonnet/";

        //  Error handler for all exceptions thrown by the Camel routes below.
        onException(Exception.class)
                .process(exchange -> {
                    final String EXCEPTION = exchange.getProperty("CamelExceptionCaught").toString();
	                //LOGGER.debug(EXCEPTION);
	
	                exchange.getIn().setBody("{\"message\":\""+	EXCEPTION+"\"}");
                })
                .routeId("exceptionHandler")
                .handled(true)
                .log("${body}")
        ;

        // Example of an implementation for a scheduled task end point.
        from("direct:task")
        	.log("Task is running.")
        ;

    }
}
