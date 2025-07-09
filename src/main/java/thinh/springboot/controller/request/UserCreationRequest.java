package thinh.springboot.controller.request;

import lombok.Getter;
import thinh.springboot.common.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
public class UserCreationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private Date dayOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String username;
    private UserType userType;
    private List<AddressRequest> addresses;
}
