package com.ontide.oneplanner.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ontide.oneplanner.dao.AuthInfoDAO;
import com.ontide.oneplanner.dao.AuthInfoDAOImpl;
import com.ontide.oneplanner.dao.ScheduleHistoryDAO;
import com.ontide.oneplanner.dao.ScheduleHistoryDAOImpl;
import com.ontide.oneplanner.dao.ScheduleInfoDAO;
import com.ontide.oneplanner.dao.ScheduleInfoDAOImpl;
import com.ontide.oneplanner.dao.TaskInfoDAO;
import com.ontide.oneplanner.dao.TaskInfoDAOImpl;
import com.ontide.oneplanner.dao.TodayInfoDAO;
import com.ontide.oneplanner.dao.TodayInfoDAOImpl;
import com.ontide.oneplanner.dao.UserInfoDAO;
import com.ontide.oneplanner.dao.UserInfoDAOImpl;

@Configuration
@ComponentScan(basePackages="com.ontide.oneplanner")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	@Qualifier("dataSource")
	private org.apache.commons.dbcp.BasicDataSource dataSource;
	//private org.springframework.jdbc.datasource.DriverManagerDataSource dataSource;
	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
    /*<beans:bean id="transactionManager"
            class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
            <beans:property name="dataSource" ref="dataSource" />
        </beans:bean> 
    */    
	@Bean
	public DataSourceTransactionManager getTransactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}
	
/*	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(datasourcePro);
		dataSource.setConnectionProperties(datasourceProperties);
		return dataSource;
	}*/

	@Bean
	public UserInfoDAO getUserInfoDAO() {
		return new UserInfoDAOImpl(dataSource);
	}
	@Bean
	public TaskInfoDAO getTaskInfoDAO() {
		return new TaskInfoDAOImpl(dataSource);
	}
	@Bean
	public ScheduleInfoDAO getScheduleInfoDAO() {
		return new ScheduleInfoDAOImpl(dataSource);
	}
	@Bean
	public ScheduleHistoryDAO getScheduleHistoryDAO() {
		return new ScheduleHistoryDAOImpl(dataSource);
	}
	@Bean
	public AuthInfoDAO getAuthInfoDAO() {
		return new AuthInfoDAOImpl(dataSource);
	}
	@Bean
	public TodayInfoDAO getTodayInfoDAO() {
		return new TodayInfoDAOImpl(dataSource);
	}
}
