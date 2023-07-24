package com.kakaobean.acceptance;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQCleaner {

    @Autowired
    protected AmqpAdmin amqpAdmin;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    public void resetRabbitMq() {
        for (Member m : memberRepository.findAll()) {
            amqpAdmin.deleteQueue("user-" + m.getId());
            amqpAdmin.deleteExchange("user-" + m.getId());
        }

        for (Project project : projectRepository.findAll()) {
            amqpAdmin.deleteExchange("project-" + project.getId());
        }
    }
}
