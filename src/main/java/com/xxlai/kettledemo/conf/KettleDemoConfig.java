package com.xxlai.kettledemo.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class KettleDemoConfig {
	// https://blog.csdn.net/whereismatrix/article/details/54883065
	@Bean(name="mysqlDB")
	@ConfigurationProperties(prefix="spring.ds-mysql")
	public DataSource mysqlDataSource(){
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="mysqlJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("mysqlDB") DataSource dsMySQL){
		return new JdbcTemplate(dsMySQL);
	}
	
	@Bean(name="postgresDB")
	@ConfigurationProperties(prefix="spring.ds-post")
	public DataSource postgresDataSource(){
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="postgresJdbcTemplate")
	public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDB") DataSource dsPostgres){
		return new JdbcTemplate(dsPostgres);
	}
}
