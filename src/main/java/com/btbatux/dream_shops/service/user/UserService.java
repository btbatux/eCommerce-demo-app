package com.btbatux.dream_shops.service.user;

import com.btbatux.dream_shops.exception.AlreadyExistsException;
import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.User;
import com.btbatux.dream_shops.repository.UserRepository;
import com.btbatux.dream_shops.request.CreateUserRequest;
import com.btbatux.dream_shops.request.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User getUserById(Long userId) {
        return userRepository.
                findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User not found! " + userId));
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }


    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(updateUserRequest.getFirstName());
            existingUser.setLastName(updateUserRequest.getLastName());
            return userRepository.save(existingUser);

        }).orElseThrow(() ->
                new ResourceNotFoundException("User not found! " + userId));

    }


    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());

                    return userRepository.save(user);
                }).orElseThrow(() ->
                        new AlreadyExistsException("Oops " + request.getEmail() + " already exists"));
    }
}
