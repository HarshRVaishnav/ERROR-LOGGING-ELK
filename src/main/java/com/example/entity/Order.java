package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "order_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer orderNumber;

	private LocalDate orderDate;

	private LocalDate shippedDate;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Status cannot be null")
	private Status status;

	@Size(max = 500, message = "Comments cannot be more than 500 characters")
	private String comments;

	private Integer customerNumber;

	//@JsonIgnore
	@ManyToOne(cascade ={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
	@JoinColumn(name = "fk_customerNumber")
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<OrderDetails> orderDetails;

}

















