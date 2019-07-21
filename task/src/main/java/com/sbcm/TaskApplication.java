package com.sbcm;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/*
* Application.java 建议放到根目录下面,主要用于做一些框架配置
* */

@EnableEurekaClient

//@SpringBootApplication是一个组合注解,用于快捷配置启动类
//"exclude" => Spring Boot会自动根据jar包的依赖来自动配置项目,但我们使用Mybatis自定义配置，想自己创建DataSource时就必须注释掉DataSourceAutoConfiguration。
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class})

//扫描所有sevlet,listener,filter的注解，如果Application类和Servlet类在同一包下，则@ServletComponentScan不需要添加相应的路径
@ServletComponentScan({"com.sbcm.*.servlet","com.sbcm.*.listener","com.sbcm.*.filter"})

//加上@MapperScan注解才能扫描到dao文件
@MapperScan({"com.sbcm.data.dao.*"})

//指明扫描bean的范围，Spring将会将在被指定的包及其下级的包中寻找bean，如果不使用@ComponentScan指明对象扫描范围，默认指扫描当前启动类所在的包里的对象
@ComponentScan("com.sbcm.**")

public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}
}