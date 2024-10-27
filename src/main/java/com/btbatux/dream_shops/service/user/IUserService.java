package com.btbatux.dream_shops.service.user;

import com.btbatux.dream_shops.dto.UserDto;
import com.btbatux.dream_shops.model.User;
import com.btbatux.dream_shops.request.CreateUserRequest;
import com.btbatux.dream_shops.request.UpdateUserRequest;

public interface IUserService {

    UserDto getUserById(Long UserId);

    UserDto createUser(CreateUserRequest createUserRequest);

    UserDto updateUser(UpdateUserRequest updateUserRequest, Long userId);

    void deleteUser(Long userId);


    UserDto convertUserToUserDto(User user);
}
