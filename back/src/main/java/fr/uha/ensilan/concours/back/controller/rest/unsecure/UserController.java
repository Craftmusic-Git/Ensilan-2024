package fr.uha.ensilan.concours.back.controller.rest.unsecure;

import fr.uha.ensilan.concours.back.dto.user.UserDto;
import fr.uha.ensilan.concours.back.mapper.UserMapper;
import fr.uha.ensilan.concours.back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/public/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    public final UserService userService;

    public final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") UUID id) {
        var user = userService.getUserByID(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toDto(user);
    }

    @PostMapping("/auth")
    public UserDto auth(@RequestBody UserDto user) {
        return userMapper.toDto(userService.getUserByUsernameAndLastname(user.getUsername(), user.getLastname(), user.getUserClass()));
    }
}
