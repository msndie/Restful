package edu.school21.ex02_testing.models.projections;

import edu.school21.ex02_testing.models.Role;
import edu.school21.ex02_testing.models.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "userProjection", types = User.class)
public interface UserProjection {
    String getFirstName();
    String getLastName();
    Role getRole();
    String getLogin();
}
