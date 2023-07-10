package com.kakaobean.unit.controller.common;

import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ImageControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    @DisplayName("이미지 업로드 성공 테스트")
    void upload_image() throws Exception {

        String url = "https://bucket.s3.ap-northeast-5.amazonaws.com/8d78cf624c99-%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%851%85%AE%206.23.05.png";

        //given
        MockMultipartFile images = new MockMultipartFile(
                "image", //requestParam
                "hello.txt", //original file name
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        given(imageService.upload(Mockito.any(MultipartFile.class))).willReturn(url);

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/images")
                        .file(images)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andDo(document("upload_image",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("image").description("The uploaded file")
                        ),
                        responseFields(
                                fieldWithPath("imageUrl").type(STRING).description("업로드한 이미지의 URL.")
                        )
                ));
    }
}