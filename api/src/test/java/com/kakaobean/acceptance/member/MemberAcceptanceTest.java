package com.kakaobean.acceptance.member;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.TestMember;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;

import io.restassured.response.ExtractableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    void registerMember(){

        //given
        RegisterMemberRequest request = RegisterMemberRequestFactory.createRequestV3();

        //when
        ExtractableResponse response = MemberAcceptanceTask.registerMemberTask(request, emailRepository);

        //given
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(getMemberQueueInfo()).isNotNull();
    }

    private QueueInformation getMemberQueueInfo() {
        Member member = memberRepository.findMemberByEmail(TestMember.TESTER.getEmail()).get();
        return amqpAdmin.getQueueInfo("user-" + member.getId());
    }
}
