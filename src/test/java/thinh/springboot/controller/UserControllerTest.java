package thinh.springboot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import thinh.springboot.common.Gender;
import thinh.springboot.controller.response.UserPageResponse;
import thinh.springboot.controller.response.UserResponse;
import thinh.springboot.service.JwtService;
import thinh.springboot.service.UserService;
import thinh.springboot.service.UserServiceDetails;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class) // Load the actual controller, not the test class
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserServiceDetails userServiceDetails;

    @MockBean
    private JwtService jwtService;

    private static UserResponse thinh;
    private static UserResponse tuan;

    @BeforeEach
    void setUp() {
        thinh = new UserResponse();
        thinh.setId(1L);
        thinh.setFirstName("Alexander");
        thinh.setLastName("Le");
        thinh.setGender(Gender.MALE);
        thinh.setBirthday(new Date());
        thinh.setEmail("alexanderthinh@gmail.com");
        thinh.setPhone("0975118228");
        thinh.setUsername("thinh");

        tuan = new UserResponse();
        tuan.setId(2L);
        tuan.setFirstName("John");
        tuan.setLastName("Doe");
        tuan.setGender(Gender.FEMALE);
        tuan.setBirthday(new Date());
        tuan.setEmail("tuan@gmail.com");
        tuan.setPhone("0123456789");
        tuan.setUsername("tuan");
    }

    @Test
    @WithMockUser(authorities = {"admin", "manager"})
    void testGetList() throws Exception {
        List<UserResponse> userResponses = List.of(thinh, tuan);

        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setPageNumber(0);
        userPageResponse.setPageSize(20);
        userPageResponse.setTotalPages(1);
        userPageResponse.setTotalElements(2);
        userPageResponse.setUserResponseList(userResponses);

        when(userService.findAll(null, null, 0, 20)).thenReturn(userPageResponse);

        mockMvc.perform(get("/user/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("users")))
                .andExpect(jsonPath("$.data.totalElements", is(2)));
    }
}
