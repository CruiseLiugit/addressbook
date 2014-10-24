package com.vaadin.demo.tutorial.addressbook.mvc.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.random.R;
import org.nutz.lang.random.StringGenerator;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.vaadin.demo.tutorial.addressbook.dao.impl.PersonManagerBean;
import com.vaadin.demo.tutorial.addressbook.data.Person;



@IocBean
public class PersonInfoAction {

	private static final Log log = Logs.get();

	@Inject("refer:personManagerBean")
	public PersonManagerBean personManagerBean;

	//----------------------------------------------------
	//随便查询一个数据，返回 页面
	@At("/person")
	// @Ok("json")
	public void searchPerson(@Param("id") String pid,HttpServletResponse response) {
		Person p = personManagerBean.getPerson(Lang.str2number(pid).intValue());
		String json = Json.toJson(p);
		log.info("====> json =" + json);

		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}

		// return json;
	}
	
	public PersonInfoAction() {
		// TODO Auto-generated constructor stub
	}

	
	
}
