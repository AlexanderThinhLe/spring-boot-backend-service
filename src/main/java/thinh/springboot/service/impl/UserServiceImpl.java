package thinh.springboot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thinh.springboot.common.Gender;
import thinh.springboot.common.UserStatus;
import thinh.springboot.controller.request.AddressRequest;
import thinh.springboot.controller.request.UserChangePwRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserResponse;
import thinh.springboot.model.AddressEntity;
import thinh.springboot.model.UserEntity;
import thinh.springboot.repository.AddressRepository;
import thinh.springboot.repository.UserRepository;
import thinh.springboot.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<UserResponse> findAll() {
        // TODO: Implement find all users logic
        return null;
    }

    @Override
    public UserResponse findById(Long id) {
        // TODO: Implement find user by ID logic
        return null;
    }

    @Override
    public UserResponse findByUsername(String username) {
        // TODO: Implement find user by username logic
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        // TODO: Implement find user by email logic
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long save(UserCreationRequest request) {
        // TODO: Implement create user logic
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());        
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDayOfBirth());
        user.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        user.setPhone(request.getPhone());
        user.setType(request.getUserType());
        user.setStatus(UserStatus.NONE); 

        userRepository.save(user);

        log.info("Saved user: {}", user);

        if (user.getId() != null) {
            log.info("User id: {}", user.getId());
            List<AddressEntity> addresses = new ArrayList<>();
            for (AddressRequest addressRequest : request.getAddresses()) {
                AddressEntity address = new AddressEntity();
                address.setApartmentNumber(addressRequest.getApartmentNumber());
                address.setFloor(addressRequest.getFloor());
                address.setBuilding(addressRequest.getBuilding());
                address.setStreetNumber(addressRequest.getStreetNumber());
                address.setStreet(addressRequest.getStreet());
                address.setCity(addressRequest.getCity());
                address.setCountry(addressRequest.getCountry());
                address.setAddressType(addressRequest.getAddressType());
                address.setUserId(user.getId());
                addresses.add(address);
            }
            addressRepository.saveAll(addresses);
        }
        return user.getId();
    }

    @Override
    public void update(UserUpdateRequest request) {
        // TODO: Implement update user logic
    }

    @Override
    public void changePassword(UserChangePwRequest request) {
        // TODO: Implement change password logic
    }

    @Override
    public void delete(Long id) {
        // TODO: Implement delete user logic
    }
} 