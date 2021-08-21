package com.erkineren.demo.persistence.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class ProductImageDto {

    private Long id;

    @NotNull
    private String url;

}

