package com.erkineren.demo.persistence.model.dto;


import com.erkineren.demo.persistence.model.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;

    private String title;

    private String description;

    private String supplierName;

    private BigDecimal price;

    private int stock;

    private String userId;

    private Product.Status status = Product.Status.NOT_ACTIVE;

    private Product.ApprovalStatus approvalStatus = Product.ApprovalStatus.WAITING_REVIEW;

    private List<ProductImageDto> images;
}
