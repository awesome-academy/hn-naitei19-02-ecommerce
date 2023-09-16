package com.ecommerce.model;

import com.ecommerce.dto.BaseDTO;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Project: hn-naitei19-02-ecommerce
 * @Author: sonle
 * @Date: 15/09/2023
 * @Time: 08:58
 * @apiNote : This is abstract class for all entity
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public BaseEntity(BaseDTO dto) {
        fromDTO(dto);
    }

    public void fromDTO(BaseDTO dto) {
        BeanUtils.copyProperties(dto, this);
    }
}
