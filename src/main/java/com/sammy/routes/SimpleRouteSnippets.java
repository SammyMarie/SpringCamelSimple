package com.sammy.routes;

import com.sammy.config.BaseRoutes;
import com.sammy.processor.SampleProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRouteSnippets extends BaseRoutes {

    private SampleProcessor sampleProcessor;

    @Autowired
    public SimpleRouteSnippets(SampleProcessor sampleProcessor) {
        this.sampleProcessor = sampleProcessor;
    }

    @Override
    public void configure () throws Exception{
        super.configure();

        from("file:src/main/resources/data/inbox")
                .routeId("queueReceiver")
                .process(sampleProcessor)
                .choice()
                    .when(header("CamelFileName").endsWith(".xml"))
                    .to("activemq:queue:incomingXMLOrders")
                .otherwise()
                    .inOnly("activemq:queue:incomingOrders");

        from("activemq:queue:incomingOrders")
                .routeId("queueSender")
                .to("{{local.ftp.URI}}");
    }
}