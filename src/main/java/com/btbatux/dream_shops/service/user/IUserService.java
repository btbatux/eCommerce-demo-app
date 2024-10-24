package com.btbatux.dream_shops.service.user;

import com.btbatux.dream_shops.model.User;
import com.btbatux.dream_shops.request.CreateUserRequest;
import com.btbatux.dream_shops.request.UpdateUserRequest;

public interface IUserService {

    User getUserById(Long UserId);

    User createUser(CreateUserRequest createUserRequest);

    User updateUser(UpdateUserRequest updateUserRequest, Long userId);

    void deleteUser(Long userId);


}
