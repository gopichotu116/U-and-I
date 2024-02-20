package com.org.ui.service;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.ui.entity.Items;
import com.org.ui.entity.Vendor;
import com.org.ui.repository.ItemsRespository;

@Service
public class ItemService {

	@Autowired
	private ItemsRespository itemRepo;

	public void save(String name, double price, String quantity, Vendor vendor) {

		Items items = new Items(name, price, quantity, vendor);
		itemRepo.save(items);
	}

	public Items getItemById(int id) {
		return itemRepo.findItemById(id);
	}

	public void deleteItemById(int id) {
		itemRepo.deleteById(id);
	}

	public List<Items> getAllItems() {
		return itemRepo.findAll();
	}

	public List<Items> getItemsByVendorId(Integer id) {
		return itemRepo.findItemsByVendorId(id);
	}

	public List<Items> findItemByVendorIdAndValue(Integer id, String value) {
		return itemRepo.findItemByVendorIdAndValue(id, value);
	}

	public List<Items> getListByItem(String item) {
		return itemRepo.findItemByItemName(item);
	}

	public List<Items> getItemsByVendorShopName(int id) {
		return itemRepo.findItemsByVendorShopName(id);
	}

	public List<Items> getItemsByVendorLoc(String loc) {
		return itemRepo.finItemsByVendorLoc(loc);
	}


}
