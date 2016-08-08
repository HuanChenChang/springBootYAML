package org.gradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan( basePackages = {"org.gradle"})
public class WebApplication {
	public static void main(String args[]){
        //run SpringApplication
        SpringApplication.run(WebApplication.class, args);
    }
}
