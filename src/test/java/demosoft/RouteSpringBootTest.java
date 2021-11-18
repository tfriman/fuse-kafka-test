package demosoft;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;

import static demosoft.KafkaRouteBuilder.ROUTE_ID_DIRECT_REST;
import static org.junit.Assert.assertTrue;

import org.springframework.test.annotation.DirtiesContext.ClassMode;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(properties = { "route.in.kafka=direct:in", "route.out.rest=direct:out" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class RouteSpringBootTest {

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate producerTemplate;

    @EndpointInject(uri = "mock:out")
    MockEndpoint mockEndpoint;

    @Value("${route.in.kafka}")
    String inRoute;

    @Value("${route.out.rest}")
    String outRoute;

    @Test
    public void testKafkaInRoute() throws Exception {

        mockEndpoint.expectedMessageCount(1);

        context.getRouteDefinition(ROUTE_ID_DIRECT_REST)
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        interceptSendToEndpoint(outRoute)
                                .skipSendToOriginalEndpoint()
                                .to(mockEndpoint);
                    }
                });

        String testName = "testname";
        String body = "{\"name\":  \"" + testName + "\"}";

        producerTemplate.sendBody(inRoute, body);

        mockEndpoint.assertIsSatisfied(2000);

        String executeMatchUpdate = mockEndpoint.getExchanges().get(0).getMessage().getBody(String.class);

        assertTrue(executeMatchUpdate.contains(testName));

    }
}