package com.eazybytes.accounts;

import com.eazybytes.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication

@OpenAPIDefinition( // some definitions to the REST API over swagger
        info = @Info(
                title = "Accounts Microservices REST API Documentation",
                description = "EazyBank Accounts Microservices REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Harsh Nain",
                        email = "tutor@eazybytes.com",
                        url = "https://www.eazybytes.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.eazybytes.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "EazyBank Accounts Microservices REST API Documentation",
                url = "https://www.eazybytes.com/swagger-ui.html"
        )
) //
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl") // please activate the JPA Auditing and create the bean and check the current auditor
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
public class AccountsApplication {


	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
