package thinh.springboot.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import thinh.springboot.common.Gender;
import thinh.springboot.common.UserStatus;
import thinh.springboot.common.UserType;
import thinh.springboot.controller.request.AddressRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserPageResponse;
import thinh.springboot.controller.response.UserResponse;
import thinh.springboot.exception.ResourceNotFoundException;
import thinh.springboot.model.UserEntity;
import thinh.springboot.repository.AddressRepository;
import thinh.springboot.repository.UserRepository;
import thinh.springboot.service.impl.UserServiceImpl;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;
    private @Mock UserRepository userRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @Mock EmailService emailService;
    private @Mock AddressRepository addressRepository;

    private static UserEntity thinh;
    private static UserEntity tuan;

    @BeforeAll
    static void beforeAll() {
        thinh = new UserEntity();
        thinh.setId(1L);
        thinh.setFirstName("Alexander");
        thinh.setLastName("Le");
        thinh.setGender(Gender.MALE);
        thinh.setBirthday(new Date());
        thinh.setEmail("alexanderthinh@gmail.com");
        thinh.setPhone("0975118228");
        thinh.setUsername("thinh");
        thinh.setPassword("password");
        thinh.setType(UserType.USER);
        thinh.setStatus(UserStatus.ACTIVE);

        tuan = new UserEntity();
        tuan.setId(2L);
        tuan.setFirstName("John");
        tuan.setLastName("Doe");
        tuan.setGender(Gender.FEMALE);
        tuan.setBirthday(new Date());
        tuan.setEmail("tuan@gmail.com");
        tuan.setPhone("0123456789");
        tuan.setUsername("tuan");
        tuan.setPassword("password");
        tuan.setType(UserType.USER);
        tuan.setStatus(UserStatus.ACTIVE);
    }

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, addressRepository, passwordEncoder, emailService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetUserList_Success() {
        // Simulate method search of UserRepository
        Page<UserEntity> userPage = new PageImpl<>(List.of(thinh, tuan));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Call method findAll in UserService
        UserPageResponse result = userService.findAll(null, null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(2, result.totalElements);
    }

    @Test
    void testGetUserListByKeyword_Success() {
        // Mock searchByKeyword with expected wrapped, lowercased keyword
        Page<UserEntity> userPage = new PageImpl<>(List.of(thinh));
        when(userRepository.searchByKeyword(eq("%alexanderthinh%"), any(Pageable.class))).thenReturn(userPage);

        // Execute
        UserPageResponse result = userService.findAll("alexanderthinh", null, 1, 10);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.totalElements);
        assertEquals("thinh", result.getUserResponseList().get(0).getUsername());
        assertEquals("alexanderthinh@gmail.com", result.getUserResponseList().get(0).getEmail());
    }

    @Test
    void testGetUserList_Empty() {
        // Simulate method search of UserRepository
        Page<UserEntity> userPage = new PageImpl<>(List.of());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Call method findAll in UserService
        UserPageResponse result = userService.findAll(null, null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(0, result.totalElements);
    }

    @Test
    void testGetUserById_Success() {
        // Simulate method findById of UserRepository
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(thinh));

        UserResponse userResponse = userService.findById(1L);
        Assertions.assertNotNull(userResponse);
        assertEquals("thinh", userResponse.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        // Simulate method findById of UserRepository
        when(userRepository.findById(3L)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(3L);
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFindByUsername_Success() {
        // Simulate method findByUsername of UserRepository
        when(userRepository.findByUsername("thinh")).thenReturn(thinh);

        UserResponse userResponse = userService.findByUsername("thinh");
        Assertions.assertNotNull(userResponse);
        assertEquals("thinh", userResponse.getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        // Simulate method findByUsername of UserRepository
        when(userRepository.findByUsername("Alan Walker")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.findByUsername("Alan Walker");
        });

        String expectedMessage = "User with username Alan Walker not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void findByEmail() {
    }

    @Test
    void testSaveUser_Success() {
        UserEntity savedUser = new UserEntity();
        savedUser.setId(3L);
        savedUser.setFirstName("Naruto");
        savedUser.setLastName("Uzumaki");
        savedUser.setBirthday(new Date());
        savedUser.setGender(Gender.MALE);
        savedUser.setPhone("0987654321");
        savedUser.setEmail("testemail@gmail.com");
        savedUser.setUsername("Uzumaki Naruto");

        // Mock to simulate saving user
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        // Fake request
        UserCreationRequest request = new UserCreationRequest();
        request.setFirstName("Alice");
        request.setLastName("Wonderland");
        request.setDayOfBirth(new Date());
        request.setGender("FEMALE");
        request.setPhone("0987654321");
        request.setEmail("testemail@gmail.com");
        request.setUsername("alice");
        request.setUserType(UserType.USER);

        AddressRequest address1 = new AddressRequest();
        address1.setApartmentNumber("1");
        address1.setFloor("4");
        address1.setBuilding("Sunshine Building");
        address1.setStreetNumber("40");
        address1.setStreet("Le Duan");
        address1.setCity("Ho Chi Minh City");
        address1.setCountry("Vietnam");
        address1.setAddressType(1);

        AddressRequest address2 = new AddressRequest();
        address1.setApartmentNumber("2");
        address1.setFloor("3");
        address1.setBuilding("Madison Building");
        address1.setStreetNumber("32");
        address1.setStreet("Nguyen Hue");
        address1.setCity("Ho Chi Minh City");
        address1.setCountry("Vietnam");
        address1.setAddressType(1);

        List<AddressRequest> addresses = List.of(address1, address2);
        request.setAddresses(addresses);

        UserEntity userResponse = userService.save(request);

        assertTrue(userResponse.getId() > 0);
        assertEquals(3L, userResponse.getId());
        assertEquals("Uzumaki Naruto", userResponse.getUsername());
    }

    @Test
    void testUpdateUser_Success() {
        Long userId = 2L; // It means update user "tuan"

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userId);
        updatedUser.setFirstName("tuan updated");
        updatedUser.setLastName("bui");
        updatedUser.setBirthday(new Date());
        updatedUser.setGender(Gender.MALE);
        updatedUser.setPhone("0987654321");
        updatedUser.setEmail("testemail@gmail.com");
        updatedUser.setUsername("alice");

        // Mock to simulate saving user
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.ofNullable(tuan));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setId(userId);
        updateRequest.setFirstName("tuan updated");
        updateRequest.setLastName("bui");
        updateRequest.setBirthday(new Date());
        updateRequest.setGender(Gender.MALE);
        updateRequest.setPhone("0987654321");
        updateRequest.setEmail("testemail@gmail.com");
        updateRequest.setUsername("alice");
        updateRequest.setAddresses(List.of());

        UserEntity result = userService.update(updateRequest);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("tuan updated", result.getFirstName());
    }

    @Test
    void changePassword() {
    }

    @Test
    void delete() {
        Long userId = 2L; // It means delete user "tuan"

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.ofNullable(tuan));
        userService.delete(userId);

        assertEquals(UserStatus.INACTIVE, tuan.getStatus());
        verify(userRepository, times(1)).save(tuan);
    }
}