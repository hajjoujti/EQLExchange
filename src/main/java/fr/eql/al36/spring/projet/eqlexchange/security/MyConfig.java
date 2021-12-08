package fr.eql.al36.spring.projet.eqlexchange.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(
                (authorize) ->
                        authorize.antMatchers("/static/**",
                                              "/templates/index.html").permitAll()
                                .antMatchers("/templates/user/**",
                                             "/templates/currency/**",
                                             "/templates/transaction/**",
                                             "/templates/wallet/**").hasRole("USER")).authorizeRequests(
                (requests) -> ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated());
        http.formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/user/dashboard", true);
        http.httpBasic();
    }

}

