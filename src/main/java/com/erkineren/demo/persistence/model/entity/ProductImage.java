package com.erkineren.demo.persistence.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
    }, optional = false)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Product product;

}
