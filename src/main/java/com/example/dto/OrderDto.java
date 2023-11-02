package com.example.dto;

import com.example.entity.Customer;
import com.example.entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data

public class OrderDto  {

    private Integer orderNumber;

    private String comments;

    private Integer customerNumber;

    private List<OrderDetailsDto> orderDetails;

}
