package fr.uha.ensilan.concours.back.controller.rest.admin;

import fr.uha.ensilan.concours.back.domain.user.User;
import fr.uha.ensilan.concours.back.dto.PageableRequest;
import fr.uha.ensilan.concours.back.dto.user.UserDto;
import fr.uha.ensilan.concours.back.mapper.UserMapper;
import fr.uha.ensilan.concours.back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@CrossOrigin
public class AdminUserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public Page<UserDto> findAllUsers(@ModelAttribute PageableRequest request) {
        return userService.getAllUsers(request).map(userMapper::toDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable UUID id) {
        User user = userMapper.toModel(userDto);
        return userMapper.toDto(userService.updateUser(user, id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
    }
}
