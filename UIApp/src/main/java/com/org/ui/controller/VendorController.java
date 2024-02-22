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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

	byte[] venProfileImage;

	String name;

	@ModelAttribute("vendorName")
	public String addNameAttribute() {
		String userName = name;
		return userName;
	}

	@ModelAttribute("venProfileImage")
	public String addImageAttribute() {
		return venProfileImage != null ? Base64.getEncoder().encodeToString(venProfileImage) : "";
	}

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
	public String vendor(Vendor vendor) {
		return "vendor/vendor_signup";
	}

	@PostMapping("/ven_signupwel")
	public String ven_signup(@RequestParam("file") MultipartFile file, Vendor vendor, Model model) throws IOException {

		if (!file.isEmpty()) {
			vendor.setProfileImage(file.getBytes());
		}

		if (passValidation(vendor.getPassword())) {
			venService.save(vendor);
			model.addAttribute("loginSuccess", "Your account is created");
			return "vendor/vendor_login";
		}
		return "fragments/passwordRequirements";

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
			venProfileImage = venService.getProfileImage(vendor.getId());
			String maps = vendor.getMaps();
			model.addAttribute("map", maps);
			model.addAttribute("vendor", vendor);
			userName = vendor.getEmail();
			name = vendor.getName();
			model.addAttribute("userName", name);
			model.addAttribute("loginSuccess", "Your account is created");
			model.addAttribute("vendor", vendor);
			return "vendor/vendor_home";
		} else {
			model.addAttribute("errorMsg", "Incorrect password/email");
			return "/vendor/vendor_login";
		}
	}

	/*----------------------------- Items ---------------------------------- */

	@GetMapping("/addItem")
	public String addItems(Model model) {
		return "items/addItem";
	}

	@PostMapping("/saveItem")
	public String updateItem(@ModelAttribute Items item, Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		item.setVendor(vendor);
		itemService.save(item);
//		model.addAttribute("addSuccess", "Item added successfully");
//		model.addAttribute("editSuccess", "Item edited successfully");
		return "items/successAddItem";
	}

	@PostMapping("/searchItem")
	public ModelAndView searchItemByVendorId(@RequestParam("value") String value, Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		List<Items> items = itemService.findItemByVendorIdAndValue(vendor.getId(), value);
		model.addAttribute("shopName", vendor.getShopName());
		return new ModelAndView("/items/itemList", "item", items);
	}

	@GetMapping("/myList")
	public ModelAndView getMyList(Model model) {
		Vendor vendor = venService.getVendorByEmail(userName);
		List<Items> list = itemService.getItemsByVendorId(vendor.getId());
		model.addAttribute("shopName", vendor.getShopName());
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
		System.out.println(i);
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
		venProfileImage = venService.getProfileImage(vendor.getId());
		model.addAttribute("venProfileImage",
				venProfileImage != null ? Base64.getEncoder().encodeToString(venProfileImage) : "");
		return "/vendor/vendorProfile";
	}

	@GetMapping("/editVendorProfile/{id}")
	public String editProfile(@PathVariable("id") int id, Model model) {
		Vendor vendor = venService.getVendorById(id);
		model.addAttribute("venProfile", vendor);
		return "/vendor/ven_editProfile";
	}

	@PostMapping("/saveVendor")
	public String updateVendor(@ModelAttribute Vendor vendor, @RequestParam("file") MultipartFile file, Model model)
			throws IOException {
		model.addAttribute("venProfile", vendor);
		if (passValidation(vendor.getPassword())) {
			vendor.setProfileImage(file.getBytes());
			venService.updateVendor(vendor);
			venProfileImage = venService.getProfileImage(vendor.getId());
			model.addAttribute("venProfileImage",
					venProfileImage != null ? Base64.getEncoder().encodeToString(venProfileImage) : "");
			return "redirect:/vendorProfile";
		}
		return "fragments/passwordRequirements";
	}

	@PostMapping("/venChangePass")
	public String changePassword(@RequestParam("email") String email, Model model) {
		Vendor vendor = venService.getVendorByEmail(email);
		if (vendor == null) {
			model.addAttribute("invalidEmail", "Invalid email address");
			return "vendor/forgetPassword";
		} else {
			model.addAttribute("password", vendor.getPassword());
			return "vendor/forgetPassword";
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
