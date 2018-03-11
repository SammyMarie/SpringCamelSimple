package com.sammy.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class SampleProcessor implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("We've just downloaded: " + exchange.getIn().getHeader("CamelFileName"));
    }
}