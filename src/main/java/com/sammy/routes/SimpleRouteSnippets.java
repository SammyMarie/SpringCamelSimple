package com.sammy.routes;

import com.sammy.config.BaseRoutes;
import com.sammy.processor.RecipientsBean;
import com.sammy.processor.SampleProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRouteSnippets extends BaseRoutes {

    private final SampleProcessor sampleProcessor;
    private static final String MESSAGE_NAME = "CamelFileName";

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
                    .when(header(MESSAGE_NAME).endsWith(".xml"))
                        .to("{{activemq.xmlOrders}}")
                    .when(header(MESSAGE_NAME).endsWith(".json"))
                        .bean(RecipientsBean.class)
                        .to("{{activemq.jsonOrders}}")
                    .when(header(MESSAGE_NAME).regex("^.*(csv|csl)$"))
                        .to("{{activemq.csvOrders}}")
                .otherwise()
                    .inOnly("{{activemq.badOrders}}").stop();

        from("{{activemq.jsonOrders}}")
            .routeId("queueSender")
            .wireTap("{{activemq.auditQueue}}")
            .to("{{local.ftp.URI}}");

        from("direct:start").transform(new Expression() {
            @Override
            public <T> T evaluate(Exchange exchange, Class<T> type) {

                String body = exchange.getIn().getBody(String.class);
                body = body.replaceAll("\n", "<br />");
                body = "<body>" + body + "</body>";

                return (T) body;
            }
        }).to("log:result");
    }
}