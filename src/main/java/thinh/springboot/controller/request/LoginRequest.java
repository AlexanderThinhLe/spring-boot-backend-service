package thinh.springboot.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequest implements Serializable {
    private String username;
    private String password;
    private String platform; // Web or mobile or ...
    private String deviceToken;
    private String versionApp;
}
