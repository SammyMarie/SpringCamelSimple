package com.sammy.routes;

import com.sammy.config.BaseRoutes;
import com.sammy.processor.RecipientsBean;
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
                        .to("{{activemq.xmlOrders}}")
                    .when(header("CamelFileName").endsWith(".json"))
                        .bean(RecipientsBean.class)
                        .to("{{activemq.jsonOrders}}")
                    .when(header("CamelFileName").regex("^.*(csv|csl)$"))
                        .to("{{activemq.csvOrders}}")
                .otherwise()
                    .inOnly("{{activemq.badOrders}}").stop();

        from("{{activemq.jsonOrders}}")
            .routeId("queueSender")
            .wireTap("{{activemq.auditQueue}}")
            .to("{{local.ftp.URI}}");
    }
}