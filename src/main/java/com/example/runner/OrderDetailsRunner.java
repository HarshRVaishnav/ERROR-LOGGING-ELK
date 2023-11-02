package com.example.runner;

import com.example.entity.OrderDetails;
import com.example.repository.IOrderDetailsRepo;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@org.springframework.core.annotation.Order(20)
public class OrderDetailsRunner implements CommandLineRunner {

    @Autowired
    private IOrderDetailsRepo orderDetailsRepo;

    @Override
    public void run(String... args) throws Exception {
        if (orderDetailsRepo.count() == 0) {
            for (int i = 1; i < 5; i++) {
                Faker faker = new Faker(new Locale("en-IND"));
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderNumber(i);
                orderDetails.setQuantityOrdered(faker.number().numberBetween(1, 2));
                orderDetails.setProductCode(2);
                orderDetails.setPriceEach(faker.number().randomDouble(2, 100, 1000));
                orderDetailsRepo.save(orderDetails);
            }
        }
    }
}
