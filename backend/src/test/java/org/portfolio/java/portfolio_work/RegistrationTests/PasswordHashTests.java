package org.portfolio.java.portfolio_work.RegistrationTests;

import org.junit.jupiter.api.Test;
import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Registration.Requests.CreateUserRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordHashTests {

    @Test
    void constructor_shouldHashPassword() {
        // Arrange
        String rawPassword = "Password123!";

        CreateUserRequest request = new CreateUserRequest(
                "Martin",
                "Fulop",
                "martin@example.com",
                rawPassword
        );

        // Act
        User user = new User(request);

        // Assert
        assertThat(user.getPassword())
                .isNotBlank()
                .isNotEqualTo(rawPassword);

        assertThat(
                BCrypt.checkpw(rawPassword, user.getPassword())
        ).isTrue();
    }

    @Test
    void constructor_shouldCopyRequestFields() {
        CreateUserRequest request = new CreateUserRequest(
                "Martin",
                "Fulop",
                "martin@example.com",
                "Password123!"
        );

        User user = new User(request);

        assertThat(user.getFirstName()).isEqualTo("Martin");
        assertThat(user.getLastName()).isEqualTo("Fulop");
        assertThat(user.getEmail()).isEqualTo("martin@example.com");
    }
}