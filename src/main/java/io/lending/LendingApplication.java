package io.lending;

import io.lending.util.DatabaseDumpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class LendingApplication {

	public static void main(String[] args) {
		SpringApplication.run(LendingApplication.class, args);
	}

	@Bean
	void backupDatabase(){
		try {
			String databaseName = "your_database_name";
			String dumpFileName = "path/to/your/dumpfile.sql";

			DatabaseDumpUtil.generateAndUploadDatabaseDump(databaseName, dumpFileName);

			log.info("Database dump generated and uploaded successfully.");
		} catch (Exception e) {
			log.info("An error occurred while generating or uploading the database dump: " + e.getMessage());
		}
	}

}
