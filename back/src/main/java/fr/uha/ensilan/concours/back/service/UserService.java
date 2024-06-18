package fr.uha.ensilan.concours.back.service;


import fr.uha.ensilan.concours.back.domain.user.User;
import fr.uha.ensilan.concours.back.domain.user.UserClass;
import fr.uha.ensilan.concours.back.domain.user.UserType;
import fr.uha.ensilan.concours.back.dto.PageableRequest;
import fr.uha.ensilan.concours.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> getUserByID(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User saveUser(User user) {
        user.setType(UserType.USER);
        return userRepository.save(user);
    }

    @Transactional
    public User getUserByUsernameAndLastname(String username, String lastname, UserClass userClass) {
        var optUser = userRepository.findByUsernameAndLastname(username, lastname);
        if (optUser.isEmpty()) {
            var user = new User();
            user.setUsername(username);
            user.setLastname(lastname);
            user.setUserClass(userClass);
            user.setType(UserType.USER);
            return userRepository.save(user);
        }
        return optUser.get();
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(PageableRequest request) {
        return userRepository.findAll(request.getPageable());
    }

    @Transactional
    public User updateUser(User user, UUID id) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }

        throw new IllegalArgumentException("User not found");
    }

    @Transactional
    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }
}
