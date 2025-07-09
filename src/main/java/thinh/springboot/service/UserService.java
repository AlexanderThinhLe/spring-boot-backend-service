package thinh.springboot.service;

import java.util.List;

import thinh.springboot.controller.request.UserChangePwRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserResponse;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    UserResponse findByEmail(String email);
    long save(UserCreationRequest request);
    void update(UserUpdateRequest request);
    void changePassword(UserChangePwRequest request);
    void delete(Long id);
} 