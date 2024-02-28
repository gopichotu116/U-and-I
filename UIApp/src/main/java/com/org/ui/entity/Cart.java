package com.org.ui.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private double price;
	
	@Column(nullable = false)
	private String quantity;
	
	@JoinColumn(name="c_id")
	@ManyToOne
	@Cascade(CascadeType.MERGE)
	private Customer customer;

	public Cart(String name, double price, String quantity, Customer customer) {
		super();
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.customer = customer;
	}
}
