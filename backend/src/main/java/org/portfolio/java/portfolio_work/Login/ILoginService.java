package org.portfolio.java.portfolio_work.Login;

import org.portfolio.java.portfolio_work.Login.Requests.LoginRequest;
import org.portfolio.java.portfolio_work.Login.Responses.LoginResponse;

public interface ILoginService {

    LoginResponse login(LoginRequest request);
}