package edu.school21.restful.controllers;

import edu.school21.restful.dto.UserRequest;
import edu.school21.restful.dto.UserResponse;
import edu.school21.restful.exception.BadRequestException;
import edu.school21.restful.model.User;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.DtoMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final DtoMapper dtoMapper;

    @Autowired
    public UsersController(UserService userService, DtoMapper dtoMapper) {
        this.userService = userService;
        this.dtoMapper = dtoMapper;
    }

    @ApiOperation(value = "Get all users")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserResponse>> get(@RequestParam(required = false, value = "page") Integer page,
                                                  @RequestParam(required = false, value = "size") Integer size) {
        if (page != null && size != null) {
            if (page >= 0 && size > 0) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                return ResponseEntity.ok(userService
                        .findAll(pageable)
                        .stream()
                        .map(dtoMapper::userToDto)
                        .collect(Collectors.toList()));
            } else {
                throw new BadRequestException();
            }
        }
        return ResponseEntity.ok(userService
                .findAll()
                .stream()
                .map(dtoMapper::userToDto)
                .collect(Collectors.toList()));
    }

    @ApiOperation(value = "Add new user")
    @PostMapping(produces = "application/json")
    public ResponseEntity<UserResponse> post(@Valid @RequestBody UserRequest userRequest) {
        User user = dtoMapper.userToDomain(userRequest);
        userService.save(user);
        return ResponseEntity.ok(dtoMapper.userToDto(user));
    }

    @ApiOperation(value = "Change user by user ID")
    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<UserResponse> put(@PathVariable Long userId, @Valid @RequestBody UserRequest userRequest) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent() && user.get().getRole() == userRequest.getRole()) {
            user.get().setFirstName(userRequest.getFirstName());
            user.get().setLastName(userRequest.getLastName());
            userService.update(user.get());
            return ResponseEntity.ok(dtoMapper.userToDto(user.get()));
        }
        throw new BadRequestException();
    }

    @ApiOperation(value = "Delete user by user ID")
    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<UserResponse> delete(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            userService.delete(user.get());
            return ResponseEntity.ok(dtoMapper.userToDto(user.get()));
        }
        throw new BadRequestException();
    }
}
