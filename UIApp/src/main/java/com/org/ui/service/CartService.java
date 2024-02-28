package com.org.ui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.ui.entity.Cart;
import com.org.ui.repository.CartRepository;

@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepo;

	public void save(Cart cart) {
		cartRepo.save(cart);
	}

	public List<Cart> getAllItems() {
		return cartRepo.findAll();
	}

	public void deleteItemById(int id) {
		cartRepo.deleteById(id);
	}

	public List<Cart> getItemsByCustomerId(Integer id) {
		return cartRepo.findItemsByCustomerId(id);
	}

}
