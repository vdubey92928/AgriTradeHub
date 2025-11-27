package com.myproject.AgritradeHub.Controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.AgritradeHub.API.PaymentService;
import com.myproject.AgritradeHub.API.SendAutoEmail;
import com.myproject.AgritradeHub.Model.AllUsers;
import com.myproject.AgritradeHub.Model.Category;
import com.myproject.AgritradeHub.Model.Orders;
import com.myproject.AgritradeHub.Model.Orders.OrderStatus;
import com.myproject.AgritradeHub.Model.Payment;
import com.myproject.AgritradeHub.Model.Products;
import com.myproject.AgritradeHub.Model.Products.ProductStatus;
import com.myproject.AgritradeHub.Repository.AllUsersRepository;
import com.myproject.AgritradeHub.Repository.CategoryRepository;
import com.myproject.AgritradeHub.Repository.OrdersRepository;
import com.myproject.AgritradeHub.Repository.PaymentRepository;
import com.myproject.AgritradeHub.Repository.ProductsRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Merchant")
public class MerchantController {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ProductsRepository productRepo;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private OrdersRepository orderRepo;
	
	@Autowired
	private PaymentRepository paymentRepo;
	
	@Autowired
	private AllUsersRepository usersRepo;
	
	@Autowired
	private SendAutoEmail sendAutoEmail;
	
	@GetMapping("/Dashboard")
	public String ShowDashboard(Model model, @RequestParam(value = "category", required = false) String categoryName)	
	{
		if (session.getAttribute("loggedInMerchant")==null) {
			return "redirect:/Login";
		}
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		
		if (categoryName !=null && !categoryName.isEmpty()) {
			List<Products> productsList = productRepo.findAllByCategory(categoryName);
			model.addAttribute("productsList", productsList);
			model.addAttribute("selectedCategory", categoryName);
		}
		else {
			List<Products> productsList = productRepo.findAll();
			model.addAttribute("productsList", productsList);
		}
		
		return "Merchant/Dashboard";
	}
	
	
	@GetMapping("/BuyProduct")
	public String ShowBuyProduct(@RequestParam("id") long id, Model model, RedirectAttributes attributes) {
		if (session.getAttribute("loggedInMerchant") == null) {
			attributes.addFlashAttribute("error", "Session Expired ⚠️");
			return "redirect:/MerchantLogin";
		}

		Products product = productRepo.findById(id).orElseThrow();
		model.addAttribute("razorpayKeyId", "rzp_live_Io1s9ctQtD0G1b");
		model.addAttribute("product", product);
		return "Merchant/BuyProduct";
	}

	@GetMapping("/create-order")
	@ResponseBody
	public Map<String, Object> createRazorpayOrder(@RequestParam long productId, @RequestParam int quantity) {
		Map<String, Object> data = new HashMap<>();
		try {
			Products product = productRepo.findById(productId).orElseThrow();
			int amount = product.getPricePerUnit().multiply(BigDecimal.valueOf(quantity)).intValue();
			//int amount = (int) (product.getPricePerUnit()*quantity);
			com.razorpay.Order razorOrder = paymentService.createRazorpayOrder(amount);

			data.put("orderId", razorOrder.get("id"));
			data.put("razorpayKeyId", "rzp_live_Io1s9ctQtD0G1b");
			data.put("amount", amount * 100); // paise
			data.put("currency", "INR");
		} catch (Exception e) {
			data.put("error", e.getMessage());
		}
		return data;
	}

	@PostMapping("/verify_payment")
	public String verifyPayment(@RequestParam("paymentId") String paymentId,
								@RequestParam("orderId") String razorpayOrderId, 
								@RequestParam("signature") String signature,
								@RequestParam("productId") long productId, 
								@RequestParam("buyQuantity") int quantity, 
								Model model, RedirectAttributes attributes) {
		try {
			AllUsers merchant = (AllUsers) session.getAttribute("loggedInMerchant");
			Products product = productRepo.findById(productId).orElseThrow();

			// Save Order
			Orders order = new Orders();
			order.setProductName(product.getProductName());
			order.setPricePerUnit(product.getPricePerUnit());
			order.setQuantity(quantity);
			order.setFarmer(product.getFarmer());
			order.setMerchant(merchant);
			order.setOrderStatus(OrderStatus.CONFIRMED);
			order.setOrderDate(LocalDateTime.now());
			orderRepo.save(order);

			// Save Payment
			Payment payment = new Payment();
			payment.setOrder(order);

			long transactionId = System.currentTimeMillis();

			payment.setTransactionId(transactionId);
			payment.setPayId(paymentId);
			payment.setAmount(product.getPricePerUnit().multiply(BigDecimal.valueOf(quantity)));
			payment.setPaymentMode("Online");
			payment.setPaymentDate(LocalDateTime.now());

			paymentRepo.save(payment);

			// Update Quantity
			if (product != null) {
				int remainingQty = product.getQuantity() - order.getQuantity();
				if (remainingQty <= 0) {
					remainingQty = 0;
					product.setStatus(ProductStatus.OUT_OF_STOCK);
				}
				product.setQuantity(remainingQty);

				productRepo.save(product);
			}

			sendAutoEmail.SendOrderConfirmationEmail(order);

			attributes.addFlashAttribute("msg", true);
			attributes.addFlashAttribute("transactionId", transactionId);
			attributes.addFlashAttribute("quantity", quantity);

			return "redirect:/Merchant/BuyProduct?id=" + productId;
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Payment verification failed: " + e.getMessage());
			return "redirect:/Merchant/BuyProduct?id=" + productId;
		}
	}
	
	@GetMapping("/MyOrders")
	public String ShowMyOrders(Model model)
	{
		if (session.getAttribute("loggedInMerchant")==null) {
			return "redirect:/Login";
		}	
		AllUsers merchant = (AllUsers) session.getAttribute("loggedInMerchant");
		
		List<Orders> orderList = orderRepo.findAllByMerchant(merchant);
		model.addAttribute("orderList", orderList);
		return "Merchant/MyOrders";
	}
	
	@GetMapping("/CancelOrder")
	public String CancelOrder(@RequestParam("id") long id, RedirectAttributes attributes)
	{
		try {
			Orders order = orderRepo.findById(id).get();
			Payment payment = paymentRepo.findByOrder(order);
			
			PaymentService refundService = new PaymentService();
			refundService.refundPayment(payment.getPayId());
			
			order.setOrderStatus(OrderStatus.CANCELLED);
			orderRepo.save(order);
			sendAutoEmail.SendOrderCancellationEmail(order, payment);
			
			attributes.addFlashAttribute("msg", "Order Successfully Cancelled ❌");
			
			return "redirect:/Merchant/MyOrders";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Merchant/MyOrders";
		}
	}
	//change password
	@GetMapping("/ChangePassword")
	public String ShowChangePassword()
	{
		if (session.getAttribute("loggedInMerchant")==null) {
			return "redirect:/Login";
		}
		return "Merchant/ChangePassword";
	}
	
	@GetMapping("/UserProfile")
	public String ShowUserProfile(Model model)
	{
		if (session.getAttribute("loggedInMerchant")==null) {
			return "redirect:/Login";
		}
		
		AllUsers merchant = (AllUsers) session.getAttribute("loggedInMerchant");
		model.addAttribute("merchant", merchant);
		return "Merchant/UserProfile";
	}
	
	@PostMapping("/UserProfile")
	public String UpdateUserProfile(@RequestParam("ImageFile") MultipartFile profilePic, RedirectAttributes attributes)
	{
		try {
			
			String storageFileName = System.currentTimeMillis()+"_"+profilePic.getOriginalFilename();
			String uploadDir = "Public/ProfilePicture/";
			Path uploadPath = Paths.get(uploadDir);
			
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try(InputStream inputStream = profilePic.getInputStream())
			{
				Files.copy(inputStream, Paths.get(uploadDir+storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
			
			AllUsers merchant = (AllUsers) session.getAttribute("loggedInMerchant");
			merchant.setProfilePic(storageFileName);
			usersRepo.save(merchant);
			
			attributes.addFlashAttribute("msg", "Profile Pic Updated Successfully!!");
			return "redirect:/Merchant/UserProfile";
		} catch (Exception e) {
			
			attributes.addFlashAttribute("msg", e.getMessage());
			return "redirect:/Merchant/UserProfile";
		}
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
				return "redirect:/Merchant/ChangePassword";
			}
			
			AllUsers merchant = (AllUsers) session.getAttribute("loggedInMerchant");
			
			if (newPassword.equals(merchant.getPassword())) {
				attributes.addFlashAttribute("msg", "Can't change, new and old password are same!!");
				return "redirect:/Merchant/ChangePassword";
			}
			
			if (oldPassword.equals(merchant.getPassword())) {
				merchant.setPassword(confirmPassword);
				usersRepo.save(merchant);
				session.removeAttribute("loggedInMerchant");
				return "redirect:/Login";
				
			}else {
				attributes.addFlashAttribute("msg", "Invalid Old Password!!!");
			}
			return "redirect:/Merchant/ChangePassword";
			
		} catch (Exception e) {
			return "redirect:/Merchant/ChangePassword";
		}
	}
	
	@GetMapping("/Logout")
	public String Logout()
	{
		session.removeAttribute("loggedInAdmin");
		return "redirect:/Login";
	}
}
