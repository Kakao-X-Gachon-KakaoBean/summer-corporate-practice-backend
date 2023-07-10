package com.kakaobean.acceptance;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tables = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        tables = entityManager.getMetamodel().getEntities().stream()
                .map(entry -> CamelToSnakeCaseConverter.convert(entry.getName()))
                .collect(Collectors.toList());
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