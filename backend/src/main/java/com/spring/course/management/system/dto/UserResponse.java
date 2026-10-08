package com.spring.course.management.system.dto;

import com.spring.course.management.system.model.Role;

public class UserResponse {

    private Long userId;
    private String email;
    private Role role;
    private boolean active;

    public UserResponse() {
    }

    public UserResponse(
            Long userId,
            String email,
            Role role,
            boolean active) {

        this.userId = userId;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}