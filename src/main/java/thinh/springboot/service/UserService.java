package thinh.springboot.service;

import thinh.springboot.controller.request.UserChangePwRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserPageResponse;
import thinh.springboot.controller.response.UserResponse;
import thinh.springboot.model.UserEntity;

public interface UserService {
    UserPageResponse findAll(String keyword, String sort, int page, int size);
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    UserResponse findByEmail(String email);
    UserEntity save(UserCreationRequest request);
    UserEntity update(UserUpdateRequest request);
    void changePassword(UserChangePwRequest request);
    void delete(Long id);
}