package edu.school21.restful.controllers;

import edu.school21.restful.dto.BadRequest;
import edu.school21.restful.dto.UserDto;
import edu.school21.restful.exception.BadRequestException;
import edu.school21.restful.model.User;
import edu.school21.restful.services.UserService;
import edu.school21.restful.utils.MappingUtils;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserDto>> get(@RequestParam(required = false, value = "page") Integer page,
                                             @RequestParam(required = false, value = "size") Integer size) {
        if (page != null && size != null) {
            if (page >= 0 && size > 0) {
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
                return ResponseEntity.ok(userService
                        .findAll(pageable)
                        .stream()
                        .map(MappingUtils::userToDto)
                        .collect(Collectors.toList()));
            } else {
                throw new BadRequestException();
            }
        }
        return ResponseEntity.ok(userService
                .findAll()
                .stream()
                .map(MappingUtils::userToDto)
                .collect(Collectors.toList()));
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @PostMapping(produces = "application/json")
    public ResponseEntity<UserDto> post(@RequestBody UserDto userDto) {
        if (userDto.getFirstName() == null || userDto.getLastName() == null
                || userDto.getRole() == null) {
            throw new BadRequestException();
        }
        User user = MappingUtils.userToDomain(userDto);
        userService.save(user);
        return ResponseEntity.ok(MappingUtils.userToDto(user));
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{userId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<UserDto> put(@PathVariable Long userId, @RequestBody UserDto userDto) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent() && user.get().getRole() == userDto.getRole()) {
            user.get().setFirstName(userDto.getFirstName());
            user.get().setLastName(userDto.getLastName());
            return ResponseEntity.ok(MappingUtils.userToDto(user.get()));
        }
        throw new BadRequestException();
    }

    @ApiResponse(
            responseCode="400",
            description="Error in the request",
            content=@Content(mediaType = "application/json", schema=@Schema(implementation = BadRequest.class)))
    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            userService.delete(user.get());
            return ResponseEntity.ok(null);
        }
        throw new BadRequestException();
    }
}
