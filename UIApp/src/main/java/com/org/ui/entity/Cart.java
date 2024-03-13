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
	
	@Column(nullable = false, name="shop_name")
	private String shopName;
	
	@Column(nullable = false)
	private String maps;
	
	@JoinColumn(name="c_id")
	@ManyToOne
	@Cascade(CascadeType.MERGE)
	private Customer customer;

	public Cart(int id, String name, double price, String quantity, String shopName, String maps, Customer customer) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.shopName = shopName;
		this.maps = maps;
		this.customer = customer;
	}

	public Cart(String name, double price, String quantity, String shopName, String maps, Customer customer) {
		super();
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.shopName = shopName;
		this.maps = maps;
		this.customer = customer;
	}

	
}
