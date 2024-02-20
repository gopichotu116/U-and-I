package com.org.ui.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.org.ui.entity.Customer;
import com.org.ui.entity.Items;
import com.org.ui.entity.Vendor;
import com.org.ui.service.CustomerService;
import com.org.ui.service.EmailService;
import com.org.ui.service.ItemService;
import com.org.ui.service.VendorService;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService custService;

	@Autowired
	private VendorService venService;

	@Autowired
	private ItemService itemService;

	private String userName;

	byte[] profileImage;

	private final EmailService emailService;

	public CustomerController(EmailService emailService) {
		this.emailService = emailService;
	}

	/*------------------------------ SignUp ------------------------------- */

	@GetMapping("/customer")
	public String customer(Customer customer) {
		return "customer/customer_signup";
	}

	@PostMapping("/signup")
	public String signup(@RequestParam("file") MultipartFile file, Customer customer) throws IOException {
		if (!file.isEmpty()) {
			customer.setProfileImage(file.getBytes());
		}
		if (passValidation(customer.getPassword())) {
			custService.save(customer);
			emailService.sendSignupConfirmationEmail(customer.getEmail(), customer.getName());
			return "customer/cust_signupwel";
		}
		return "customer/cust_signupfail";
	}

	@PostMapping("/cust_signupwel")
	public String custSignup(@RequestParam("file") MultipartFile file, Customer customer) throws IOException {
		if (!file.isEmpty()) {
			customer.setProfileImage(file.getBytes());
		}
		if (passValidation(customer.getPassword())) {
			custService.save(customer);
			emailService.sendSignupConfirmationEmail(customer.getEmail(), customer.getName());
			return "customer/cust_signupwel";
		}
		return "customer/cust_signupfail";

	}

	/*----------------------------- Login ---------------------------------- */

	@GetMapping("/customer_login")
	public String cus_login() {
		return "customer/customer_login";
	}

	@PostMapping("/cust_loginwel")
	public String loginwell(@RequestParam("email") String email, @RequestParam("pswd") String password, Model model) {

		Customer customer = custService.checkUserandPass(email, password);
		if (customer != null) {
			profileImage = custService.getProfileImage(customer.getId());
			model.addAttribute("profileImage",
					profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
			userName = customer.getEmail();
			model.addAttribute("name", userName);
			model.addAttribute("customer", customer);
			return "customer/cust_home";
		}
		return "/customer/cust_loginfail";
	}

	/*----------------------------- Customer Home ---------------------------------- */
	@GetMapping("/cust_home")
	public String customerHome(Model model) {
		model.addAttribute("profileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		model.addAttribute("name", userName);
		return "/customer/cust_home";
	}

	@GetMapping("/custAboutUs")
	public String aboutUs(Model model) {
		model.addAttribute("profileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		model.addAttribute("name", userName);
		return "customer/aboutUs";
	}

	/*----------------------------- Search Location ---------------------------------- */

	@PostMapping("/searchLoc")
	public String getShopNamesByLoc(@RequestParam("loc") String loc, Model model) {
		List<Vendor> vendorsList = venService.getVendorByLoc(loc);
		if (!vendorsList.isEmpty()) {
			model.addAttribute("vendors", vendorsList);
			model.addAttribute("profileImage",
					profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
			model.addAttribute("name", userName);

			return "customer/shopsList2";
		}

		return "customer/noShops";
	}

	/*----------------------------- Search Items ---------------------------------- */
	
	@GetMapping("/shopItems/{id}")
	public String getShopItems(@PathVariable("id") int id, Model model) {
		List<Items> itemsList = itemService.getItemsByVendorShopName(id);
		System.out.println(itemsList);
		model.addAttribute("shopItemsList", itemsList);
		model.addAttribute("profileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		model.addAttribute("name", userName);
		return "customer/shopItemsList";
	}

	@PostMapping("/searchItemInShop")
	public String searchItemInShop(@RequestParam("value") String value, Model model) {
		List<Items> itemsList = itemService.getListByItem(value);
//		itemService.getItmesListByVendorShopName(value);
		model.addAttribute("items", itemsList);
		
		model.addAttribute("profileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		model.addAttribute("name", userName);
		return "customer/searchShopItems";
	}

	/*----------------------------- Customer Search Item ---------------------------------- */

	@PostMapping("/custSearchItem")
	public String customerSearchItem(@RequestParam("item") String item, Model model) {
		List<Items> itemsList = itemService.getListByItem(item);
		model.addAttribute("items", itemsList);
		model.addAttribute("profileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		model.addAttribute("name", userName);
		return "customer/searchItem";
	}

	/*----------------------------- Customer Profile ---------------------------------- */

	@GetMapping("/custProfile")
	public String customerProfile(Model model) {

		Customer customer = custService.getCustomerByEmail(userName);
		model.addAttribute("customerProfile", customer);
		profileImage = custService.getProfileImage(customer.getId());
		model.addAttribute("profileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		model.addAttribute("name", userName);
		return "customer/customerProfile";
	}

	@GetMapping("/editCustomerProfile/{id}")
	public String editCustomer(@PathVariable("id") int id, Model model) {
		Customer customer = custService.getCustomerById(id);
		model.addAttribute("custProfile", customer);
		model.addAttribute("profileImage",
				profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
		model.addAttribute("name", userName);
		return "customer/custEditProfile";
	}

	@PostMapping("/saveCustomer")
	public String updateCustomer(@ModelAttribute Customer customer, @RequestParam("file") MultipartFile file,
			Model model) throws IOException {
		model.addAttribute("custProfile", customer);
		if (passValidation(customer.getPassword())) {
			customer.setProfileImage(file.getBytes());
			profileImage = custService.getProfileImage(customer.getId());
			model.addAttribute("profileImage",
					profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
			model.addAttribute("name", userName);
			custService.updateVendor(customer);
			return "redirect:/custProfile";
		}
		return "customer/invalidPassword";
	}

	@PostMapping("/custChangePass")
	public String changePassword(@RequestParam("email") String email, Model model) {
		Customer customer = custService.getCustomerByEmail(email);
		if (customer == null)
			return "customer/invalidEmail";
		else {
			model.addAttribute("profileImage",
					profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
			model.addAttribute("name", userName);
			model.addAttribute("password", customer.getPassword());

			return "customer/showPassword";
		}
	}

	@GetMapping("/custForgetPassword")
	public String forgetPass() {
		return "/customer/forgetPassword";
	}

	/*----------------------------- Password Validation ---------------------------------- */

	public boolean passValidation(String pass) {
		String regexp = "(?=.*[A-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{5,16}";
		Matcher m = Pattern.compile(regexp).matcher(pass);
		return m.matches();

	}

}
