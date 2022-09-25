package edu.school21.restful.listeners;

import edu.school21.restful.exceptions.BadRequestException;
import edu.school21.restful.models.User;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener extends AbstractRepositoryEventListener<User> {
    @Override
    protected void onBeforeCreate(User entity) {
        validate(entity);
    }

    @Override
    protected void onBeforeSave(User entity) {
        validate(entity);
    }

    private void validate(User user) {
        if (user == null || user.getLogin() == null || user.getPassword() == null
                || user.getFirstName() == null || user.getLastName() == null || user.getRole() == null
                || user.getFirstName().trim().isEmpty() || user.getLastName().trim().isEmpty()
                || user.getLogin().trim().isEmpty() || user.getPassword().trim().isEmpty()) {
            throw new BadRequestException();
        }
    }
}
