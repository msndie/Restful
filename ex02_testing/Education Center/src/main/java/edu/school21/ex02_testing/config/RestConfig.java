package edu.school21.ex02_testing.config;

import edu.school21.ex02_testing.models.Course;
import edu.school21.ex02_testing.models.Lesson;
import edu.school21.ex02_testing.models.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {
//    @Override
//    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//        ExposureConfiguration configuration = config.getExposureConfiguration();
//        configuration.forDomainType(Course.class).disablePutForCreation();
//        configuration.forDomainType(User.class).disablePutForCreation();
//        configuration.forDomainType(Lesson.class).disablePutForCreation();
//        configuration.withItemExposure(((metadata, httpMethods) -> httpMethods.disable(HttpMethod.PATCH)));
//        configuration.disablePatchOnItemResources();
//    }
}
