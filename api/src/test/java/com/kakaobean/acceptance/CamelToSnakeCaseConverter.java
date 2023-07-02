package com.kakaobean.acceptance;

public class CamelToSnakeCaseConverter {
    public static String convert(String camelCase) {
        StringBuilder snakeCase = new StringBuilder();

        for (int i = 0; i < camelCase.length(); i++) {

            char currentChar = camelCase.charAt(i);

            // 대문자인 경우 앞에 언더스코어(_)를 추가하여 소문자로 변환
            if (Character.isUpperCase(currentChar) && i != 0) {
                snakeCase.append("_").append(Character.toLowerCase(currentChar));
            } else {
                snakeCase.append(Character.toLowerCase(currentChar));
            }
        }

        return snakeCase.toString();
    }
}
