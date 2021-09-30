package com.asdf148.javaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
public class JavaprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaprojectApplication.class, args);
	}

}
