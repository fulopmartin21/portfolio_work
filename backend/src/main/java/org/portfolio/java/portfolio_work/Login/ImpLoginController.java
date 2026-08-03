package org.portfolio.java.portfolio_work.Login;

import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.portfolio.java.portfolio_work.Login.Responses.LoginResponse;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImpLoginController implements ILoginController {

    private final ILoginService loginService;

    public ImpLoginController(
            ILoginService loginService
    ) {
        this.loginService = loginService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return loginService.login(request);
    }
}