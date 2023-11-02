package com.example.runner;

import com.example.entity.Order;
import com.example.entity.Status;
import com.example.repository.IOrderRepo;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@org.springframework.core.annotation.Order(10)
@Component
public class OrderRunner implements CommandLineRunner {

    @Autowired
    private IOrderRepo orderRepo;

    @Override
    public void run(String... args) throws Exception {
        if (orderRepo.count() == 0) {
            for (int i = 1; i < 5; i++) {
                Faker faker = new Faker(new Locale("en-IND"));
                Order order = new Order();
                order.setOrderDate(LocalDate.now());
                order.setComments(faker.lorem().sentence());
                order.setStatus(Status.SHIPPED);
                order.setCustomerNumber(faker.number().randomDigit());
                order.setShippedDate(LocalDate.now().plusDays(2));
                order.setOrderDetails(List.of());
                orderRepo.save(order);
            }
        }
    }
}
