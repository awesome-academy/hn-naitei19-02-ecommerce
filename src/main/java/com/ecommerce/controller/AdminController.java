package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.service.AdminService;
import com.ecommerce.service.impl.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/order")
	public String showAllByAdmin(Model model) {
		List<OrderDTO> orderDTOS = adminService.showAllByAdmin();
		model.addAttribute("orders", orderDTOS);
		return "admin/index";
	}
	
	@GetMapping("/order/{orderId}")
	public String showOrderDetail(@PathVariable Long orderId, Model model) {
		OrderDTO orderDTO = adminService.getOrderDetail(orderId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("createAt", orderDTO.getCreatedAt());
		
		int status = orderDTO.getStatus();
		String strStatus = "";
		switch (status) {
		case 0: strStatus = "Đang xử lý";break;
		case 1: strStatus = "Tiếp nhận";break;
		case 2: strStatus = "Từ chối";break;
		case 3: strStatus = "Đã hủy";break;
		case 4: strStatus = "Đang giao";break;
		case 5: strStatus = "Thành công";break;
		default: strStatus = "Đang cập nhật";
		}
		
		//System.out.println(orderDTO.getReceiver().getName());
		model.addAttribute("status", strStatus);
		model.addAttribute("receiver", orderDTO.getReceiver());
		model.addAttribute("totalPrice", orderDTO.getTotalPrice());
		model.addAttribute("shippingFee", orderDTO.getShippingFee());
		model.addAttribute("total", orderDTO.getTotalPrice()+orderDTO.getShippingFee());
		model.addAttribute("orderDetails", orderDTO.getOrderDetails());
		//System.out.println(orderDTO.getOrderDetails().get(0).getProduct().getName());
		return "admin/order-detail";
	}
}
