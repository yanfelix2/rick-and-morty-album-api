package com.albumrickandmortyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;


// Esse enableFeignClients é necessário para habilitar o uso do Feign na aplicação Spring Boot
@EnableFeignClients // Habilita o uso do Feign Clients na aplicação Spring Boot
@SpringBootApplication // Anotação principal que indica que esta é uma aplicação Spring Boot
public class AlbumRickandmortyApiApplication {

	public static void main(String[] args) { // Método principal que inicia a aplicação Spring Boot
		SpringApplication.run(AlbumRickandmortyApiApplication.class, args);
	}




}
