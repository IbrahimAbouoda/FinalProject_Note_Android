package com.example.staynoted.listeners;

public interface LoginStatusListener {
    final static int LOGIN = 1;
    final static int SIGNUP = 2;

    void onLoginStatusChanged(int statusCode);
}
