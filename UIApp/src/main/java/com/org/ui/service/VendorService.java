package com.org.ui.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.ui.entity.Vendor;
import com.org.ui.repository.VendorRepository;

@Service
public class VendorService {

	@Autowired
	private VendorRepository venRepo;
	
	public void save(Vendor vendor) {
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


	public byte[] getProfileImage(Integer id) {
		Optional<Vendor> optionalVendor = venRepo.findById(id);
		if(optionalVendor.isPresent()) {
			Vendor vendor = optionalVendor.get();
			return vendor.getProfileImage();
		}
		return null;
	}



}
