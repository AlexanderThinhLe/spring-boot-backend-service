package thinh.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import thinh.springboot.controller.request.UserChangePwRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserResponse;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mockup/user")
@Tag(name = "Mockup User Controller")
public class MockupUserController {
    @Operation(summary = "Get user list", description = "API retrieve user from db")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(1L);
        userResponse1.setFirstName("Thinh Le");
        userResponse1.setLastName("Alexander");
        userResponse1.setGender("");
        userResponse1.setBirthday(new Date());
        userResponse1.setUsername("Admin");
        userResponse1.setEmail("admin@gmail.com");
        userResponse1.setPhone("0866681063");

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2L);
        userResponse2.setFirstName("Thinh Le 2");
        userResponse2.setLastName("Alexander");
        userResponse2.setGender("");
        userResponse2.setBirthday(new Date());
        userResponse2.setUsername("Admin 2");
        userResponse2.setEmail("admin2@gmail.com");
        userResponse2.setPhone("0866681063");

        List<UserResponse> userList = List.of(userResponse1, userResponse2);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "List user");
        result.put("data", userList);

        return result;
    }

    @Operation(summary = "Get user detail", description = "API retrieve user by ID")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetail(@PathVariable @Validated @Min(1) Long userId) {
        UserResponse user = new UserResponse();
        user.setId(1L);
        user.setFirstName("Thinh Le");
        user.setLastName("Alexander");
        user.setGender("");
        user.setBirthday(new Date());
        user.setUsername("Admin");
        user.setEmail("admin@gmail.com");
        user.setPhone("0866681063");

        List<UserResponse> userDetail = List.of(user, user);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "User detail");
        result.put("data", user);

        return result;
    }

    @Operation(summary = "Create new user", description = "API add new user to db")
    @PostMapping("/add")
    public Map<String, Object> createUser(@RequestBody UserCreationRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "User created successfully");
        result.put("data", 3);

        return result;
    }

    @Operation(summary = "Update user", description = "API update user to db")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody UserUpdateRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "User updated successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Change user password", description = "API change user password")
    @PatchMapping("/change-pw")
    public Map<String, Object> changePassword(@RequestBody UserChangePwRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Updated password successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Delete user", description = "API inactivate user")
    @DeleteMapping("/delete/{userId}")
    public Map<String, Object> deleteUser(@PathVariable @Validated @Min(1) Long userId) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.RESET_CONTENT.value());
        result.put("message", "User deleted successfully");
        result.put("data", "");

        return result;
    }
}
