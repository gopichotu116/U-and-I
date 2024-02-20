package com.org.ui.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.org.ui.entity.Items;
import com.org.ui.entity.Vendor;
import com.org.ui.service.ItemService;
import com.org.ui.service.VendorService;

@Controller
public class VendorController {

	@Autowired
	private VendorService venService;

	@Autowired
	private ItemService itemService;

	private String userName;

	/*----------------------------- Vendor Home ---------------------------------- */

	@GetMapping("/vendor_home")
	public String vendorHome(Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		String maps = vendor.getMaps();
		model.addAttribute("map", maps);
		return "vendor/vendor_home";
	}
	
	@GetMapping("/venAboutUs")
	public String aboutUs() {
		return "vendor/aboutUs";
	}

	/*----------------------------- SignUp ---------------------------------- */

	@GetMapping("/vendor")
	public String vendor() {
		return "vendor/vendor_signup";
	}

	@PostMapping("/ven_signupwel")
	public String ven_signup(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("pswd") String password, @RequestParam("num") String phoneNum,
			@RequestParam("shopName") String shopName, @RequestParam("loc") String loc,
			@RequestParam("maps") String maps) {

		if (passValidation(password)) {
			venService.saveVendor(name, email, password, phoneNum, shopName, loc, maps);
			return "vendor/ven_signupwel";
		}
		return "vendor/ven_signupfail";

	}

	/*----------------------------- Login ---------------------------------- */

	@GetMapping("/vendor_login")
	public String ven_login() {
		return "vendor/vendor_login";
	}

	@PostMapping("/ven_loginwel")
	public String loginwell(@RequestParam("email") String email, @RequestParam("pswd") String password, Model model) {

		Vendor vendor = venService.checkUserandPass(email, password);
		if (vendor != null) {
			String maps = vendor.getMaps();
			model.addAttribute("map", maps);
			model.addAttribute("vendor", vendor);
			userName = vendor.getEmail();
			return "vendor/vendor_home";
		}
		return "vendor/ven_loginfail";
	}

	/*----------------------------- Items ---------------------------------- */
	
	@PostMapping("/saveItem")
	public String saveItem(@RequestParam("name") String name, @RequestParam("price") double price,
			@RequestParam("quantity") String quantity, Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		System.out.println(vendor);
		itemService.save(name, price, quantity, vendor);
		return "items/successAddItem";
	}
	
	@PostMapping("/searchItem")
	public ModelAndView searchItemByVendorId(@RequestParam("value") String value, Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		List<Items> items = itemService.findItemByVendorIdAndValue(vendor.getId(), value);
		System.out.println(items);
		return new ModelAndView("/items/itemList", "item", items);
	}

	@GetMapping("/myList")
	public ModelAndView getMyList(Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		List<Items> list = itemService.getItemsByVendorId(vendor.getId());
		return new ModelAndView("/items/itemList", "item", list);
	}
	
	@GetMapping("/availableList")
	public ModelAndView getAllItems() {
		List<Items> items = itemService.getAllItems();
		return new ModelAndView("/items/itemList", "item", items);
	}

	/* --------------------- Edit Item ---------------------- */

	@RequestMapping("/editList/{id}")
	public String editList(@PathVariable("id") int id, Model model) {
		Items i = itemService.getItemById(id);
		model.addAttribute("item", i);
		return "items/editItem";

	}

	/* --------------------- Delete Item ---------------------- */

	@GetMapping("/deleteItem/{id}")
	public String deleteItem(@PathVariable("id") int id) {
		itemService.deleteItemById(id);
		return "redirect:/myList";
	}

	/*----------------------------- Vendor Profile ---------------------------------- */
	
	@GetMapping("/vendorProfile")
	public String myProfile(Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		model.addAttribute("vendorProfile", vendor);
		return "/vendor/vendorProfile";
	}

	@GetMapping("/editVendorProfile/{id}")
	public String editProfile(@PathVariable("id") int id, Model model) {
		Vendor vendor = venService.getVendorById(id);
		model.addAttribute("venProfile", vendor);
		return "/vendor/ven_editProfile";
	}
	
	@PostMapping("/saveVendor")
	public String updateVendor(@ModelAttribute Vendor vendor, Model model) {
		model.addAttribute("venProfile", vendor);
		if (passValidation(vendor.getPassword())) {
			venService.updateVendor(vendor);
			return "redirect:/vendorProfile";
		}
		return "vendor/invalidPassword";
	}

	@PostMapping("/venChangePass")
	public String changePassword(@RequestParam("email") String email, Model model) {
		Vendor vendor = venService.getVendorByEmail(email);
		if (vendor == null)
			return "vendor/invalidEmail";
		else {
			model.addAttribute("password", vendor.getPassword());
			return "vendor/showPassword";
		}
	}

	/* --------------------- Forget password ---------------------- */

	@GetMapping("/forgetPassword")
	public String forgetPass() {
		return "/vendor/forgetPassword";
	}

	/*----------------------------- Password Validation ---------------------------------- */
	
	public boolean passValidation(String pass) {
		String regexp = "(?=.*[A-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{5,16}";
		Matcher m = Pattern.compile(regexp).matcher(pass);
		return m.matches();

	}

}
