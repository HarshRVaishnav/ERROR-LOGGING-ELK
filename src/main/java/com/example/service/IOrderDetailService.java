package com.example.service;

import com.example.entity.OrderDetails;

import java.util.List;

public interface IOrderDetailService {

    public void save(List<OrderDetails> orderDetailsList);

}
