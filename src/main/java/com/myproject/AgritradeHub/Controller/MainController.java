package com.myproject.AgritradeHub.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.AgritradeHub.Model.AllUsers;
import com.myproject.AgritradeHub.Model.AllUsers.UserRole;
import com.myproject.AgritradeHub.Model.AllUsers.UserStatus;
import com.myproject.AgritradeHub.Model.Enquiry;
import com.myproject.AgritradeHub.Repository.AllUsersRepository;
import com.myproject.AgritradeHub.Repository.EnquiryRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	@Autowired
	private AllUsersRepository userRepo;
	
	@Autowired
	private EnquiryRepository enquiryRepo;

	//Index Page Get Method
	@GetMapping("/")
	public String ShowIndex()
	{
		return "index";
	}
	
	@GetMapping("/AboutUs")
	public String ShowAboutUs()
	{
		return "aboutus";
	}
	
	@GetMapping("/FarmerRegistration")
	public String ShowFarmerRegistration(Model model)
	{
		AllUsers farmer = new AllUsers();
		model.addAttribute("farmer", farmer);
		return "FarmerRegistration";
	}
	
	@PostMapping("/FarmerRegistration")
	public String FarmerRegistration(@ModelAttribute AllUsers farmer, RedirectAttributes attributes)
	{
		try {
			
			if (userRepo.existsByEmail(farmer.getEmail())) {
				attributes.addFlashAttribute("msg", "User Already Exists! ⚠️");
				return "redirect:/FarmerRegistration";
			}
			
			farmer.setRegDate(LocalDateTime.now());
			farmer.setRole(UserRole.FARMER);
			farmer.setStatus(UserStatus.PENDING);
			
			userRepo.save(farmer);
			attributes.addFlashAttribute("msg", "Farmer Registration Successful! ✅");
			
			return "redirect:/FarmerRegistration";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/FarmerRegistration";
		}
	}
	
	@GetMapping("/MerchantRegistration")
	public String ShowMerchantRegistration(Model model)
	{
		model.addAttribute("merchant", new AllUsers());
		return "MerchantRegistration";
	}
	
	@PostMapping("/MerchantRegistration")
	public String MerchantRegistration(@ModelAttribute AllUsers merchant, RedirectAttributes attributes)
	{
		try {
			
			if (userRepo.existsByEmail(merchant.getEmail())) {
				attributes.addFlashAttribute("msg", "User Already Exists! ⚠️");
				return "redirect:/FarmerRegistration";
			}
			
			merchant.setRegDate(LocalDateTime.now());
			merchant.setRole(UserRole.MERCHANT);
			merchant.setStatus(UserStatus.PENDING);
			
			userRepo.save(merchant);
			attributes.addFlashAttribute("msg", "Merchant Registration Successful! ✅");
			
			return "redirect:/MerchantRegistration";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/MerchantRegistration";
		}
	}
	
	
	
	@GetMapping("/Login")
	public String ShowLogin()
	{
		return "Login";
	}
	
	@PostMapping("/Login")
	public String Login(HttpServletRequest request, RedirectAttributes attributes, HttpSession session)
	{
		try {
			String email = request.getParameter("email"); 
			String password = request.getParameter("password");
			
			if (!userRepo.existsByEmail(email)) {
				attributes.addFlashAttribute("msg", "User Not Found ❌");
				return "redirect:/Login";
			}
			
			AllUsers user = userRepo.findByEmail(email);
			
			if (password.equals(user.getPassword())) {
				
				if (user.getStatus().equals(UserStatus.VERIFIED)) {
					//go to complete login
					if (user.getRole().equals(UserRole.ADMIN)) {
						//go to admin dashboard
						session.setAttribute("loggedInAdmin", user);
						return "redirect:/Admin/Dashboard";
					}
					else if(user.getRole().equals(UserRole.FARMER)){
						//go to farmer dashboard
						session.setAttribute("loggedInFarmer", user);
						return "redirect:/Farmer/Dashboard";
					}
					else if(user.getRole().equals(UserRole.MERCHANT)){
						//go to merchant dashboard
						session.setAttribute("loggedInMerchant", user);
						return "redirect:/Merchant/Dashboard";
					}
				}
				else  if(user.getStatus().equals(UserStatus.PENDING)){
					attributes.addFlashAttribute("msg", "Registration Pending, Please wait for Admin Aproval! ⚠️");
				}else {
					attributes.addFlashAttribute("msg", "Login Disabled 🚫, Please contact Administration!");
				}			
			}
			else {
				attributes.addFlashAttribute("msg", "Invalid Password ⚠️");
			}
			return "redirect:/Login";
		} catch (Exception e) {
			return "redirect:/Login";
		}
	}
	
	
	@GetMapping("/Services")
	public String ShowServices()
	{
		return "services";
	}
	
	@GetMapping("/gemini")
	public String Gemini()
	{
		return "gemini";
	}

	
	@GetMapping("/ContactUs")
	public String ShowContactUs(Model model)
	{
		Enquiry enquiry = new Enquiry();
		model.addAttribute("enquiry", enquiry);
		return "ContactUs";
	}
	
	@PostMapping("/ContactUs")
	public String SubmitEnquiry(@ModelAttribute Enquiry enquiry, RedirectAttributes attributes)
	{
		try {
			enquiry.setEnquiryDate(LocalDateTime.now());
			enquiryRepo.save(enquiry);
			attributes.addFlashAttribute("msg", "Enquiry Successfully Sumitted!");
			return "redirect:/ContactUs";
		} catch (Exception e) {
			
			return "redirect:/ContactUs";
		}
	}
}
