package ${package};

import com.datasonnet.document.MediaTypes;
import org.apache.camel.language.datasonnet.DatasonnetExpression;
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

        /* This where the implementation routes go.
           They consume the producers that are set in RoutesGenerated. */
        from(direct("get-hello"))
            .setBody(DatasonnetExpression.builder("{greeting: 'Hello World'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
    }
}
