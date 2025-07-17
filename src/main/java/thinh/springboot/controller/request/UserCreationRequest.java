package thinh.springboot.controller.request;

import lombok.Getter;
import thinh.springboot.common.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
public class UserCreationRequest implements Serializable {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private Date dayOfBirth;
    private String gender;
    private String phone;

    @Email(message = "Email should be valid")
    private String email;
    
    private String username;
    private UserType userType;
    private List<AddressRequest> addresses;
}
