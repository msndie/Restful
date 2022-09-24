package edu.school21.restful.controllers;

import edu.school21.restful.dto.SignUpRequest;
import edu.school21.restful.exceptions.UserNotFound;
import edu.school21.restful.models.User;
import edu.school21.restful.repositories.UserRepository;
import edu.school21.restful.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signUp", produces = MediaType.TEXT_PLAIN_VALUE)
    public String  token(@Valid @RequestBody SignUpRequest signUpRequest) {
        logger.debug("Token requested for user: {}", signUpRequest.getLogin());
        Optional<User> user = userRepository.findByLogin(signUpRequest.getLogin());
        if (user.isPresent() && passwordEncoder.matches(signUpRequest.getPassword(), user.get().getPassword())) {
            String token = tokenService.generateToken(user.get());
            logger.debug("Token granted: {}", token);
            return token;
        }
        logger.debug("User not found: {}", signUpRequest.getLogin());
        throw new UserNotFound();
    }
}
