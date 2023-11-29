package com.kakaobean.acceptance.member;

import com.kakaobean.acceptance.AcceptanceTest;
import com.kakaobean.acceptance.member.MemberAcceptanceTask;
import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.fixture.member.MemberFactory;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.fixture.member.RegisterMemberRequestFactory;

import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.QueueInformation;

import static org.assertj.core.api.Assertions.*;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void registerMember(){

        //given
        Member member = MemberFactory.createAdminWithTempEmail();
        RegisterMemberRequest request = RegisterMemberRequestFactory.createMember(member);

        //when
        ExtractableResponse response = MemberAcceptanceTask.registerMemberTask(request, emailRepository);
        CommandSuccessResponse.Created createdMember = response.as(CommandSuccessResponse.Created.class);

        //given
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(getMemberQueueInfo(createdMember.getId())).isNotNull();
    }

    private QueueInformation getMemberQueueInfo(Long memberId) {
        return amqpAdmin.getQueueInfo("user-" + memberId);
    }
}
