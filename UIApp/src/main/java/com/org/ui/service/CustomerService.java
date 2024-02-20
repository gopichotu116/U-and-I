package com.org.ui.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.org.ui.entity.Customer;
import com.org.ui.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository custRepo;

	public byte[] getProfileImage(int customerId) {
        Optional<Customer> optionalCustomer =custRepo.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            return customer.getProfileImage();
        } else {
            return null;
        }
    }

	public Customer checkUserandPass(String email, String password) {
		return custRepo.findByEmailAndPassword(email,password);
	}

	public void save(Customer cust) {
		custRepo.save(cust);
	}

	public Customer getCustomerByEmail(String userName) {
		return custRepo.findByEmail(userName);
	}

	public Customer getCustomerById(int id) {
		return custRepo.findById(id).get();
	}

	public void updateVendor(Customer customer) {
		custRepo.save(customer);
	}

	

}
