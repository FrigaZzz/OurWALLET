/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.group.api.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
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
          .groupName("group-api")
          .tags(
            new Tag("group-management","Create, modify, delete, manage your groups.",0),
            new Tag("member-management","Create, modify, delete, manage members of a group.",1),
            new Tag("budget-management", "Buget: monthly budget you have to respect for a determined category.\n "
                        + "If you overcame the budget, the system will not accept any more negative transaction.\n",2),
            new Tag("goal-management", "Goal: goal to achieve within a deadline. The goal has a deadline and an account associated.\n You need to do a transaction to fullfill the goal",3),
            new Tag("group-controller","All the requests.",4))
          .apiInfo(apiInfo())
          .select()    
          .apis( RequestHandlerSelectors.basePackage( "com.mfr.taass.spring.group.api" ) )
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())     
          .paths(Predicates.not(PathSelectors.regex("/error.*")))
          .build();                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("OurWallet Group API")
                .description("In this page you can find and try the requests for a Group in OurWallet.\n You can create "
                        + "a new Family Group and manage the members.\n"
                        + "You can also create and manage a group common fund, and manage his members.\n"+
                        "In addition, you can manage the Goals and the Budgets of the group.\n"+
                        "To make any request, you will need the token JWT given by the login function: put it in the Authorization header as Bearer {jwtToken}.\n\n")
                .termsOfServiceUrl("http://ourwallet.io")
                .contact("OurWallet")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }

}