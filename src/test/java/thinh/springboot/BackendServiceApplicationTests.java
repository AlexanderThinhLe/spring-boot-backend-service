package thinh.springboot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import thinh.springboot.controller.AuthenticationController;
import thinh.springboot.controller.EmailController;
import thinh.springboot.controller.UserController;

@SpringBootTest
class BackendServiceApplicationTests {
	@InjectMocks
	private AuthenticationController authenticationController;

	@InjectMocks
	private EmailController emailController;

	@InjectMocks
	private UserController userController;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(authenticationController);
		Assertions.assertNotNull(emailController);
		Assertions.assertNotNull(userController);
	}

}
