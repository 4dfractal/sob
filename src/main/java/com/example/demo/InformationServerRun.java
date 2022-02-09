package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example.demo")
public class InformationServerRun {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(InformationServerRun.class, args);
	}
}
