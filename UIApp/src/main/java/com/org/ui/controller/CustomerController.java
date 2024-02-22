package com.org.ui.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.org.ui.entity.Customer;
import com.org.ui.entity.Items;
import com.org.ui.entity.Vendor;
import com.org.ui.service.CustomerService;
import com.org.ui.service.EmailService;
import com.org.ui.service.ItemService;
import com.org.ui.service.VendorService;

@ControllerAdvice
@Controller
public class CustomerController {

	@Autowired
	private CustomerService custService;

	@Autowired
	private VendorService venService;

	@Autowired
	private ItemService itemService;

	private String userName;
	
	private String name;

	byte[] profileImage;

	private final EmailService emailService;

	public CustomerController(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@ModelAttribute("name")
    public String addNameAttribute() {
        String userName = name;
        return userName;
    }
	
	@ModelAttribute("profileImage")
	public String addImageAttribute() {
		return profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "";
	}

	/*------------------------------ SignUp ------------------------------- */

	@GetMapping("/customer")
	public String customer(Customer customer) {
		return "customer/customerSignup";
	}

	@PostMapping("/cust_signupwel")
	public String custSignup(@RequestParam("file") MultipartFile file, Customer customer, Model model) throws IOException {
		if (!file.isEmpty()) {
			customer.setProfileImage(file.getBytes());
		}
		if (passValidation(customer.getPassword())) {
			custService.save(customer);
			emailService.sendSignupConfirmationEmail(customer.getEmail(), customer.getName());
			model.addAttribute("loginSuccess", "Your account is created");
			return "customer/customerLogin";
		}
		return "redirect:/passwordRequirements";

	}
	
	
	/*----------------------------- Login ---------------------------------- */

	@GetMapping("/customer_login")
	public String cus_login() {
		return "customer/customerLogin";
	}

	@PostMapping("/cust_loginwel")
	public String loginwell(@RequestParam("email") String email, @RequestParam("pswd") String password, Model model) {

		Customer customer = custService.checkUserandPass(email, password);
		if (customer != null) {
			profileImage = custService.getProfileImage(customer.getId());
			model.addAttribute("profileImage",
					profileImage != null ? Base64.getEncoder().encodeToString(profileImage) : "");
			userName = customer.getEmail();
			name=customer.getName();
			model.addAttribute("name", name);
			
			model.addAttribute("loginSuccess", "Your account is created");
			model.addAttribute("customer", customer);
			return "customer/customerHome";
		}
		else {
			
			model.addAttribute("errorMsg", "Incorrect password/email");
			return "/customer/customerLogin";
			
		}
	}
	
	@GetMapping("/passwordRequirements")
	public String pswdReq() {
		return "/fragments/passwordRequirements";
	}

	/*----------------------------- Customer Home ---------------------------------- */
	@GetMapping("/cust_home")
	public String customerHome(Model model) {
		return "/customer/customerHome";
	}

	@GetMapping("/custAboutUs")
	public String aboutUs(Model model) {
		return "customer/aboutUs";
	}

	/*----------------------------- Search Location ---------------------------------- */

	@PostMapping("/searchLoc")
	public String getShopNamesByLoc(@RequestParam("loc") String loc, Model model) {
		List<Vendor> vendorsList = venService.getVendorByLoc(loc);
		if (!vendorsList.isEmpty()) {
			model.addAttribute("vendors", vendorsList);
			return "customer/shopsList2";
		}

		return "customer/noShops";
	}

	/*----------------------------- Search Items ---------------------------------- */
	
	int vendorId;
	
	@GetMapping("/shopItems/{id}")
	public String getShopItems(@PathVariable("id") int id, Model model) {
		vendorId=id;
		List<Items> itemsList = itemService.getItemsByVendorShopName(id);
		Vendor vendor = venService.getVendorById(id);
		model.addAttribute("vendor", vendor);
		model.addAttribute("url", vendor.getMaps());
		model.addAttribute("shopItemsList", itemsList);
		return "customer/shopItemsList";
	}
	@PostMapping("/searchItemInShop")
	public ModelAndView searchItemInShop(@RequestParam("value") String value, Model model) {
		List<Items> itemsList = itemService.getListByItem(value);
//		itemService.getItmesListByVendorShopName(value);
		Vendor vendor = venService.getVendorById(vendorId);
		model.addAttribute("vendor", vendor);
		List<Items> items = itemService.findItemByVendorIdAndValue(vendorId, value);
		System.out.println(items);
		return new ModelAndView("/customer/searchShopItems", "items", items);
//		return "customer/searchShopItems";
	}

	/*----------------------------- Customer Search Item ---------------------------------- */

	@PostMapping("/custSearchItem")
	public String customerSearchItem(@RequestParam("item") String item, Model model) {
		List<Items> itemsList = itemService.getListByItem(item);
		model.addAttribute("items", itemsList);
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
		return "customer/customerProfile";
	}

	@GetMapping("/editCustomerProfile/{id}")
	public String editCustomer(@PathVariable("id") int id, Model model) {
		Customer customer = custService.getCustomerById(id);
		model.addAttribute("custProfile", customer);
		
		return "customer/customerEditProfile";
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
			custService.updateVendor(customer);
			
			return "redirect:/custProfile";
		}
		return "redirect:/passwordRequirements";
	}

	@PostMapping("/custChangePass")
	public String changePassword(@RequestParam("email") String email, Model model) {
		Customer customer = custService.getCustomerByEmail(email);
		if (customer == null) {
			model.addAttribute("invalidEmail", "Invalid email address");
			return "customer/forgetPassword";
		}
		else {
			model.addAttribute("password", customer.getPassword());
			return "customer/forgetPassword";
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
