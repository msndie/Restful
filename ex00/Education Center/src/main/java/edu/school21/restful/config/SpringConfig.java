package edu.school21.restful.config;

import com.fasterxml.classmate.TypeResolver;
import edu.school21.restful.dto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableJpaRepositories("edu.school21.restful.repositories")
public class SpringConfig {
    @Bean
    public Docket api(TypeResolver typeResolver) {
        List<Response> list = new ArrayList<>();
        list.add(new ResponseBuilder().code("400").description("There is an error in your request").build());
        return new Docket(DocumentationType.OAS_30)
                .additionalModels(typeResolver.resolve(BadRequest.class))
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, list)
                .globalResponses(HttpMethod.POST, list)
                .globalResponses(HttpMethod.DELETE, list)
                .globalResponses(HttpMethod.PUT, list)
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.school21.restful.controllers"))
                .paths(PathSelectors.any())
                .build();
    }
}
