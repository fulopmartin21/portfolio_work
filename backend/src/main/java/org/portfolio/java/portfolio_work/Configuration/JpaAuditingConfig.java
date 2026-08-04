package org.portfolio.java.portfolio_work.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(
        auditorAwareRef = "auditorAware"
)
public class JpaAuditingConfig {
}