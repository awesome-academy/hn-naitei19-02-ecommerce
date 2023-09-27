package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO extends BaseDTO {
	private Long id;
	private Integer gender;
	private Integer role;
	private String username;
	private String password;
	private Boolean isActive;
	private String fullname;
	private String avatar;
	private String phone;
	private String address;
	
	
}
