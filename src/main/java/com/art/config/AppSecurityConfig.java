package com.art.config;

import com.art.config.application.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("com.art")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public JavaMailSender getMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    String fileName = "mail.ru.properties";
    Properties prop = new Properties();
    InputStream input;
    try {
      input = AppSecurityConfig.class.getClassLoader().getResourceAsStream(fileName);
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }


    //Using mail.ru
    mailSender.setHost(prop.getProperty("mail.host"));
    mailSender.setPort(Integer.parseInt(prop.getProperty("mail.port")));

    mailSender.setUsername(prop.getProperty("mail.username"));
    mailSender.setPassword(prop.getProperty("mail.password"));
    mailSender.setProtocol(prop.getProperty("mail.protocol"));

    Authenticator auth = new MyAuthenticator(mailSender.getUsername(), mailSender.getPassword());

    Properties javaMailProperties = new Properties();
    javaMailProperties.put("mail.transport.protocol", prop.getProperty("mail.protocol"));
    javaMailProperties.put("mail.smtp.port", Integer.parseInt(prop.getProperty("mail.port")));
    javaMailProperties.put("mail.smtp.host", prop.getProperty("mail.host"));
    javaMailProperties.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
    javaMailProperties.put("mail.debug", prop.getProperty("mail.debug"));
    javaMailProperties.put("mail.smtp.starttls.enable", prop.getProperty("mail.smtp.starttls.enable"));

    Session session = Session.getInstance(javaMailProperties, auth);

    mailSender.setJavaMailProperties(javaMailProperties);
    mailSender.setSession(session);
    return mailSender;
  }

  @Resource(name = "customUserDetailsService")
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) {
    web
        .ignoring()
        .antMatchers("/resources/**"); // #3
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CharacterEncodingFilter filter = new CharacterEncodingFilter();
    filter.setEncoding("UTF-8");
    filter.setForceEncoding(true);
    http.addFilterBefore(filter, CsrfFilter.class);
    //rest of your code

    http
        .authorizeRequests()
        .antMatchers(Location.PERMIT_ALL_URLS).permitAll()
        .antMatchers(Location.WEBSOCKET_PATHS).permitAll()
        .anyRequest().authenticated()
        .antMatchers("/kind-on-project", Location.INVESTMENTS, "/union-profit", "/have-unread").access("hasAnyRole('ADMIN', 'INVESTOR')")
        .antMatchers("/mark-read-annex", "/cashing-money", "/annexToContract/**").access("hasAnyRole('ADMIN', 'INVESTOR')")
        .antMatchers("/uploadexcel", "**close**", "/load**", "/investors/rating**").access("hasRole('ADMIN')")
        .antMatchers(Location.ADMIN_URLS).access("hasRole('ADMIN')")
        .and().formLogin().loginPage(Location.LOGIN).permitAll()
        .loginProcessingUrl(Location.LOGIN)
        .usernameParameter("login").passwordParameter("password")
        .defaultSuccessUrl(Location.WELCOME)
        .and()
        .rememberMe().rememberMeParameter("remember-me")
        .userDetailsService(userDetailsService())
        .tokenValiditySeconds(86400)
        .and().csrf()
        .and().exceptionHandling().accessDeniedPage("/Access_Denied")
        .and().sessionManagement().invalidSessionUrl(Location.LOGIN);
  }

  @Bean
  public AuthenticationTrustResolver getAuthenticationTrustResolver() {
    return new AuthenticationTrustResolverImpl();
  }

  static class MyAuthenticator extends Authenticator {
    private final String user;
    private final String password;

    MyAuthenticator(String user, String password) {
      this.user = user;
      this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
      String user = this.user;
      String password = this.password;
      return new PasswordAuthentication(user, password);
    }

  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
