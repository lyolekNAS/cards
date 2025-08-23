package org.sav.fornas.cards;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CardsWebApplication {

	public static void main(String[] args) {
		log.info("------------------------------------Starting--------------------------------------");
		SpringApplication.run(CardsWebApplication.class, args);
		log.info("------------------------------------Started--------------------------------------");
	}

}
