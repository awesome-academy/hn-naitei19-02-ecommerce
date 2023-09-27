package com.ecommerce.service.impl;

import com.ecommerce.dto.*;
import com.ecommerce.exception.NotFound;
import com.ecommerce.model.*;
import com.ecommerce.service.OrderService;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Project: hn-naitei19-02-ecommerce
 * @Author: sonle
 * @Date: 16/09/2023
 * @Time: 20:16
 */
@Service
public class OrderServiceImpl extends BaseService implements OrderService {
    @Override
    public List<OrderDTO> get() {
        Type listType = new TypeToken<List<OrderDTO>>() {
        }.getType();
        List<Order> orders = orderDAO.findAll();
        return modelMapper.map(orders, listType);
    }

    @Override
    public OrderDTO get(Long id) {
        Order order = orderDAO.findById(id).orElseThrow(() -> new NotFound("Order not found"));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    @Transactional
    public void save(OrderDTO e) {
        orderDAO.save(new Order(e));
    }

    @Override
    public void update(OrderDTO e) {

    }

    @Override
    public void delete(OrderDTO e) {

    }

    @Override
    public Page<OrderDTO> findOrdersByUserId(Long userId, FilterDTO filter) {
        Pageable pageable = getPageable(filter.getPage(), filter.getSize());
        Order.Status status = filter.getStatusValue() != -1 ? Order.Status.values()[filter.getStatusValue()] : null;
        Page<Order> orders = orderDAO.findAllByUserIdAndCreatedAtBetweenAndStatusLike(userId, filter.getFrom(), filter.getTo(), status, pageable);
        return orders.map(o -> {
            OrderDTO orderDTO = getMappedOrderDTO(o);
            List<OrderDetail> orderDetails = o.getOrderDetails();
            Product product = orderDetails.get(0).getProduct();
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            orderDTO.setFirstProduct(productDTO);
            return orderDTO;
        });
    }

    @Override
    public OrderDTO createOrder(Long userId, OrderDTO orderDTO) {
        ReceiverDTO receiverDTO = orderDTO.getReceiver();
        List<OrderDetailDTO> orderDetailsDTO = orderDTO.getOrderDetails();
        var cartOptional = cartDAO.findByUserId(userId);
        Cart cart;
        if (cartOptional.isEmpty()) {
            cart = new Cart(userId);
            cartDAO.save(cart);
        } else {
            cart = cartOptional.get();
        }
        List<CartDetail> cartDetails = cart.getCartDetails();
        //Lấy ra những sản phẩm có trong cart và có trong orderDetails
        List<CartDetail> filteredCartDetail = cartDetails.stream().filter(
                cd -> orderDetailsDTO.stream().anyMatch(od -> Objects.equals(cd.getProductId(), od.getProductId()))
        ).toList();
        List<OrderDetail> orderDetails = new ArrayList<>();
        mapCartDetailToOrderDetail(filteredCartDetail, orderDetails);
        long shippingFee = calculateShippingFee(orderDetails);
        long totalPrice = calculateTotalPrice(orderDetails, shippingFee);
        //Lưu thông tin người nhận
        Receiver receiver = modelMapper.map(receiverDTO, Receiver.class);
        receiverDAO.save(receiver);
        //Lưu thông tin order
        Order order = new Order(totalPrice, null, shippingFee, receiver.getId(), userId, 0);
        orderDAO.save(order);
        //Lưu thông tin orderDetails đồng thời trừ đi số lượng sản phẩm trong kho(tăng số lượng đã bán)
        orderDetails.forEach(orderDetail -> {
            orderDetail.setOrderId(order.getId());
            var product = orderDetail.getProduct();
            product.setQuantity(product.getQuantity() - orderDetail.getQuantity());
            product.setNumberOfSale(product.getNumberOfSale() + orderDetail.getQuantity());
            orderDetailDAO.save(orderDetail);
        });
        //Xóa những sản phẩm đã thanh toán order khỏi cart
        cartDetailDAO.deleteAll(filteredCartDetail);
        return getMappedOrderDTO(order);
    }

    @Override
    public OrderDTO initOrder(OrderDTO orderDTO) {
        List<OrderDetailDTO> orderDetailsDTO = orderDTO.getOrderDetails();
        long shippingFee = calculateShippingFee(orderDetailsDTO);
        orderDetailsDTO = orderDetailsDTO.stream().peek(odd -> {
            Product product = productDAO.findById(odd.getProductId()).orElseThrow(() -> new NotFound("Product not found"));
            odd.setProduct(modelMapper.map(product, ProductDTO.class));
        }).toList();
        long totalPrice = orderDetailsDTO.stream().mapToLong(od -> od.getProduct().getPrice() * od.getQuantity()).sum() + shippingFee;
        orderDTO.setOrderDetails(orderDetailsDTO);
        orderDTO.setShippingFee(shippingFee);
        orderDTO.setTotalPrice(totalPrice);
        return orderDTO;
    }

    private OrderDTO getMappedOrderDTO(Order order) {
        return modelMapper
                .typeMap(Order.class, OrderDTO.class)
                .addMapping(Order::getStatusValue, OrderDTO::setStatus)
                .map(order);
    }

    private long calculateTotalPrice(List<OrderDetail> orderDetails, long shippingFee) {
        return orderDetails.stream().mapToLong(od -> od.getPrice() * od.getQuantity()).sum() + shippingFee;
    }


    private void mapCartDetailToOrderDetail(CartDetail cartDetail, OrderDetail orderDetail) {
        Product product = cartDetail.getProduct();
        orderDetail.setPrice(product.getPrice());
        orderDetail.setQuantity(cartDetail.getQuantity());
        orderDetail.setProductId(product.getId());
    }

    private void mapCartDetailToOrderDetail(List<CartDetail> cartDetails, List<OrderDetail> orderDetails) {
        cartDetails.forEach(cartDetail1 -> {
            OrderDetail orderDetail = new OrderDetail();
            mapCartDetailToOrderDetail(cartDetail1, orderDetail);
            orderDetails.add(orderDetail);
        });
    }

    private long calculateShippingFee(List<?> orderDetails) {
        return orderDetails.size() * 1000L;
    }
}