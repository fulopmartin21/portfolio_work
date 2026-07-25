package org.portfolio.java.portfolio_work.Registration;

import org.portfolio.java.portfolio_work.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<User, UUID>
{

    public boolean existsByEmailIgnoreCase(String email);
}
