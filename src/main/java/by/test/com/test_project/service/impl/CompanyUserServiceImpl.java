package by.test.com.test_project.service.impl;

import by.test.com.test_project.model.entity.CompanyUser;
import by.test.com.test_project.model.repository.CompanyUserRepository;
import by.test.com.test_project.service.CompanyUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyUserServiceImpl implements CompanyUserService {

    private final CompanyUserRepository companyUserRepository;

    @Override
    public CompanyUser createUser(String login) {
        CompanyUser user = new CompanyUser();
        user.setLogin(login);
        return companyUserRepository.save(user);
    }

    @Override
    public CompanyUser findByLogin(String login) {
        return companyUserRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found for login: " + login));
    }

}
