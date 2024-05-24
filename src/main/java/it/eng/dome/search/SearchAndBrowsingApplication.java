package it.eng.dome.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class SearchAndBrowsingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchAndBrowsingApplication.class, args);
	}

}
