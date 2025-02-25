package fontys.demo.business;

import fontys.demo.domain.login.LoginRequest;
import fontys.demo.domain.login.LoginResponse;
import fontys.demo.domain.userDomain.*;
import fontys.demo.persistence.entity.UserEntity;
import fontys.demo.persistence.impl.UserJPARepository;
import fontys.demo.security.token.AccessToken;
import fontys.demo.security.token.AccessTokenEncoder;
import fontys.demo.security.token.impl.AccessTokenImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class UserService {

    private static final String USER_NOT_FOUND_MSG = "User not found with id: ";

    @Autowired
    private UserJPARepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccessTokenEncoder accessTokenEncoder;

    public UserService(UserJPARepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Transactional(readOnly = true)
    public GetUserResponse getUserById(long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MSG + userId));
        User user = convertEntityToDomain(userEntity);
        return new GetUserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    @Transactional(readOnly = true)
    public List<GetUserResponse> getAllPTs() {
        return userRepository.findAll().stream()
                .filter(user -> "PT".equals(user.getRoles()))
                .map(this::convertEntityToDomain)
                .map(user -> new GetUserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GetUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertEntityToDomain)
                .map(user -> new GetUserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()))
                .toList();
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        // Encrypting the password before saving to the database
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRoles(request.getRoles()); // Set roles, potentially default or based on input
        UserEntity savedUserEntity = userRepository.save(convertDomainToEntity(newUser));
        User savedUser = convertEntityToDomain(savedUserEntity);
        return new CreateUserResponse("User created successfully", savedUser.getId());
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public UpdateUserResponse updateUser(long userId, UpdateUserRequest request) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MSG + userId));
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword())); // Remember to hash the password
        userEntity.setEmail(request.getEmail());
        userRepository.save(userEntity);

        User updatedUser = convertEntityToDomain(userEntity);
        return new UpdateUserResponse(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(), "User updated successfully");
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        System.out.println("Login attempt for user: " + request.getUsername());
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    System.out.println("User not found");
                    return new RuntimeException("Invalid username or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            System.out.println("Password mismatch");
            throw new RuntimeException("Invalid username or password");
        }

        System.out.println("User authenticated");

        AccessToken accessToken = new AccessTokenImpl(
                userEntity.getUsername(),
                userEntity.getId(),
                userEntity.getRoles()
        );

        String token = accessTokenEncoder.encode(accessToken);

        String role = userEntity.getRoles();

        return new LoginResponse(token, role);
    }

    @Transactional
    public void deleteUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException(USER_NOT_FOUND_MSG + userId);
        }
        userRepository.deleteById(userId);
    }

    private User convertEntityToDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setEmail(entity.getEmail());
        user.setRoles(entity.getRoles());
        return user;
    }

    private UserEntity convertDomainToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword()); // Password should already be hashed
        entity.setEmail(user.getEmail());
        entity.setRoles(user.getRoles());
        return entity;
    }
}
