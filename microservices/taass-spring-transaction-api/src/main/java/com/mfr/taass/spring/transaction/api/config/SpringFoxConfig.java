/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfr.taass.spring.transaction.api.config;

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
                .groupName("transaction-api")
                .tags(
                        new Tag("transaction-management", "Create, modify, delete, manage your transactions.", 0),
                        new Tag("account-management", "Create, modify, delete, manage your accounts.", 1),
                        new Tag("category-management", "Create and delete the categories for your budgets.", 2),
                        new Tag("transaction-controller", "All the requests.", 3)
                )
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mfr.taass.spring.transaction.api"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("OurWallet Transactions API")
                .description("In this page you can find and try the requests for a Transaction in OurWallet.\n You can create "
                        + "a new transaction or transfer.\n"
                        + "You can also create and manage your Accounts.\n"
                        + "In addition, you can manage the categories for your Budgets.\n"
                        + "To make any request, you will need the token JWT given by the login function: put it in the Authorization header as Bearer {jwtToken}.\n\n")
                .termsOfServiceUrl("http://ourwallet.io")
                .contact("OurWallet")
                .license("Apache License Version 2.0")
                .version("1.0")
                .build();
    }

}
