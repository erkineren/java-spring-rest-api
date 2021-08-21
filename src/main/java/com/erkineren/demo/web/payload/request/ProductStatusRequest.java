package com.erkineren.demo.web.payload.request;

import com.erkineren.demo.persistence.model.entity.Product;
import lombok.Data;

@Data
public class ProductStatusRequest {
    private Product.Status status;
}
