package org.portfolio.java.portfolio_work.Registration.Requests;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest
{
    @Length(min=5, max=100,message = "The first name should be at least 5 characters")
    private String firstName;
    @Length(min=5, max=100, message = "The last name should be at least 5 characters")
    private String lastName;
    @Email
    @Length(min=5, max=100, message = "The email address should be unique and at least 5 characters")
    private String email;
    @Length(min=8, max=20, message = "Your password has to be at least 8 characters long and contain at least 1 number and special symbol")
    private String password;
}
