package org.portfolio.java.portfolio_work.Registration.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.portfolio.java.portfolio_work.Entities.User;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse
{
    private UUID id;

    public CreateUserResponse(User user)
    {
        this.id = user.getId();
    }
}
