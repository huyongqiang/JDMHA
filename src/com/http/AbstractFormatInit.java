package com.http;

import java.util.List;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.http.beans.config.ConfigurablePropertiesFactory;
import com.http.beans.factory.support.XmlDefinitionReader;
import com.http.core.io.Resource;
import com.http.core.io.resource.DefaultResourceLoader;
import com.http.utils.Assert;

/**
 * 
 * xml模板初始化入口<br>
 * 
 * @Project:JDMHA
 * @file:AbStractFormatInit.java
 *
 * @Author:chenssy
 * @email:chenssy995812509@163.com
 * @url : <a href="http://cmsblogs.com">http://cmsblogs.com</a>
 * @qq : 122448894
 *
 * @data:2016年1月5日
 */
public abstract class AbstractFormatInit extends DefaultResourceLoader{
	private	final Logger logger = Logger.getLogger(AbstractFormatInit.class);
	
	private static ApplicationContext context;
	
	private ServletContextEvent contextEvent;
	
	@Override
	public void contextInitialized(ServletContextEvent context) {
		try {
			super.contextInitialized(context);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		
		//设置context
		this.contextEvent = context;
		setContext(WebApplicationContextUtils.getRequiredWebApplicationContext(context.getServletContext()));
		
		//解析properties配置文件
		ConfigurablePropertiesFactory.loadProperties();
		
		/*
		 * 调用初始化方法
		 * 采用模板方法模式，该方法由具体的子类来实现
		 */
		initalizeXmlFormat();
	}

	/**
	 * 
	 * 初始化入口；由子类来实现<br>
	 * 初始化过程主要包括三个步骤：1资源定位、2资源解析、3注入<br>
	 * 1资源定位。Resource接口提供统一的资源、ResourceLoader提供统一的资源加载策略。资源定位返回为File[]<br>
	 * 2资源解析。采用SAX来完成对xml文件的解析，全部解析为统一的数据结构XMLDefinition<br>
	 * 3注入。将XMLDefinition注入容器当中，容器其本身就是一个HashMap的数据结构
	 * 
	 * @author:chenssy
	 * @data:2016年1月5日
	 *
	 */
	public abstract void initalizeXmlFormat();
	
	/**
	 * 资源解析和注册<br>
	 * 资源解析：使用SAX来完成对xml的解析，将其中元素封装为XMLDefinition<br>
	 * 注册：
	 * @author:chenssy
	 * @data:2016年1月13日
	 *
	 * @param resources
	 */
	public void loadXMLBeanDefinition(List<Resource> resources){
		Assert.notNull(resources, "Resources must not be null");
		XmlDefinitionReader reader = new XmlDefinitionReader();
		//迭代Resource资源
		for(Resource resource : resources){
			reader.loadxmlDefinition(resource);
		}
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext ctx) {
		context = ctx;
	}

	public ServletContextEvent getContextEvent() {
		return contextEvent;
	}
	
}
