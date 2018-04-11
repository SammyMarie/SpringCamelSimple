package com.sammy.routes;

import com.sammy.config.BaseRoutes;
import com.sammy.processor.OrderTransformProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentEnrichingRoutes extends BaseRoutes {

    private final OrderTransformProcessor transformProcessor;

    @Autowired
    public ContentEnrichingRoutes(OrderTransformProcessor transformProcessor) {
        this.transformProcessor = transformProcessor;
    }

    @Override
    public void configure() throws Exception {

        super.configure();

        from("quartz2://report?cron=0+0+6+*+*+?")
                .to("http://riders.com/cmd=received&date=yesterday")
            .process(transformProcessor)
                .pollEnrich("{{local.ftp.URI}}", (oldExchange, newExchange) -> {
                    if(newExchange == null){
                        return oldExchange;
                    }

                    String http = oldExchange.getIn().getBody(String.class);
                    String ftp = newExchange.getIn().getBody(String.class);

                    String body = http + "\n" + ftp;

                    oldExchange.getIn().setBody(body);
                    return oldExchange;
                })
        .to("file://riders/orders?fileName=report-${header.Date}.csv");
    }
}