package by.test.com.test_project.service;

import by.test.com.test_project.model.entity.CompanyUser;

public interface CompanyUserService {

    CompanyUser createUser(String login);
    CompanyUser findByLogin(String login);
}
