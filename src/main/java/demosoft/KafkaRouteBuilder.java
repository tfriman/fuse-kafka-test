package demosoft;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaRouteBuilder extends RouteBuilder {

    protected static final String ROUTE_ID_KAFKA_IN = "kafka-consumer-route";
    protected static final String ROUTE_ID_DIRECT_TRANSFORMATION = "transformation";
    protected static final String ROUTE_ID_DIRECT_REST = "rest-call";
    protected static final String ROUTE_DIRECT_TRANSFORM = "direct:payload-transform";
    protected static final String ROUTE_DIRECT_REST = "direct:rest-call";

    @Override
    public void configure() {

        from("{{route.in.kafka}}")
                .routeId(ROUTE_ID_KAFKA_IN)
                .log("Received '${body}' from Kafka with offset ${headers[kafka.OFFSET]}")
                .to(ROUTE_DIRECT_TRANSFORM);

       from(ROUTE_DIRECT_TRANSFORM)
                .routeId(ROUTE_ID_DIRECT_TRANSFORMATION)
                .log("Transforming payload ${body}")
                .setHeader("name").jsonpathWriteAsString("$.name")
                .setBody(simple("{\"job\":\"janitor\",\"name\":${header.name}\"}"))
                .log("Transformed payload ${body}")
                .to(ROUTE_DIRECT_REST);

        from(ROUTE_DIRECT_REST)
                .routeId(ROUTE_ID_DIRECT_REST)
                .log("Calling rest with payload '${body}'")
                .to("{{route.out.rest}}")
                .log("Rest responded with payload '${body}'");
    }
}