package com.seah.specialsteel;

import com.seah.specialsteel.tools.ExtractJson;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;

@EnableJpaAuditing
@SpringBootApplication
public class SpecialsteelApplication {

	public static void main(String[] args) throws IOException, ParseException {
		SpringApplication.run(SpecialsteelApplication.class, args);


	}

}
