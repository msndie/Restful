package edu.school21.ex02_testing.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UserNotFound extends RuntimeException {
}
