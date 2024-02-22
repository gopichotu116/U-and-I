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
@Data
@NoArgsConstructor
public class Vendor {
	
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
	
	
	@Column(nullable = false, name="shop_name")
	private String shopName;
	
	@Column(nullable = false)
	private String loc;
	
	@Column(nullable = false)
	private String maps;
	
	@Lob
	private byte[] profileImage;

	public Vendor(String name, String email, String password, String phno, String shopName, String loc, String maps,
			byte[] profileImage) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phno = phno;
		this.shopName = shopName;
		this.loc = loc;
		this.maps = maps;
		this.profileImage = profileImage;
	}
	
	
}
