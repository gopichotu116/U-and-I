package com.org.ui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.ui.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{

	@Query("SELECT c FROM Cart c WHERE c.customer.id=:id")
	List<Cart> findItemsByCustomerId(Integer id);

	@Query("SELECT c FROM Cart c WHERE c.customer.id=:id AND c.name LIKE %:value%")
	List<Cart> findItemsByCustomerIdAndValue(Integer id, String value);

	@Query("SELECT c FROM Cart c WHERE c.shopName=:shopName AND c.name=:name")
	List<Cart> findItemByShopNameAndItemName(String shopName, String name);

}
