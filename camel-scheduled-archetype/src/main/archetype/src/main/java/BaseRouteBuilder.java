package ${package};

import com.datasonnet.document.DefaultDocument;
import com.datasonnet.document.Document;
import com.datasonnet.document.MediaTypes;
import org.apache.camel.language.datasonnet.DatasonnetExpression;
import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

/**
 * This class centralizes exception handling for all Camel routes.  Extend this class for all RouteBuilder classes
 * in the application.  Currently, two handlers are included:
 * <ul>
 * <li>RestException - malformed requests, etc.</li>
 * <li>Exception - everything else returns a 500 status code.</li>
 * </ul>
 * These can also be used an examples of how to handle more specific exceptions.
 * 
 * @author Maven Archetype (camel-oas-archetype)
 */
public class BaseRouteBuilder extends EndpointRouteBuilder {
	protected DatasonnetExpression datasonnetEx(String expression) {
		return (DatasonnetExpression) getContext().resolveLanguage("datasonnet").createExpression(expression);
	}

	protected DatasonnetExpression datasonnetEx(String expression, Class<?> resultType) {
		Object[] properties = new Object[3];
		properties[0] = resultType;
		return (DatasonnetExpression) getContext().resolveLanguage("datasonnet").createExpression(expression, properties);
	}

	@Override
	public void configure() throws Exception {

		onException(Exception.class)
			.routeId("exception-policy")
			.handled(true)
			.logHandled(true)
			.logStackTrace(true)
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
			.setBody(constant(DefaultDocument.NULL_INSTANCE))
			.transform(datasonnetEx("resource:classpath:exception.ds", String.class)
					.outputMediaType(MediaTypes.APPLICATION_JSON))
		;
	}
}
