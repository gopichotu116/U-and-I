package com.org.ui.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, unique = true)
	private String password;
	
	@Column(nullable = false)
	private String phno;
	
	@Column(nullable = false)
	private String loc;
	
	@Lob
	private byte[] profileImage;

	public Customer(String name, String email, String password, String phno, String loc, byte[] profileImage) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phno = phno;
		this.loc = loc;
		this.profileImage = profileImage;
	}

}
