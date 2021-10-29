package com.school.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@SpringBootApplication
@MapperScan("com.school.manager.mapper")
public class App {
	public static void main(String[] args){
		SpringApplication.run(App.class, args);
	}
}
