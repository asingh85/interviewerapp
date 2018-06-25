package com.softvision.service;

public interface LoginService<T> {

    T register(T login);

    T login(String userName, String password);

}
