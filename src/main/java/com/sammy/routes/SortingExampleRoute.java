package com.sammy.routes;

import com.sammy.config.BaseRoutes;
import org.springframework.stereotype.Component;

@Component
public class SortingExampleRoute extends BaseRoutes{

    @Override
    public void configure () throws Exception {
        super.configure();

        from("{{activemq.xmlOrders}}")
            .filter(header("CamelFileName").contains("2"))
            .log("Received XML order: ${header.CamelFileName}")
        .to("{{activemq.secondOrders}}");

        from("{{activemq.jsonOrders}}")
            .filter().jsonpath("$..CustInfo[?(@.infotype == 'LoanCustomer')]")
            .multicast().parallelProcessing()
        .to("{{activemq.accountQueue}}", "{{activemq.customerServices}}");
    }
}