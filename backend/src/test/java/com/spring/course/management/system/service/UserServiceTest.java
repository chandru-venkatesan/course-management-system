package com.spring.course.management.system.service;

import com.spring.course.management.system.dto.UserRequest;
import com.spring.course.management.system.dto.UserResponse;
import com.spring.course.management.system.exception.UserNotFoundException;
import com.spring.course.management.system.model.Role;
import com.spring.course.management.system.model.User;
import com.spring.course.management.system.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    // ---------------------------------------------------------
    // 1. CREATE USER
    // ---------------------------------------------------------

    @Test
    void testCreateUser() {

        UserRequest request = new UserRequest();

        request.setEmail("student@gmail.com");
        request.setPassword("password123");
        request.setRole(Role.STUDENT);


        User savedUser = new User();

        savedUser.setUserId(1L);
        savedUser.setEmail("student@gmail.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.STUDENT);
        savedUser.setActive(true);


        when(userRepository.existsByEmail("student@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);


        UserResponse result =
                userService.createUser(request);


        assertEquals(
                1L,
                result.getUserId()
        );

        assertEquals(
                "student@gmail.com",
                result.getEmail()
        );

        assertEquals(
                Role.STUDENT,
                result.getRole()
        );

        assertEquals(
                true,
                result.isActive()
        );


        verify(userRepository, times(1))
                .existsByEmail("student@gmail.com");

        verify(passwordEncoder, times(1))
                .encode("password123");

        verify(userRepository, times(1))
                .save(any(User.class));
    }


    // ---------------------------------------------------------
    // 2. DUPLICATE EMAIL
    // ---------------------------------------------------------

    @Test
    void testCreateUserAlreadyExists() {

        UserRequest request = new UserRequest();

        request.setEmail("student@gmail.com");
        request.setPassword("password123");
        request.setRole(Role.STUDENT);


        when(userRepository.existsByEmail("student@gmail.com"))
                .thenReturn(true);


        assertThrows(
                IllegalStateException.class,
                () -> userService.createUser(request)
        );


        verify(userRepository, times(1))
                .existsByEmail("student@gmail.com");

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(userRepository, never())
                .save(any(User.class));
    }


    // ---------------------------------------------------------
    // 3. GET ALL USERS
    // ---------------------------------------------------------

    @Test
    void testGetAllUsers() {

        User user1 = new User();

        user1.setUserId(1L);
        user1.setEmail("student@gmail.com");
        user1.setRole(Role.STUDENT);
        user1.setActive(true);


        User user2 = new User();

        user2.setUserId(2L);
        user2.setEmail("trainer@gmail.com");
        user2.setRole(Role.TRAINER);
        user2.setActive(true);


        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));


        List<UserResponse> result =
                userService.getAllUsers();


        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                1L,
                result.get(0).getUserId()
        );

        assertEquals(
                "student@gmail.com",
                result.get(0).getEmail()
        );

        assertEquals(
                Role.STUDENT,
                result.get(0).getRole()
        );

        assertEquals(
                "trainer@gmail.com",
                result.get(1).getEmail()
        );

        assertEquals(
                Role.TRAINER,
                result.get(1).getRole()
        );


        verify(userRepository, times(1))
                .findAll();
    }


    // ---------------------------------------------------------
    // 4. GET USER BY ID
    // ---------------------------------------------------------

    @Test
    void testGetUserById() {

        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setRole(Role.STUDENT);
        user.setActive(true);


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));


        UserResponse result =
                userService.getUserById(1L);


        assertEquals(
                1L,
                result.getUserId()
        );

        assertEquals(
                "student@gmail.com",
                result.getEmail()
        );

        assertEquals(
                Role.STUDENT,
                result.getRole()
        );

        assertEquals(
                true,
                result.isActive()
        );


        verify(userRepository, times(1))
                .findById(1L);
    }


    // ---------------------------------------------------------
    // 5. USER NOT FOUND
    // ---------------------------------------------------------

    @Test
    void testGetUserByIdUserNotFound() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(999L)
        );


        verify(userRepository, times(1))
                .findById(999L);
    }


    // ---------------------------------------------------------
    // 6. UPDATE USER
    // ---------------------------------------------------------

    @Test
    void testUpdateUser() {

        UserRequest request = new UserRequest();

        request.setEmail("updated@gmail.com");
        request.setPassword("newPassword123");
        request.setRole(Role.ADMIN);


        User existingUser = new User();

        existingUser.setUserId(1L);
        existingUser.setEmail("student@gmail.com");
        existingUser.setPassword("oldEncodedPassword");
        existingUser.setRole(Role.STUDENT);
        existingUser.setActive(true);


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.encode("newPassword123"))
                .thenReturn("newEncodedPassword");

        when(userRepository.save(existingUser))
                .thenReturn(existingUser);


        UserResponse result =
                userService.updateUser(1L, request);


        assertEquals(
                1L,
                result.getUserId()
        );

        assertEquals(
                "updated@gmail.com",
                result.getEmail()
        );

        assertEquals(
                Role.ADMIN,
                result.getRole()
        );

        assertEquals(
                true,
                result.isActive()
        );


        verify(userRepository, times(1))
                .findById(1L);

        verify(passwordEncoder, times(1))
                .encode("newPassword123");

        verify(userRepository, times(1))
                .save(existingUser);
    }


    // ---------------------------------------------------------
    // 7. UPDATE USER NOT FOUND
    // ---------------------------------------------------------

    @Test
    void testUpdateUserUserNotFound() {

        UserRequest request = new UserRequest();

        request.setEmail("updated@gmail.com");
        request.setPassword("newPassword123");
        request.setRole(Role.ADMIN);


        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(999L, request)
        );


        verify(userRepository, times(1))
                .findById(999L);

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(userRepository, never())
                .save(any(User.class));
    }


    // ---------------------------------------------------------
    // 8. DELETE USER
    // ---------------------------------------------------------

    @Test
    void testDeleteUser() {

        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setRole(Role.STUDENT);
        user.setActive(true);


        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));


        doNothing()
                .when(userRepository)
                .delete(user);


        userService.deleteUser(1L);


        verify(userRepository, times(1))
                .findById(1L);

        verify(userRepository, times(1))
                .delete(user);
    }


    // ---------------------------------------------------------
    // 9. DELETE USER NOT FOUND
    // ---------------------------------------------------------

    @Test
    void testDeleteUserUserNotFound() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());


        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(999L)
        );


        verify(userRepository, times(1))
                .findById(999L);

        verify(userRepository, never())
                .delete(any(User.class));
    }
}