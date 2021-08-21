package com.erkineren.demo.web.payload.response;

import com.erkineren.demo.persistence.model.entity.Product;
import com.erkineren.demo.persistence.model.entity.ProductImage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class ProductResponse {

    private Long id;

    private String title;

    private String description;

    private String supplier;

    private BigDecimal price;

    private String user;

    private List<String> images;

    public static ProductResponse fromProduct(Product product) {
        return (new ProductResponse())
                .setId(product.getId())
                .setDescription(product.getDescription())
                .setSupplier(product.getSupplier().getName())
                .setPrice(product.getPrice())
                .setUser(product.getUser().getName())
                .setImages(
                        product.getImages()
                                .stream().map(ProductImage::getUrl)
                                .collect(Collectors.toList())
                )
                ;
    }
}
