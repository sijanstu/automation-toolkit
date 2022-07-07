package com.sijanstu.playwrighthamroapi;

import com.sijanstu.playwrighthamroapi.playwright.Chromium;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		Chromium.getPage(true);
	}

}
