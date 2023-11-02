package com.example.batchinsert;
import com.example.ErrorLoggingApplication;

import static com.example.batchinsert.TestObjectHelper.createOrder;
import com.example.entity.Order;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErrorLoggingApplication.class)
@Transactional
@ActiveProfiles("batchinserts")
@TestPropertySource(properties = "spring.jpa.properties.hibernate.jdbc.batch_size=-1")
public class JpaNoBatchInsertsIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void whenNotConfigured_ThenSendsInsertsSeparately() {
        for (int i = 0; i < 10; i++) {
            Order school = createOrder(i);
            entityManager.persist(school);
        }
    }

    @After
    public void tearDown() {
        entityManager.flush();
    }
}
