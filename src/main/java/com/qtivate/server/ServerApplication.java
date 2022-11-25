package com.qtivate.server;

import com.qtivate.server.model.Class;
import com.qtivate.server.respository.SubjectRepository;
import com.qtivate.server.service.SubjectService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(SubjectRepository repository, SubjectService subjectService) {
		return args -> {

		};
	}
}
