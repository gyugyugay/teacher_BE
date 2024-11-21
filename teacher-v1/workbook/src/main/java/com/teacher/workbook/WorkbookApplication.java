package com.teacher.workbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class WorkbookApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(WorkbookApplication.class)
				.properties("spring.config.location=classpath:/application.yml,classpath:/application-secret.yml")
				.run(args);

		//SpringApplication.run(WorkbookApplication.class, args);
	}

}
