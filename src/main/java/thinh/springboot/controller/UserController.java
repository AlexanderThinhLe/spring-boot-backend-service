package thinh.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import thinh.springboot.controller.request.UserChangePwRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserResponse;
import thinh.springboot.service.UserService;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@Slf4j(topic = "USER-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user list", description = "API retrieve user from db")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('admin', 'manager')")
    public ResponseEntity<Map<String, Object>> getList(@RequestParam(required = false) String keyword,
                                                       @RequestParam(required = false) String sort, // Example: firstName:asc
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        log.info("Get user list");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "user list");
        result.put("data", userService.findAll(keyword, sort, page, size));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Get user detail", description = "API retrieve user by ID")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user')")
    public Map<String, Object> getUserDetail(@PathVariable @Validated @Min(value = 1, message = "User ID must be greater than 0") Long userId) {
        UserResponse user = userService.findById(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "User detail");
        result.put("data", user);

        return result;
    }

    @Operation(summary = "Create new user", description = "API add new user to db")
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody @Valid UserCreationRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "User created successfully");
        result.put("data", userService.save(request));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user", description = "API update user to db")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        userService.update(request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "User updated successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Change user password", description = "API change user password")
    @PatchMapping("/change-pw")
    public Map<String, Object> changePassword(@RequestBody @Valid UserChangePwRequest request) {
        userService.changePassword(request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "Updated password successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Confirm Email", description = "Confirm email for account")
    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("Confirm email for account with secretCode: {}", secretCode);

        try {
            // TODO check or compare secret code from db
        } catch (Exception e) {
            log.error("Verification fail", e.getMessage(), e);
        } finally {
            response.sendRedirect("https://alexanderthinhle.vn/login/"); // Login page
        }
    }

    @Operation(summary = "Delete user", description = "API inactivate user")
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('admin')")
    public Map<String, Object> deleteUser(@PathVariable @Validated @Min(value = 1, message = "User ID must be greater than 0") Long userId) {
        userService.delete(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.RESET_CONTENT.value());
        result.put("message", "User deleted successfully");
        result.put("data", "");

        return result;
    }
}
