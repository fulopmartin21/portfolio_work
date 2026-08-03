package org.portfolio.java.portfolio_work.Login;

import org.portfolio.java.portfolio_work.Entities.User;

public interface IJwtTokenService {

    String generateAccessToken(User user);

    long getExpirationSeconds();
}