package ${package};

import com.datasonnet.document.Document;
import com.datasonnet.document.MediaTypes;
import com.ms3_inc.camel.extensions.rest.OperationResult;
import com.ms3_inc.camel.extensions.rest.exception.RestException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class BaseRestRouteBuilder extends RouteBuilder {
	private static final Processor REST_EXCEPTION_PROCESSOR = ex -> {
		RestException exc = ex.getProperty(Exchange.EXCEPTION_CAUGHT, RestException.class);

		OperationResult result = ex.getMessage()
			.getHeader(OperationResult.EXCHANGE_OPERATION_RESULT, new OperationResult(), OperationResult.class)
			.addMessage(exc.getOperationResultMessage());

		ex.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, exc.httpStatusCode().orElse(500));
		ex.getMessage().setHeader(OperationResult.EXCHANGE_OPERATION_RESULT, result);
	};

	private static final Processor UNWRAP_DOCUMENT = ex -> {
		Object doc = ex.getMessage().getBody();
		if (doc instanceof Document) {
			ex.getMessage().setBody(((Document<?>) doc).getContent());
			ex.getMessage().setHeader(Exchange.CONTENT_TYPE, ((Document<?>) doc).getMediaType().toString());
		}
	};

	@Override
	public void configure() throws Exception {
		onException(RestException.class)
			.routeId("rest-exception-policy")
			.handled(true)
			.logHandled(true)
			.logStackTrace(true)
			.process(REST_EXCEPTION_PROCESSOR)
			.transform(datasonnet("resource:classpath:rest-exception.ds")
					.outputMediaType(MediaTypes.APPLICATION_JSON_VALUE)
			);

		onException(Exception.class)
			.routeId("exception-policy")
			.handled(true)
			.logHandled(true)
			.logStackTrace(true)
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
			.transform(datasonnet("resource:classpath:exception.ds")
					.outputMediaType(MediaTypes.APPLICATION_JSON_VALUE)
			);
	}
}
