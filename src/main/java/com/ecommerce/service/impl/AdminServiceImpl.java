package com.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderDetailDTO;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.ReceiverDTO;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetail;
import com.ecommerce.service.AdminService;

@Service 
public class AdminServiceImpl extends BaseService implements AdminService{

	@Override
	public List<OrderDTO> showAllByAdmin() {
		List<Order> orders = orderDAO.findAll();
		List<OrderDTO> orderDTOs = new ArrayList<>();
		
		for (Order order : orders)
		{
			OrderDTO orderDTO = new OrderDTO();
	        BeanUtils.copyProperties(order, orderDTO);
	        orderDTO.setFullname(order.getUser().getFullname()); // lay ten nguoi dat hang tu model
	        orderDTO.setStatus(order.getStatus().getValue());	//anh xa tu enum-Model sang int-DTO
	        orderDTOs.add(orderDTO);
		}
		
		return orderDTOs;
	}

	@Override
	public OrderDTO getOrderDetail(Long orderId) {
		 Optional<Order> optionalOrder = orderDAO.findById(orderId);
		 
		 if (optionalOrder.isPresent())
		 {
			 Order order = optionalOrder.get();
			 OrderDTO orderDTO = new OrderDTO();
			 ReceiverDTO receiverDTO = new ReceiverDTO();
			 
			 //System.out.println(order.getReceiver().getName());
			 BeanUtils.copyProperties(order.getReceiver(), receiverDTO); // Lay receiver tu order
			 BeanUtils.copyProperties(order, orderDTO);
			 orderDTO.setStatus(order.getStatus().getValue());
			 orderDTO.setReceiver(receiverDTO);
			 
			 // Lay product va order-detail tu order
			 List<OrderDetailDTO> orderDetailDTOs = getProductFromOrder(order); 
			 orderDTO.setOrderDetails(orderDetailDTOs);
			 //System.out.println(orderDetailDTOs.size());
			 
			 return orderDTO;
		 }
		 else
		 {
			 System.out.println("Don hang khong ton tai");
			 return null;
		 }
	}

	private List<OrderDetailDTO> getProductFromOrder(Order order) {
		List<OrderDetail> orderDetails = order.getOrderDetails();
		List<OrderDetailDTO> orderDetailDTOs = new ArrayList<>();
		for ( OrderDetail orderDetail : orderDetails)
		{
			ProductDTO productDTO = new ProductDTO();
			OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
			
			BeanUtils.copyProperties(orderDetail.getProduct(), productDTO);
			BeanUtils.copyProperties(orderDetail, orderDetailDTO);
			orderDetailDTO.setProduct(productDTO);
			orderDetailDTOs.add(orderDetailDTO);
		}
		return orderDetailDTOs;
	}
	
}
