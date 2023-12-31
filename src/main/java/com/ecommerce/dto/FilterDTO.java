package com.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Project: hn-naitei19-02-ecommerce
 * @Author: sonle
 * @Date: 24/09/2023
 * @Time: 13:44
 */
@Data
@ToString
//@Builder
public class FilterDTO {
    @Min(0)
//    @Builder.Default
    private int page = 1;
    @Min(1)
//    @Builder.Default
    private int size = 6;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date from;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date to;
//    @Builder.Default
    @Pattern(regexp = "(^|pending|accepted|rejected|cancelled|shipping|completed)$", message = "Invalid filter")
    private String s = "";//status
    public Integer getStatusValue(){
        return switch (s) {
            case "pending" -> 0;
            case "accepted" -> 1;
            case "rejected" -> 2;
            case "cancelled" -> 3;
            case "shipping" -> 4;
            case "completed" -> 5;
            default -> -1;
        };
    }
}
