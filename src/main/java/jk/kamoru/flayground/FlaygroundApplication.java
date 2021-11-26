package jk.kamoru.flayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlaygroundApplication.class, args);
	}

}
