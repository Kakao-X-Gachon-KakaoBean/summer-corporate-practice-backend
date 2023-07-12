package com.kakaobean.acceptance;


public enum TestMember {

    TESTER("tester@gmail.com", "1q2w3e4r!"),
    ADMIN("dlsdlaqja888@gmail.com", "1q2w3e4r!"),
    MEMBER("dladlsqja@naver.com", "1q2w3e4r!"),
    SENDER("j949854@gmail.com", "1q2w3e4r!"),
    RECEIVER("trillion32@naver.com", "1q2w3e4r!");


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
