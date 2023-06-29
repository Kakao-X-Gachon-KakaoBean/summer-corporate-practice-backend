package com.kakaobean.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class FindEmailRequest {

    private String name;
    private LocalDate birth;

    public FindEmailRequest(String name, LocalDate birth) {
        this.name = name;
        this.birth = birth;
    }
}
