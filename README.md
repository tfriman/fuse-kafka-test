# kafka-consumer

Demo project how to unit test kafka consumer with Fuse.

```mvn test```

All external endpoints are configurable via application.properties.

For Unit test those in/out routes are fixed with test annotation:
```java
@SpringBootTest(properties = { "route.in.kafka=direct:in", "route.out.rest=direct:out" })
```

Using those injected values out is mocked and in is used to start the route. 

Test is not actually testing Kafka nor Rest integrations but how the route works. 
This is how it should be in unit tests because both Kafka and Rest components are 
tested by Camel tests and thus we should not worry about those. 

Actual e2e integration tests could be added as well, possibly with [testcontainers](https://testcontainers.org) 
and could be run locally. Testcontainers needs a running container runtime for tests to succeed and the actual test 
is somewhat slower due to the overhead of starting those test containers.