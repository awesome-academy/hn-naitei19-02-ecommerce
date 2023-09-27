package com.ecommerce.service;

import java.util.List;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderDetailDTO;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.model.Order;

public interface AdminService {
	List<OrderDTO> showAllByAdmin();
	OrderDTO getOrderDetail(Long orderId);
}
