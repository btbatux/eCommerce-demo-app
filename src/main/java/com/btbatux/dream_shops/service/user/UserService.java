package com.btbatux.dream_shops.service.user;

import com.btbatux.dream_shops.dto.UserDto;
import com.btbatux.dream_shops.exception.AlreadyExistsException;
import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.User;
import com.btbatux.dream_shops.repository.UserRepository;
import com.btbatux.dream_shops.request.CreateUserRequest;
import com.btbatux.dream_shops.request.UpdateUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDto getUserById(Long userId) {
        return convertUserToUserDto(userRepository.findById(userId).
                orElseThrow(() ->
                        new ResourceNotFoundException("User not found! " + userId)));
    }


    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserDto updateUser(UpdateUserRequest updateUserRequest, Long userId) {
        return userRepository
                .findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(updateUserRequest.getFirstName());
                    existingUser.setLastName(updateUserRequest.getLastName());
                    return convertUserToUserDto(userRepository.save(existingUser));
                }).orElseThrow(() ->
                        new ResourceNotFoundException("User not found! " + userId));

    }


    @Override
    public UserDto createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());

                    return convertUserToUserDto(userRepository.save(user));
                }).orElseThrow(() ->
                        new AlreadyExistsException("Oops " + request.getEmail() + " already exists"));
    }

    private UserDto convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }


    private User convertUserDtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
