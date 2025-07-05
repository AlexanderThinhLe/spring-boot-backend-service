package thinh.springboot.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserChangePwRequest implements Serializable {
    private Long id;
    private String password;
    private String confirmPassword;
}
