package com.sammy.config;

import com.sammy.filter.OrderFileFilter;
import com.sammy.processor.SampleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public SampleProcessor sampleProcessor(){
        return new SampleProcessor();
    }

    @Bean
    public OrderFileFilter<Object> fileFilter(){

        //registry.bind("myFilter", new OrderFileFilter<>());

        return new OrderFileFilter<>();
    }
}