package ru.gb.shopservice.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.gb.shopservice.proxy" )
public class FeignConfiguration {
}
