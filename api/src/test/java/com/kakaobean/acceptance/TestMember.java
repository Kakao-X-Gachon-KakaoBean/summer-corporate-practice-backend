package com.kakaobean.acceptance;


public enum TestMember {

    TESTER("tester@gmail.com", "1q2w3e4r!"),
    ADMIN("asb1651@gachon.ac.kr", "1q2w3e4r!"),
    MEMBER("asb0711@gmail.com", "1q2w3e4r!"),
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
