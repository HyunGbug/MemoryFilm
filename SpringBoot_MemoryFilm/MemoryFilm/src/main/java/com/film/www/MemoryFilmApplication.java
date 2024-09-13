package com.film.www;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.film.www.mapper")
public class MemoryFilmApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoryFilmApplication.class, args);
	}
}
