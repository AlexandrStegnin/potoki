package com.art.config;

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
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
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
    private PersistentTokenRepository tokenRepository;

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
                .antMatchers("/savePassword").permitAll()
                .antMatchers("/turn/**", "/progress/**", "/status/**").permitAll()
                .antMatchers("/kind-on-project", "/investments", "/union-profit", "/have-unread")
                .permitAll()
                .antMatchers("/mark-read-annex", "/cashing-money", "/annexToContract/**")
                .access("hasAnyRole('ADMIN', 'INVESTOR')")
                .antMatchers("/investor/annexes", "/investor/annexes/**", "/investor/annexes/upload")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .antMatchers("/", "/welcome", "/cashflows")
                .access("hasRole('ADMIN') or hasRole('DBA') or hasRole('INVESTOR')")
                .antMatchers("createmail")
                .access("hasRole('BIGDADDY') or hasRole('DBA') or hasRole('ADMIN')")
                .antMatchers("/new**", "/delete**", "**double**", "/save**", "/show**", "/sum**")
                .access("hasRole('ADMIN')")
                .antMatchers("/getIncomes", "/getInvestorsFlows", "/getMainFlows")
                .access("hasRole('ADMIN') or hasRole('DBA') or hasRole('INVESTOR')")
                .antMatchers("/edit**", "/admin**", "catalogue", "uploadImage")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .antMatchers("/**allowance**")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .antMatchers("/**alpha**", "**tag**", "**rentors**", "/switch", "/uploadexcel",
                        "**close**", "/load**")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .antMatchers("/**bonus**")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .antMatchers("/**cashpayment**", "**cashsource**", "**source**",
                        "**facilities**", "/investors**")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .antMatchers("/tokens")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .antMatchers("/bitrix/contacts")
                .access("hasRole('ADMIN') or hasRole('DBA')")
                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("login").passwordParameter("password")
                .defaultSuccessUrl("/investments")
                .and()
                .rememberMe().rememberMeParameter("remember-me").tokenRepository(tokenRepository)
                .tokenValiditySeconds(86400)
                .and().csrf()
                .and().exceptionHandling().accessDeniedPage("/Access_Denied")
                .and().sessionManagement().enableSessionUrlRewriting(true)
                .and().sessionManagement().invalidSessionUrl("/login");
    }

    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(
                "remember-me", customUserDetailsService, tokenRepository);
    }

    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

    class MyAuthenticator extends Authenticator {
        private String user;
        private String password;

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
