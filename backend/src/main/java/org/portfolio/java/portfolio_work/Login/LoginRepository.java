package org.portfolio.java.portfolio_work.Login;

import org.portfolio.java.portfolio_work.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoginRepository extends JpaRepository<User, UUID>
{
    Optional<User> findByEmailIgnoreCase(String email);
}
