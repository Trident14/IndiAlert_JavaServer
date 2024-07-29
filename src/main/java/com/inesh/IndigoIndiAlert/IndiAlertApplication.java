package com.inesh.IndigoIndiAlert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling
public class IndiAlertApplication {


	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();

        System.setProperty("ApplicationName", dotenv.get("ApplicationName"));
        System.setProperty("ServerPort", dotenv.get("ServerPort"));
        System.setProperty("DBKey", dotenv.get("DBKey"));
        System.setProperty("DBName", dotenv.get("DBName"));
        System.setProperty("KafkaBootstrapServers", dotenv.get("KafkaBootstrapServers"));
        System.setProperty("KafkaConsumerGroupId", dotenv.get("KafkaConsumerGroupId"));
        System.setProperty("MailHost", dotenv.get("MailHost"));
        System.setProperty("MailPort", dotenv.get("MailPort"));
        System.setProperty("MailUsername", dotenv.get("MailUsername"));
        System.setProperty("MailPassword", dotenv.get("MailPassword"));

		SpringApplication.run(IndiAlertApplication.class, args);
	}

}
