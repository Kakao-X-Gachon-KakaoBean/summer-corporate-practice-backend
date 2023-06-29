package com.kakaobean.acceptance;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;
    private List<String> entities;
    private List<String> tables = new ArrayList<>();


    private List<String> filters = List.of(
            "essayquestionresponse",
            "multiplechoicequestionresponse",
            "rangequestionresponse",
            "essayquestion",
            "multiplechoicequestion",
            "rangequestion",
            "multiplechoiceanswerresponse"
    );

    @Override
    public void afterPropertiesSet() throws Exception {
        entities = entityManager.getMetamodel().getEntities().stream()
                .map(entry -> entry.getName().toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());
        for (String str : entities) {
            if (!filters.contains(str)) {
                tables.add(str);
            }
        }
        System.out.println("tables = " + tables);
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET foreign_key_checks = 0").executeUpdate();

        for (String table : tables) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + table + " AUTO_INCREMENT = 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET foreign_key_checks = 1").executeUpdate();
    }
}