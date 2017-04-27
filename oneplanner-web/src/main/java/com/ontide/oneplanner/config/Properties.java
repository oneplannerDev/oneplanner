package com.ontide.oneplanner.config;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.ontide.oneplanner.dao.EnvironmentBean;

@Configuration
@PropertySource("classpath:config.properties.prod")
public class Properties {

//	@Value("$(domain.name)")
//	public String domainName;
	@Autowired
	private Environment env;
	
	@Bean
	public EnvironmentBean environmentBean() throws UnsupportedEncodingException {
		EnvironmentBean envBean = new EnvironmentBean();
		envBean.setDomainName(env.getProperty("domain.name"));
		envBean.setDomainIp(env.getProperty("domain.ip"));
		envBean.setPort(env.getProperty("domain.port"));
		envBean.setWorkPath(env.getProperty("work.path"));
		envBean.setMailUserName(env.getProperty("mail.username"));
		envBean.setMailPassword(env.getProperty("mail.password"));
		envBean.setMailSmtpHost(env.getProperty("mail.smtp.host"));
		envBean.setMailSmtpPort(Integer.parseInt(env.getProperty("mail.smtp.port")));
		envBean.setMailFromMail(env.getProperty("mail.from.mail"));
		envBean.setMailSubscAuthUrl(env.getProperty("mail.subscr.auth.url"));
		envBean.setMailSubscAuthTitleKo(new String(env.getProperty("mail.subscr.auth.title.ko").getBytes("ISO-8859-1"), "UTF-8"));
		envBean.setMailSubscAuthTitleEn(new String(env.getProperty("mail.subscr.auth.title.en").getBytes("ISO-8859-1"), "UTF-8"));
		envBean.setMailSubscAuthContentKo(new String(env.getProperty("mail.subscr.auth.content.ko").getBytes("ISO-8859-1"), "UTF-8"));
		envBean.setMailSubscAuthContentEn(new String(env.getProperty("mail.subscr.auth.content.en").getBytes("ISO-8859-1"), "UTF-8"));
		envBean.setMailPasswordResetTitleKo(new String(env.getProperty("mail.passwd.reset.title.ko").getBytes("ISO-8859-1"), "UTF-8"));
		envBean.setMailPasswordResetTitleEn(new String(env.getProperty("mail.passwd.reset.title.en").getBytes("ISO-8859-1"), "UTF-8"));
		envBean.setMailpasswordResetContentKo(new String(env.getProperty("mail.passwd.reset.content.ko").getBytes("ISO-8859-1"), "UTF-8"));
		envBean.setMailpasswordResetContentEn(new String(env.getProperty("mail.passwd.reset.content.en").getBytes("ISO-8859-1"), "UTF-8"));		
/*		
		mail.subscr.auth.title
		mail.subscr.auth.content
		mail.passwd.reset.title
		mail.subscr.auth.content
*/
		return envBean;
	}
	public String getDomainName() {
		System.out.println(env.getProperty("domain.ip"));
		for (String key : env.getDefaultProfiles()) {
			System.out.println(key+":"+env.getProperty(key));
		}
		return env.getProperty("domain.name");
	}
	
//	
//	
//	domain.name=notdecided
//	domain.ip=127.9.9.1
//	sound.filepath=/asdfasdfasdfa
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
