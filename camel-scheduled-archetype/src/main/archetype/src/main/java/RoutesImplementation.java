package ${package};

import org.apache.camel.LoggingLevel;
import org.springframework.stereotype.Component;

/**
 * The RoutesImplementation class holds implementations for the end points configured in RoutesScheduled.
 *
 * @author Maven Archetype (camel-scheduled-archetype)
 */
@Component
public class RoutesImplementation extends BaseRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        // Example of an implementation for a scheduled task end point.
        from(direct("task"))
            .log(LoggingLevel.INFO, "Task is running.")
        ;

    }
}
