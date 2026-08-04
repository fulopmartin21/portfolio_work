package org.portfolio.java.portfolio_work.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.portfolio.java.portfolio_work.Registration.Requests.CreateUserRequest;
import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;

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

    public User (CreateUserRequest request)
    {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.email = request.getEmail();
        this.password = passwordEncryptor(request.getPassword());
    }

    public User (LoginRequest request)
    {
        this.email = request.getEmail();
        this.password = request.getPassword();
    }

    private String passwordEncryptor(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
