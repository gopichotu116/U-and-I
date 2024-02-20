package com.org.ui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.org.ui.entity.Items;

@Repository
public interface ItemsRespository extends JpaRepository<Items, Integer> {

	Items findItemById(int id);

	List<Items> findItemsByVendorId(Integer id);

	@Query("SELECT i FROM Items i WHERE i.vendor.id = :id AND i.name LIKE %:value%")
	List<Items> findItemByVendorIdAndValue(Integer id, String value);

	@Query("SELECT i FROM Items i WHERE i.name LIKE %:item% ORDER BY i.price")
	List<Items> findItemByItemName(String item);

	@Query("SELECT i FROM Items i WHERE i.vendor.id=:id")
	List<Items> findItemsByVendorShopName(int id);

	@Query("SELECT i FROM Items i WHERE i.vendor.loc=:loc")
	List<Items> finItemsByVendorLoc(String loc);
	

}
