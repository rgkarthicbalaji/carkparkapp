package com.asses.park.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.asses.park"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Application to perform Parking Slot Booking",
                "Application to perform Parking Slot Booking",
                "0.0.1-SNAPSHOT",
                "Terms of Service",
                new Contact("R.G. Karthic Balaji",
                        "https://www.linkedin.com/in/r-g-karthic-balaji-67896610/",
                        "karthicbalajitoshiba@gmail.com"),
                "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList());
    }
}
