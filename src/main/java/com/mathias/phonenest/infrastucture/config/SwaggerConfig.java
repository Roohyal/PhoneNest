package com.mathias.phonenest.infrastucture.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI createOpenAPIConfig(){
        //This creates an OpenAPI object
        return new OpenAPI()
                //This sets general information about the API
                .info(new Info().title("PhoneNest")
                        .version("1.0")
                        .description("A PhoneBook Application"));

    }


}


