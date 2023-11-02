package com.example.batchinsert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Order;
import com.example.entity.OrderDetails;
import com.example.ErrorLoggingApplication;

import java.util.List;

import static com.example.batchinsert.TestObjectHelper.createOrder;
import static com.example.batchinsert.TestObjectHelper.createOrderDetails;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErrorLoggingApplication.class)
@Transactional
@ActiveProfiles("batchinserts")
public class JpaBatchInsertsIntegrationTest {

    @PersistenceContext
    private EntityManager entityManager;

    private static final int BATCH_SIZE = 5;

    @Transactional
    @Test
    public void whenInsertingSingleTypeOfEntity_thenCreatesSingleBatch() {
        for (int i = 0; i < 10; i++) {
            Order Order = createOrder(i);
            entityManager.persist(Order);
        }
    }

    @Transactional
    @Test
    public void whenFlushingAfterBatch_ThenClearsMemory() {
        for (int i = 0; i < 10; i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            Order Order = createOrder(i);
            entityManager.persist(Order);
        }
    }

    @Transactional
    @Test
    public void whenThereAreMultipleEntities_ThenCreatesNewBatch() {
        for (int i = 0; i < 10; i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            Order Order = createOrder(i);
            entityManager.persist(Order);
            OrderDetails firstOrderDetails = createOrderDetails(Order);
            OrderDetails secondOrderDetails = createOrderDetails(Order);
            entityManager.persist(firstOrderDetails);
            entityManager.persist(secondOrderDetails);
        }
    }

    @Transactional
    @Test
    public void whenUpdatingEntities_thenCreatesBatch() {
        for (int i = 0; i < 10; i++) {
            Order Order = createOrder(i);
            entityManager.persist(Order);
        }
        entityManager.flush();
        TypedQuery<Order> OrderQuery = entityManager.createQuery("SELECT s from Order s", Order.class);
        List<Order> allOrders = OrderQuery.getResultList();
        for (Order Order : allOrders) {
            Order.setCustomerNumber(2);
        }
    }

    @After
    public void tearDown() {
        entityManager.flush();
    }
}