package edu.school21.restful.controllers;

import edu.school21.restful.dto.UserDto;
import edu.school21.restful.model.BadRequest;
import edu.school21.restful.model.User;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final Map<String, Object> error;
    private final UserMapper userMapper;

    @Autowired
    public UsersController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.error = Collections.singletonMap("error", BadRequest.getInstance());
    }

    @GetMapping
    public ResponseEntity<Object> get() {
        return ResponseEntity.ok(userService
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Object> post(@RequestBody UserDto userDto) {
        if (userDto.getFirstName() == null || userDto.getLastName() == null
                || userDto.getRole() == null) {
            return ResponseEntity.badRequest().body(error);
        }
        User user = userMapper.toDomain(userDto);
        userService.save(user);
        return ResponseEntity.ok(Collections.singletonMap("user", userMapper.toDto(user)));
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> put(@PathVariable Long userId, @RequestBody UserDto userDto) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent() && user.get().getRole() == userDto.getRole()) {
            user.get().setFirstName(userDto.getFirstName());
            user.get().setLastName(userDto.getLastName());
            return ResponseEntity.ok(Collections.singletonMap("user", userMapper.toDto(user.get())));
        }
        return ResponseEntity.badRequest().body(error);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            userService.delete(user.get());
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.badRequest().body(error);
    }
}
