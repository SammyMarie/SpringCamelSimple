package com.sammy.routes;

import com.sammy.config.BaseRoutes;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldRoute extends BaseRoutes {

    @Override
    public void configure () throws Exception{
        super.configure();

        from("file:data/inbox?include=.*json")
                .routeId("queueReceiver")
                .inOnly("activemq:queue:incomingOrders");

        from("activemq:queue:incomingOrders")
                .routeId("queueSender")
                .to("{{local.ftp.URI}}");
    }
}