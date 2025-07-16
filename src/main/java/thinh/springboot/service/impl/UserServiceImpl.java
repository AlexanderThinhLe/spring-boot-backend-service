package thinh.springboot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import thinh.springboot.common.Gender;
import thinh.springboot.common.UserStatus;
import thinh.springboot.controller.request.AddressRequest;
import thinh.springboot.controller.request.UserChangePwRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserResponse;
import thinh.springboot.exception.ResourceNotFoundException;
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
    private final PasswordEncoder passwordEncoder;

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

    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest req) {
        log.info("Updating user: {}", req);

        // Get user by id
        UserEntity user = getUserEntity(req.getId());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setGender(req.getGender());
        user.setDateOfBirth(req.getBirthday());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setUsername(req.getUsername());

        userRepository.save(user);
        log.info("Updated user: {}", user);

        // save address
        List<AddressEntity> addresses = new ArrayList<>();

        req.getAddresses().forEach(address -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(user.getId(), address.getAddressType());

            if (addressEntity == null) {
                addressEntity = new AddressEntity();
            }

            addressEntity.setApartmentNumber(address.getApartmentNumber());
            addressEntity.setFloor(address.getFloor());
            addressEntity.setBuilding(address.getBuilding());
            addressEntity.setStreetNumber(address.getStreetNumber());
            addressEntity.setStreet(address.getStreet());
            addressEntity.setCity(address.getCity());
            addressEntity.setCountry(address.getCountry());
            addressEntity.setAddressType(address.getAddressType());
            addressEntity.setUserId(user.getId());

            addresses.add(addressEntity);
        });

        // save addresses
        addressRepository.saveAll(addresses);
        log.info("Updated addresses: {}", addresses);
    }

    @Override
    public void changePassword(UserChangePwRequest req) {
        log.info("Changing password for user: {}", req);

        // Get user by id
        UserEntity user = getUserEntity(req.getId());
        if (req.getPassword().equals(req.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        userRepository.save(user);
        log.info("Changed password for user: {}", user);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user: {}", id);

        // Get user by id
        UserEntity user = getUserEntity(id);
        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
        log.info("Deleted user id: {}", id);
    }

    /**
     * Get user by id
     *
     * @param id
     * @return
     */
    private UserEntity getUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
} 