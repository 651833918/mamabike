package cn.powerr.mamabike.security;

import cn.powerr.mamabike.cache.CommonCacheUtil;
import cn.powerr.mamabike.common.constant.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Parameters parameters;

    @Autowired
    private CommonCacheUtil commonCacheUtil;

    private RestPreAuthenticatedProcessingFilter getPreAuthenticatedProcessingFilter() throws Exception {
        RestPreAuthenticatedProcessingFilter filter = new RestPreAuthenticatedProcessingFilter(parameters.getNoneSecurityPath(), commonCacheUtil);
        filter.setAuthenticationManager(this.authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(String.valueOf(parameters.getNoneSecurityPath())).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and().addFilter(getPreAuthenticatedProcessingFilter());
    }

    /**
     * manager set 1-n provider
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new RestAuthenticationProvider());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略HttpMethod的OPTION方法
        //cross domain request
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
