package com.org.ui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.ui.entity.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {

	Vendor findVendorByEmailAndPassword(String email, String password);

	Vendor findVendorByEmail(String userName);

	@Query("SELECT v.shopName FROM Vendor v WHERE v.loc LIKE %:loc%")
	List<String> findShopNamesByShopNameContaining(String loc);

	@Query("SELECT v FROM Vendor v WHERE v.loc LIKE %:loc%")
	List<Vendor> findVendorByLoc(String loc);

	String findMapsByEmail(String email);

}
