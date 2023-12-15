/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.auth.api.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author matteo
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2) 
          .groupName("auth-api")
          .apiInfo(apiInfo())
          .select()    
          .apis( RequestHandlerSelectors.basePackage( "com.mfr.taass.spring.auth.api" ) )
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())     
          .paths(Predicates.not(PathSelectors.regex("/error.*")))
          .build();                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("OurWallet Authentication API")
                .description("In this page you can find and try the requests to authenticate a user in OurWallet.\n You can do "
                        + "the registration using your email or using your Google Login.\n"
                        + "Authentication returns a JWT token which must be passed in the Authentication header as Bearer"
                        + ", in any other API.\n" +
                        "In addition, the token must be refreshed before expiration, which is described in the token.")
                .termsOfServiceUrl("http://ourwallet.io")
                .contact("OurWallet")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }

}