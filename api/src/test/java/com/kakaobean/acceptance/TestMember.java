package com.kakaobean.acceptance;


public enum TestMember {

    MEMBER("member@gmail.com", "1q2w3e4r!"),
    TESTER("test@gmail.com", "1q2w3e4r!!");

    private String email;
    private String password;

    TestMember(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
