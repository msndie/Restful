package edu.school21.ex02_testing.processors;

import edu.school21.ex02_testing.models.Course;
import edu.school21.ex02_testing.models.CourseState;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class CourseModelProcessor implements RepresentationModelProcessor<EntityModel<Course>> {

    @Override
    public EntityModel<Course> process(EntityModel<Course> model) {
        if (model.getContent().getState() == CourseState.DRAFT) {
            model.add(Link.of("http://localhost:8080/courses/" + model.getContent().getId() + "/publish",
                    LinkRelation.of("publish")));
        }
        return model;
    }
}
