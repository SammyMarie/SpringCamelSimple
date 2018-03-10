package com.sammy.config;

import org.apache.camel.builder.RouteBuilder;

public abstract class BaseRoutes extends RouteBuilder{

    @Override
    public void configure() throws Exception{

        /*restConfiguration().component("undertow")
                           .bindingMode(RestBindingMode.auto)
                           .host("0.0.0.0")
                           .port("8082")
                           .apiContextPath("/api-doc")
                           .apiProperty("api.title", "Middleware API")
                           .apiProperty("api.version", "1.0")
                           .apiProperty("cors", "true");*/
    }
}