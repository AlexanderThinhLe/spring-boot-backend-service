package thinh.springboot.controller.request;

import lombok.Getter;
import lombok.ToString;
import thinh.springboot.common.Gender;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@ToString
public class UserUpdateRequest implements Serializable {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be greater than 0")
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private Gender gender;
    private Date birthday;
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    private String phone;
    private List<AddressRequest> addresses;
}