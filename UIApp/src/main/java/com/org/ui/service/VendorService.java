package com.org.ui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.ui.entity.Vendor;
import com.org.ui.repository.VendorRepository;

@Service
public class VendorService {

	@Autowired
	private VendorRepository venRepo;
	

	public void saveVendor(String name, String email, String password, String phoneNum, String shopName, String loc, String maps) {

		Vendor vendor = new Vendor(name, email, password, phoneNum, shopName, loc, maps);
		venRepo.save(vendor);

	}


	public Vendor checkUserandPass(String email, String password) {
		return venRepo.findVendorByEmailAndPassword(email,password);
		
	}


	public Vendor getVendorByEmail(String userName) {
		return venRepo.findVendorByEmail(userName);
	}


	public Vendor getVendorById(int id) {
		return venRepo.findById(id).get();
	}


	public void updateVendor(Vendor vendor) {
		venRepo.save(vendor);
	}


	public List<Vendor> getVendorByLoc(String loc) {
		return venRepo.findVendorByLoc(loc);
	}

}
