package com.onlinemarket.OnlinemarketProjectBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.onlinemarket.OnlinemarketProjectCommon.entity","com.onlinemarket.OnlinemarketProjectBackend.user"})
public class OnlinemarketProjectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlinemarketProjectBackendApplication.class, args);
	}

}
