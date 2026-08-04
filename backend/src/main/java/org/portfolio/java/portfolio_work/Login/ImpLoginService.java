package org.portfolio.java.portfolio_work.Login;

import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedException;
import org.portfolio.java.portfolio_work.Exceptions.UnauthorizedException.UnauthorizedExceptionSubType;
import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.portfolio.java.portfolio_work.Login.Responses.LoginResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImpLoginService implements ILoginService {

    private static final String TOKEN_TYPE = "Bearer";

    private final LoginRepository loginRepository;
    private final IJwtTokenService jwtTokenService;

    public ImpLoginService(
            LoginRepository loginRepository,
            IJwtTokenService jwtTokenService
    ) {
        this.loginRepository = loginRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = loginRepository
                .findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException(
                                UnauthorizedExceptionSubType.INVALID_CREDENTIALS
                        )
                );

        boolean passwordMatches = BCrypt.checkpw(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new UnauthorizedException(
                    UnauthorizedExceptionSubType.INVALID_CREDENTIALS
            );
        }

        String accessToken =
                jwtTokenService.generateAccessToken(user);

        return new LoginResponse(
                accessToken,
                TOKEN_TYPE,
                jwtTokenService.getExpirationSeconds()
        );
    }
}