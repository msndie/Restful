package edu.school21.restful.controllers;

import edu.school21.restful.dto.SignUpRequest;
import edu.school21.restful.exception.UserNotFound;
import edu.school21.restful.model.User;
import edu.school21.restful.security.TokenService;
import edu.school21.restful.services.UserService;
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
    public String token(@Valid @RequestBody SignUpRequest signUpRequest) {
        Optional<User> user = userService.findByLogin(signUpRequest.getLogin());
        if (user.isPresent() && passwordEncoder.matches(signUpRequest.getPassword(), user.get().getPassword())) {
            return tokenService.generateToken(user.get());
        }
        throw new UserNotFound();
    }
}
