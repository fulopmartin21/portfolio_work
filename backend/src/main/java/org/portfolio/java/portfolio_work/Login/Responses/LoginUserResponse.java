package org.portfolio.java.portfolio_work.Login.Responses;

import org.portfolio.java.portfolio_work.Entities.User;

import java.util.UUID;

public record LoginUserResponse(
        UUID id,
        String email
) {

    public LoginUserResponse(User user) {
        this(
                user.getId(),
                user.getEmail()
        );
    }
}