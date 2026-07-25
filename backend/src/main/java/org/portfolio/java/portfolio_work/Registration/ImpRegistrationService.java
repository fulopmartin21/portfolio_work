package org.portfolio.java.portfolio_work.Registration;

import org.portfolio.java.portfolio_work.Entities.User;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictException;
import org.portfolio.java.portfolio_work.Exceptions.ConflictException.ConflictExceptionSubType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpRegistrationService implements IRegistrationService
{
    private final RegistrationRepository registrationRepository;

    @Autowired
    public ImpRegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Override
    public User createUser(User model)
    {
        if (registrationRepository.existsByEmailIgnoreCase(model.getEmail()))
        {
            throw new ConflictException(ConflictExceptionSubType.EMAIL_ALREADY_EXISTS);
        }
        return registrationRepository.save(model);
    }
}
