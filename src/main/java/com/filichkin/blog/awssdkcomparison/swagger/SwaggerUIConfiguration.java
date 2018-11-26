package com.filichkin.blog.awssdkcomparison.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableSwagger2WebFlux
@Configuration
public class SwaggerUIConfiguration {

    @Bean
    RouterFunction<ServerResponse> routerFunction()  {
        return route(GET("/"), serverRequest -> ServerResponse.temporaryRedirect(URI.create("/swagger-ui.html")).build());
    }
    @Bean
    public Docket appApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.filichkin"))
                .build();



    }

}
