package com.onlinemarket.OnlinemarketProjectFrontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.onlinemarket.OnlinemarketProjectCommon.entity"})
public class OnlinemarketProjectFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinemarketProjectFrontendApplication.class, args);
	}

}
