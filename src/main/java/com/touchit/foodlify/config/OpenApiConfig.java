package com.touchit.foodlify.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Muhammad Yunus",
            email = "abudukhanyunus@gmail.com",
            url = "https://touchit.click"
        ),
        description = "Foodlies API Documentation for the test phase",
        title = "Foodlies :: API-Docs",
        version = "1.0.0"
    ),
    servers = {
        @Server(
            description = "Local Dev Environment",
            url = "http://localhost:8090"
        ),
        @Server(
            description = "Production Environment",
            url = "#"
        )
    }
)
public class OpenApiConfig {
}
