
package com.example.studentcomplaintsystem;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

public class StudentAccount extends User {

    private final String fullName;

    public StudentAccount(
            String fullName,
            String username,
            String encodedPassword) {

        super(username, encodedPassword,
                List.of(new SimpleGrantedAuthority("ROLE_STUDENT")));

        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
