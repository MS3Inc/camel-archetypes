package ${package};

import javax.annotation.Generated;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Scheduled tasks use the
 * <a href="https://camel.apache.org/components/latest/scheduler-component.html">Camel Schedule Component</a>.
 * Schedules are specified using a CRON-like string expression.
 *
 * @author Maven Archetype (camel-scheduled-archetype)
 */
@Generated("com.ms3inc.camel-oas-archetype")
@Component
public class RoutesScheduled extends EndpointRouteBuilder {

    /**
     * This is the base end point for a scheduled task where the schedule is named "schedule" and the task is named "task".
     * The task is implemented in RoutesImplementation.  The default provided here triggers every 10,000 milliseconds (10 seconds).
     */
    public void configure() {

        from(scheduler("schedule?delay=10000"))
            .to(direct("task"))
        ;
    }

}
