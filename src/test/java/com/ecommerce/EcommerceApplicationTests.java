package com.ecommerce;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.model.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(classes= EcommerceApplicationTests.class)
class EcommerceApplicationTests {
	@Autowired
	private CartDAO cartDAO;
	@Test
	void test() {
		Optional<Cart> cart = cartDAO.findByUserId(9L);
		System.out.println(cart.orElseThrow().getCartDetails().size());
	}

}
