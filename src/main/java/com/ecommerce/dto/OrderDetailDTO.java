package com.ecommerce.dto;

import com.ecommerce.model.BaseEntity;
import jakarta.validation.Valid;
import lombok.*;

/**
 * @Project: hn-naitei19-02-ecommerce
 * @Author: sonle
 * @Date: 16/09/2023
 * @Time: 00:52
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO extends BaseDTO {

    private Long price;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    @Valid
    private ProductDTO product;
}
