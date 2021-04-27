package org.hongxi.sample.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class SampleApplication {

	public static void main(String[] args) {
		Hooks.onOperatorDebug();
		SpringApplication.run(SampleApplication.class, args);
	}

}
