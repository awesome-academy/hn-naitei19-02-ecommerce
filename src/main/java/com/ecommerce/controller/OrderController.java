package com.ecommerce.controller;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.ReceiverDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Project: hn-naitei19-02-ecommerce
 * @Author: sonle
 * @Date: 15/09/2023
 * @Time: 22:50
 */
@Controller
public class OrderController {
    @PostMapping("api/order")
    @ResponseBody
    public OrderDTO create(@Validated @RequestBody OrderDTO order) {
        return order;
    }

    @GetMapping("/order")
    public ModelAndView showForm() {
        var mav = new ModelAndView("order");
        var receiver = new ReceiverDTO(){
            {
                setId(1L);
                setName("Son Le");
                setPhone("0123456789");
                setAddress("Hanoi");
                setId(1L);
            }
        };
        var order = new OrderDTO(){
            {
                setId(1L);
                setTotalPrice(1000);
                setShippingStatus("shipping");
                setShippingFee(100L);
                setReceiverId(1L);
                setUserId(1L);
                setStatus(1);
                setReceiver(receiver);
            }
        };
        mav.addObject("order", order);
        return mav;
    }
}
