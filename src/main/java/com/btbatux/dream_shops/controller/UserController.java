package com.btbatux.dream_shops.controller;

import com.btbatux.dream_shops.dto.UserDto;
import com.btbatux.dream_shops.exception.AlreadyExistsException;
import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.request.CreateUserRequest;
import com.btbatux.dream_shops.request.UpdateUserRequest;
import com.btbatux.dream_shops.response.ApiResponse;
import com.btbatux.dream_shops.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController //RESTful web hizmetleri
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity
                    .ok(new ApiResponse("Success", userService.getUserById(userId)));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Not Found", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            UserDto userDto = userService.createUser(createUserRequest);
            return ResponseEntity
                    .ok(new ApiResponse("Create User", userDto));
        } catch (AlreadyExistsException a) {
            return ResponseEntity
                    .status(CONFLICT).body(new ApiResponse(a.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest,
                                                  @PathVariable Long userId) {
        try {
            UserDto userDto = userService.updateUser(updateUserRequest, userId);
            return ResponseEntity
                    .ok(new ApiResponse("Update User", userDto));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse("User Not Found", null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity
                    .ok(new ApiResponse("Delete User", userId));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity
                    .status(NOT_FOUND).body(new ApiResponse("User Not Found", null));
        }
    }
}
