package thinh.springboot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import thinh.springboot.common.Gender;
import thinh.springboot.common.UserStatus;
import thinh.springboot.controller.request.AddressRequest;
import thinh.springboot.controller.request.UserChangePwRequest;
import thinh.springboot.controller.request.UserCreationRequest;
import thinh.springboot.controller.request.UserUpdateRequest;
import thinh.springboot.controller.response.UserPageResponse;
import thinh.springboot.controller.response.UserResponse;
import thinh.springboot.exception.InvalidDataException;
import thinh.springboot.exception.ResourceNotFoundException;
import thinh.springboot.model.AddressEntity;
import thinh.springboot.model.UserEntity;
import thinh.springboot.repository.AddressRepository;
import thinh.springboot.repository.UserRepository;
import thinh.springboot.service.EmailService;
import thinh.springboot.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {
        // Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); // tencot:asc|desc
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                } else {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }

        int pageNo = page > 0 ? page - 1 : 0;

        // Paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));
        Page<UserEntity> entityPage;

        // Search by keyword
        if (StringUtils.hasLength(keyword)) {
            keyword = "%" + keyword.toLowerCase() + "%";
            entityPage = userRepository.searchByKeyword(keyword, pageable);
        } else {
            entityPage = userRepository.findAll(pageable);
        }

        return getUserPageResponse(page, size, entityPage);
    }

    @Override
    public UserResponse findById(Long id) {
        UserEntity user = getUserEntity(id);

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .birthday(user.getDateOfBirth())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
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
        
        UserEntity userByUsername = userRepository.findByUsername(request.getUsername());
        if (userByUsername != null) {
            throw new InvalidDataException("Username already exists");
        }

        UserEntity userByEmail = userRepository.findByEmail(request.getEmail());
        if (userByEmail != null) {
            throw new InvalidDataException("Email already exists");
        }

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

        // Send email confirm
        try {
            emailService.sendVerificationEmail(request.getEmail(), request.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    /**
     * Convert UserEntities to UserResponse
     *
     * @param page
     * @param size
     * @param userEntities
     * @return
     */
    private static UserPageResponse getUserPageResponse(int page, int size, Page<UserEntity> userEntities) {
        log.info("Convert User Entity Page");

        List<UserResponse> userResponseList = userEntities
                .stream()
                .map(entity -> UserResponse.builder()
                        .id(entity.getId())
                        .firstName(entity.getFirstName())
                        .lastName(entity.getLastName())
                        .gender(entity.getGender())
                        .birthday(entity.getDateOfBirth())
                        .username(entity.getUsername())
                        .phone(entity.getPhone())
                        .email(entity.getEmail())
                        .build()
                ).toList();

        UserPageResponse response = new UserPageResponse();

        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(userEntities.getTotalElements());
        response.setTotalPages(userEntities.getTotalPages());
        response.setUserResponseList(userResponseList);

        return response;
    }
} 