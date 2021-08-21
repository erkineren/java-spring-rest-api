package com.erkineren.demo.web.payload.request;

import com.erkineren.demo.persistence.model.dto.ProductImageDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {

    @Length(min = 5)
    private String title;

    @Length(min = 20)
    private String description;

    @NotBlank
    private String supplierName;

    @Min(0)
    private BigDecimal price;

    @Min(0)
    private int stock;

    private List<ProductImageDto> productImageList;
}
