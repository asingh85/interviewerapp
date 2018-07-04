package com.softvision.service;

import java.util.List;

public interface LoginService<T> {

    T register(T login);

    T login(String userName, String password);

    List<T> getAll();

}
