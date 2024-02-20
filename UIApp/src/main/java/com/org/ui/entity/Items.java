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
public class Items {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private double price;
	
	@Column(nullable = false)
	private String quantity;
	
	@ManyToOne
	@Cascade(CascadeType.MERGE)
	@JoinColumn(name = "v_id")
	private Vendor vendor;

	public Items(String name, double price, String quantity, Vendor vendor) {
		super();
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.vendor = vendor;
	}
	
	
	
}
