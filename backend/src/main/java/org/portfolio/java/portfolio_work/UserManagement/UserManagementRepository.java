package org.portfolio.java.portfolio_work.UserManagement;

import org.portfolio.java.portfolio_work.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserManagementRepository extends JpaRepository<User, UUID> {

    boolean existsByEmailIgnoreCase(String email);
}
