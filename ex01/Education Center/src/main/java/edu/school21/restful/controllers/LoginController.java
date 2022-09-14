package edu.school21.restful.controllers;

import edu.school21.restful.dto.SignUpRequest;
import edu.school21.restful.exception.UserNotFound;
import edu.school21.restful.model.User;
import edu.school21.restful.security.TokenService;
import edu.school21.restful.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(TokenService tokenService,
                           UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/signUp", produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiResponse(responseCode = "403", description = "Wrong user credentials")
    public String token(@Valid @RequestBody SignUpRequest signUpRequest) {
        logger.debug("Token requested for user: {}", signUpRequest.getLogin());
        Optional<User> user = userService.findByLogin(signUpRequest.getLogin());
        if (user.isPresent() && passwordEncoder.matches(signUpRequest.getPassword(), user.get().getPassword())) {
            String token = tokenService.generateToken(user.get());
            logger.debug("Token granted: {}", token);
            return token;
        }
        logger.debug("User not found: {}", signUpRequest.getLogin());
        throw new UserNotFound();
    }
}
