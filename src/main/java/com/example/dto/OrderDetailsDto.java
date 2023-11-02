package com.example.dto;

import lombok.Data;

@Data
public class OrderDetailsDto  {

    private Integer orderId;

    private Integer orderNumber;

    private Integer productCode;

    private Integer quantityOrdered;

    private Double priceEach;

}
