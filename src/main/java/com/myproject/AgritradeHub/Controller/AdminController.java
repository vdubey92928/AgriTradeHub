package com.myproject.AgritradeHub.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.AgritradeHub.API.SendAutoEmail;
import com.myproject.AgritradeHub.Model.AllUsers;
import com.myproject.AgritradeHub.Model.AllUsers.UserRole;
import com.myproject.AgritradeHub.Model.AllUsers.UserStatus;
import com.myproject.AgritradeHub.Model.Category;
import com.myproject.AgritradeHub.Model.Enquiry;
import com.myproject.AgritradeHub.Model.Orders.OrderStatus;
import com.myproject.AgritradeHub.Model.Payment;
import com.myproject.AgritradeHub.Repository.AllUsersRepository;
import com.myproject.AgritradeHub.Repository.CategoryRepository;
import com.myproject.AgritradeHub.Repository.EnquiryRepository;
import com.myproject.AgritradeHub.Repository.OrdersRepository;
import com.myproject.AgritradeHub.Repository.PaymentRepository;
import com.myproject.AgritradeHub.Repository.ProductsRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class AdminController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AllUsersRepository usersRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private ProductsRepository productsRepo;
	
	@Autowired
	private EnquiryRepository enquiryRepo;
	
	@Autowired
	private OrdersRepository ordersRepo;
	
	@Autowired
	private SendAutoEmail sendAutoEmail;

	@GetMapping("/Dashboard")
	public String ShowDashboard(Model model)
	{
		if (session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/Login";
		}
		
		// ✅ Counts
        model.addAttribute("farmerCount", usersRepo.countByRole(UserRole.FARMER));
        model.addAttribute("merchantCount", usersRepo.countByRole(UserRole.MERCHANT));
        model.addAttribute("productCount", productsRepo.count());
        model.addAttribute("orderCount", ordersRepo.count());
        model.addAttribute("categoryCount", categoryRepo.count());
        model.addAttribute("enquiryCount", enquiryRepo.count());

        // ✅ Recent Enquiries (last 5)
        List<Enquiry> recentEnquiries = enquiryRepo.findTop5ByOrderByEnquiryDateDesc();
        model.addAttribute("recentEnquiries", recentEnquiries);

        // Orders by status
        long cancelledOrders = ordersRepo.countByOrderStatus(OrderStatus.CANCELLED);
        long confirmedOrders = ordersRepo.countByOrderStatus(OrderStatus.CONFIRMED);
        long deliveredOrders = ordersRepo.countByOrderStatus(OrderStatus.DELIVERED);
        
        model.addAttribute("cancelledOrders", cancelledOrders);
        model.addAttribute("confirmedOrders", confirmedOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);
        
        
     // ✅ Orders data for Chart (monthly orders)
        List<Object[]> stats = ordersRepo.getMonthlyOrderStats();

        Map<Integer, Long> monthCountMap = new HashMap<>();
        for (Object[] row : stats) {
            int monthNumber = ((Number) row[0]).intValue();  // 1-12
            long count = ((Number) row[1]).longValue();
            monthCountMap.put(monthNumber, count);
        }

        List<String> orderMonths = new ArrayList<>();
        List<Long> orderCounts = new ArrayList<>();

        String[] monthNames = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        for (int i = 1; i <= 12; i++) {
            orderMonths.add(monthNames[i - 1]);
            orderCounts.add(monthCountMap.getOrDefault(i, 0L)); // agar nahi mila to 0
        }        
        model.addAttribute("orderMonths", orderMonths);
        model.addAttribute("orderCounts", orderCounts);


		return "Admin/Dashboard";
	}
	
	@GetMapping("/ManageFarmers")
	public String ShowManageFarmers(Model model)
	{
		if (session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/Login";
		}
		
		List<AllUsers> farmerList = usersRepo.findAllByRole(UserRole.FARMER);
		model.addAttribute("farmerList", farmerList);
		return "Admin/ManageFarmers";
	}
	
	@GetMapping("/FarmerStatus")
	public String UpdateFarmerStatus(@RequestParam long id, RedirectAttributes attributes)
	{
		try {
			AllUsers farmer = usersRepo.findById(id).get();
			
			if (farmer.getStatus().equals(UserStatus.PENDING)) {
				farmer.setStatus(UserStatus.VERIFIED);
				usersRepo.save(farmer);
				sendAutoEmail.sendApprovalEmail(farmer);
				//Email Intergration for Inform User 
			}
			else if(farmer.getStatus().equals(UserStatus.VERIFIED)){
				farmer.setStatus(UserStatus.DISABLED);
				usersRepo.save(farmer);
			}
			else{
				farmer.setStatus(UserStatus.VERIFIED);
				usersRepo.save(farmer);
			}
			
			attributes.addFlashAttribute("msg", farmer.getName()+" Status Successfully Updated");
			return "redirect:/Admin/ManageFarmers";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Admin/ManageFarmers";
		}
	}
	
	
	@GetMapping("/ManageMerchants")
	public String ShowManageMerchants(Model model)
	{
		if (session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/Login";
		}
		
		List<AllUsers> merchantList = usersRepo.findAllByRole(UserRole.MERCHANT);
		model.addAttribute("merchantList", merchantList);
		return "Admin/ManageMerchants";
	}
	
	@GetMapping("/MerchantStatus")
	public String UpdateMerchantStatus(@RequestParam long id, RedirectAttributes attributes)
	{
		try {
			AllUsers merchant = usersRepo.findById(id).get();
			
			if (merchant.getStatus().equals(UserStatus.PENDING)) {
				merchant.setStatus(UserStatus.VERIFIED);
				usersRepo.save(merchant);
				sendAutoEmail.sendApprovalEmail(merchant);
				//Email Intergration for Inform User 
			}
			else if(merchant.getStatus().equals(UserStatus.VERIFIED)){
				merchant.setStatus(UserStatus.DISABLED);
				usersRepo.save(merchant);
			}
			else{
				merchant.setStatus(UserStatus.VERIFIED);
				usersRepo.save(merchant);
			}
			
			attributes.addFlashAttribute("msg", merchant.getName()+" Status Successfully Updated");
			return "redirect:/Admin/ManageMerchants";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Admin/ManageMerchants";
		}
	}
	
	@GetMapping("/AddCategory")
	public String ShowAddCategory(Model model)
	{
		if (session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/Login";
		}
		
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		return "Admin/AddCategory";
	}
	
	@PostMapping("/AddCategory")
	public String AddCategory(@RequestParam("categoryName") String categoryName, RedirectAttributes attributes)
	{
		try {
			Category cate = new Category();
			cate.setCategoryName(categoryName);
			categoryRepo.save(cate);
			attributes.addFlashAttribute("msg", "Category Successfully Added! ✅");
			
			return "redirect:/Admin/AddCategory";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Admin/AddCategory";
		}
	}
	
	
	@GetMapping("/DeleteCategory")
	public String DeleteCategory(@RequestParam long id)
	{
		categoryRepo.deleteById(id);
		return "redirect:/Admin/AddCategory";
	}
	
	@GetMapping("/ViewOrder")
	public String ShowViewOrder(Model model)
	{
		if (session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/Login";
		}
		
		List<Payment> orderList = paymentRepo.findAll();
		model.addAttribute("orderList", orderList);
		return "Admin/ViewOrder";
	}
	
	@GetMapping("/ViewProfile")
	public String ViewProfile(Model model)
	{
		if (session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/Login";
		}
		AllUsers admin = (AllUsers) session.getAttribute("loggedInAdmin");
		model.addAttribute("user", admin);
		return "Admin/ViewProfile";
	}
	
	@GetMapping("/Enquiry")
	public String ShowEnquiry(Model model)
	{
		if (session.getAttribute("loggedInAdmin")==null) {
			return "redirect:/Login";
		}
		
		List<Enquiry> enquiryList = enquiryRepo.findAll();
		model.addAttribute("enquiryList", enquiryList);
		return "Admin/Enquiry";
	}
	
	@GetMapping("/DeleteEnquiry")
	public String DeleteEnquiry(@RequestParam("id") long id)
	{
		enquiryRepo.deleteById(id);
		return "redirect:/Admin/Enquiry";
	}
	
	//change password
		@GetMapping("/ChangePassword")
		public String ShowChangePassword()
		{
			if (session.getAttribute("loggedInAdmin")==null) {
				return "redirect:/Login";
			}
			return "Admin/ChangePassword";
		}
		
		@PostMapping("/ChangePassword")
		public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes)
		{
			try {
				String oldPassword = request.getParameter("oldPassword");
				String newPassword = request.getParameter("newPassword");
				String confirmPassword = request.getParameter("confirmPassword");
				
				if (!newPassword.equals(confirmPassword)) {
					attributes.addFlashAttribute("msg", "New and Confirm Password are not same ⚠️");
					return "redirect:/Admin/ChangePassword";
				}
				
				AllUsers admin = (AllUsers) session.getAttribute("loggedInAdmin");
				
				if (newPassword.equals(admin.getPassword())) {
					attributes.addFlashAttribute("msg", "Can't change, new and old password are same!!");
					return "redirect:/Admin/ChangePassword";
				}
				
				if (oldPassword.equals(admin.getPassword())) {
					admin.setPassword(confirmPassword);
					usersRepo.save(admin);
					session.removeAttribute("loggedInAdmin");
					return "redirect:/Login";
					
				}else {
					attributes.addFlashAttribute("msg", "Invalid Old Password!!!");
				}
				return "redirect:/Admin/ChangePassword";
				
			} catch (Exception e) {
				return "redirect:/Admin/ChangePassword";
			}
		}
		
	
	@GetMapping("/logout")
	public String Logout()
	{
		session.removeAttribute("loggedInAdmin");
		return "redirect:/Login";
	}
	
}
