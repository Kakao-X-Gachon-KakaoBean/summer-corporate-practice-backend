package com.kakaobean.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CommandSuccessResponse {

    private String message;

    public CommandSuccessResponse() {};

    public CommandSuccessResponse(String message) {
        this.message = message;
    }

    public static CommandSuccessResponse from(String message){
        return new CommandSuccessResponse(message);
    }


    public static CommandSuccessResponse.Created from(Long id, String message){
        return new CommandSuccessResponse.Created(id, message);
    }

    @Getter
    @NoArgsConstructor
    public static class Created {

        private Long id;
        private String message;

        public Created(Long id, String message) {
            this.id = id;
            this.message = message;
        }
    }
}
