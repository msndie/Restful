package edu.school21.ex02_testing.listeners;

import edu.school21.ex02_testing.exceptions.BadRequestException;
import edu.school21.ex02_testing.models.User;
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
        if (user.getFirstName().trim().isEmpty() || user.getLastName().trim().isEmpty()
                || user.getLogin().trim().isEmpty() || user.getPassword().trim().isEmpty()) {
            throw new BadRequestException();
        }
    }
}
