package com.imooc.plpl_piliao_api;

import com.imooc.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@MapperScan(basePackages = {"com.imooc.mapper"})
public class PlplPiliaoApiApplication {

	@Bean
	public SpringUtil getSpringUtil() {
		return new SpringUtil();
	}

	public static void main(String[] args) {
		SpringApplication.run(PlplPiliaoApiApplication.class, args);
	}
}
