package com.xxlai.kettledemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	@Autowired
	@Qualifier("mysqlJdbcTemplate")
	private JdbcTemplate mySqlJdbcTemplate;

	@Autowired
	@Qualifier("postgresJdbcTemplate")
	private JdbcTemplate postgresJdbcTemplate;

	@RequestMapping(value = "/getPGDept",method=RequestMethod.GET)
	public String getPGDept() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from step_table";
		list = postgresJdbcTemplate.queryForList(sql);
		return "PostgreSQL Data: " + list.toString();
	}
	
	@RequestMapping(value="/getMYDept",method=RequestMethod.GET)
	public String getMYDept(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from t_dept";
		list = mySqlJdbcTemplate.queryForList(sql);
		return "MYSQL Data: " + list.toString();
	}
}
