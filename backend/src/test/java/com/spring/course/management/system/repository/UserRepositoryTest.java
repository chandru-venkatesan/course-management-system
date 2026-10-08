package com.spring.course.management.system.repository;

import com.spring.course.management.system.model.Role;
import com.spring.course.management.system.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void testSaveUser() {

        User user = new User();

        user.setEmail("save-user@test.com");
        user.setPassword("password123");
        user.setRole(Role.STUDENT);
        user.setActive(true);
        user.setTokenVersion(1L);

        User savedUser =
                userRepository.save(user);

        assertNotNull(savedUser.getUserId());

        assertEquals(
                "save-user@test.com",
                savedUser.getEmail()
        );

        assertEquals(
                Role.STUDENT,
                savedUser.getRole()
        );

        assertTrue(savedUser.isActive());

        assertEquals(
                1L,
                savedUser.getTokenVersion()
        );
    }


    @Test
    void testFindAllUsers() {

        User user1 = createUser(
                "user1@test.com",
                Role.STUDENT
        );

        User user2 = createUser(
                "user2@test.com",
                Role.TRAINER
        );

        User savedUser1 =
                userRepository.save(user1);

        User savedUser2 =
                userRepository.save(user2);

        List<User> users =
                userRepository.findAll();

        assertTrue(users.size() >= 2);

        assertTrue(
                users.stream()
                        .anyMatch(user ->
                                user.getUserId()
                                        .equals(savedUser1.getUserId()))
        );

        assertTrue(
                users.stream()
                        .anyMatch(user ->
                                user.getUserId()
                                        .equals(savedUser2.getUserId()))
        );
    }


    @Test
    void testFindUserById() {

        User user =
                createUser(
                        "find-user@test.com",
                        Role.STUDENT
                );

        User savedUser =
                userRepository.save(user);

        Optional<User> result =
                userRepository.findById(
                        savedUser.getUserId()
                );

        assertTrue(result.isPresent());

        assertEquals(
                "find-user@test.com",
                result.get().getEmail()
        );

        assertEquals(
                Role.STUDENT,
                result.get().getRole()
        );
    }


    @Test
    void testFindUserByEmail() {

        User user =
                createUser(
                        "email-search@test.com",
                        Role.TRAINER
                );

        userRepository.save(user);

        Optional<User> result =
                userRepository.findByEmail(
                        "email-search@test.com"
                );

        assertTrue(result.isPresent());

        assertEquals(
                "email-search@test.com",
                result.get().getEmail()
        );

        assertEquals(
                Role.TRAINER,
                result.get().getRole()
        );
    }


    @Test
    void testExistsByEmail() {

        User user =
                createUser(
                        "exists@test.com",
                        Role.STUDENT
                );

        userRepository.save(user);

        boolean exists =
                userRepository.existsByEmail(
                        "exists@test.com"
                );

        assertTrue(exists);
    }


    @Test
    void testFindUserByIdNotFound() {

        Optional<User> result =
                userRepository.findById(99999L);

        assertFalse(result.isPresent());
    }


    @Test
    void testDeleteUser() {

        User user =
                createUser(
                        "delete-user@test.com",
                        Role.STUDENT
                );

        User savedUser =
                userRepository.save(user);

        Long userId =
                savedUser.getUserId();

        userRepository.delete(savedUser);

        Optional<User> result =
                userRepository.findById(userId);

        assertFalse(result.isPresent());
    }


    private User createUser(
            String email,
            Role role) {

        User user = new User();

        user.setEmail(email);
        user.setPassword("password123");
        user.setRole(role);
        user.setActive(true);
        user.setTokenVersion(1L);

        return user;
    }
}