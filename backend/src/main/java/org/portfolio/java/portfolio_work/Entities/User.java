package org.portfolio.java.portfolio_work.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.portfolio.java.portfolio_work.Registration.Requests.CreateUserRequest;
import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class User
{
    @Id
    @Column(name = "id",  updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    @Column(name = "lastName", nullable = false)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role = Role.USER;


    @Column(name = "token_version", nullable = false)
    private long tokenVersion = 0L;

    @CreatedDate
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    @CreationTimestamp
    private Instant createdAt;

    @CreatedBy
    @Column(
            name = "created_by",
            nullable = false,
            updatable = false,
            length = 100
    )
    private String createdBy;

    @LastModifiedDate
    @Column(
            name = "updated_at",
            nullable = false
    )
    @UpdateTimestamp
    private Instant updatedAt;

    @LastModifiedBy
    @Column(
            name = "updated_by",
            nullable = false,
            length = 100
    )
    private String updatedBy;

    public User (CreateUserRequest request)
    {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.email = request.getEmail();
        this.password = passwordEncryptor(request.getPassword());
        this.createdBy = "SYSTEM";
        this.updatedBy = "SYSTEM";
    }

    public User (LoginRequest request)
    {
        this.email = request.getEmail();
        this.password = request.getPassword();
    }

    public User(UUID id, String firstName, String lastName, String email, String password, Role role)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.tokenVersion = 0L;
    }

    private String passwordEncryptor(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public void incrementTokenVersion() {
        this.tokenVersion++;
    }

    public void updateName(
            String firstName,
            String lastName
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void changeEmail(String email) {
        this.email = email;
        incrementTokenVersion();
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
        incrementTokenVersion();
    }

    public void changeRole(Role role) {
        this.role = role;
        incrementTokenVersion();
    }
}
