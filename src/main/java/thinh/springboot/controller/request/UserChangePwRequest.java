package thinh.springboot.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserChangePwRequest implements Serializable {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be greater than 0")
    private Long id;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
