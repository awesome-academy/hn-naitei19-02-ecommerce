package com.ecommerce.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.dto.HeaderInfoDTO;
import com.ecommerce.dto.ProductDTO;
import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.SearchService;
import com.ecommerce.userdetails.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SearchService searchService;

    private final CategoryService categoryService;

    private final CartService cartService;

    private final OrderService orderService;

    @GetMapping(value = { "/", "search" })
    public ModelAndView index(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer numberOfSale,
            @RequestParam(defaultValue = "numberOfSale") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortType,
            @RequestParam(defaultValue = "1") Integer page) {

        ModelAndView modelAndView = new ModelAndView("user/home");

        Pageable pageable = PageRequest.of(page - 1, 18,
                Sort.by(sortType.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy));
        Page<Product> pages = searchService.search(name, minPrice, maxPrice, category, numberOfSale, pageable);
        Integer totalPages = pages.getTotalPages();

        List<ProductDTO> products = searchService.mapProductDTOs(pages.getContent());
        List<CategoryDTO> categories = categoryService.getAllByTree();
        List<ProductDTO> saleProducts = searchService.findTopSale();

        modelAndView.addObject("products", products);
        modelAndView.addObject("saleProducts", saleProducts);
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("categoryName", category);
        modelAndView.addObject("name", name);
        modelAndView.addObject("page", page);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("sortBy", sortBy);

        return modelAndView;
    }

    @GetMapping("/header-info")
    @ResponseBody
    public HeaderInfoDTO header(UsernamePasswordAuthenticationToken principal) {
        var user = (CustomUserDetails) principal.getPrincipal();
        var cartSize = cartService.getCartSize(user);
        var notificationSize = 5; // fixme: get notification size
        var orderSize = orderService.getOrderSize(user);
        return new HeaderInfoDTO(cartSize, notificationSize, orderSize);
    }
}