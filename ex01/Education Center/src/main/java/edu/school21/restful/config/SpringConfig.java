package edu.school21.restful.config;

import com.fasterxml.classmate.TypeResolver;
import edu.school21.restful.dto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

@Configuration
@EnableJpaRepositories("edu.school21.restful.repositories")
public class SpringConfig {
    @Bean
    public Docket api(TypeResolver typeResolver) {
        Response response400 = new ResponseBuilder().code("400").description("There is an error in your request").build();
        Response response401 = new ResponseBuilder().code("401").description("Unauthorized").build();
        Response response403 = new ResponseBuilder().code("403").description("Forbidden").build();
        List<Response> listForGet = new ArrayList<>();
        List<Response> listForElse = new ArrayList<>();
        listForGet.add(response400);
        listForGet.add(response401);
        listForElse.add(response400);
        listForElse.add(response403);
        return new Docket(DocumentationType.OAS_30)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(jwtScheme()))
                .additionalModels(typeResolver.resolve(BadRequest.class))
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, listForGet)
                .globalResponses(HttpMethod.POST, listForElse)
                .globalResponses(HttpMethod.DELETE, listForElse)
                .globalResponses(HttpMethod.PUT, listForElse)
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.school21.restful.controllers"))
                .paths(PathSelectors.any())
                .build();
    }

    private SecurityScheme jwtScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name("JWT")
                .description("Please get token at 'signUp'")
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(defaultAuth()))
                .build();
    }

    private SecurityReference defaultAuth() {
        return SecurityReference.builder()
                .scopes(new AuthorizationScope[0])
                .reference("JWT")
                .build();
    }
}
