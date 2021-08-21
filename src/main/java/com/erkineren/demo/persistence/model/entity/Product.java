package com.erkineren.demo.persistence.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
    }, optional = false)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private Supplier supplier;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @ManyToOne(optional = false)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.NOT_ACTIVE;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ApprovalStatus approvalStatus = ApprovalStatus.WAITING_REVIEW;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    private List<ProductImage> images = new ArrayList<>();

    public Product addImage(String url) {
        images.add(
                new ProductImage()
                        .setProduct(this)
                        .setUrl(url)
        );
        return this;
    }

    public enum Status {
        ACTIVE, NOT_ACTIVE;
    }

    public enum ApprovalStatus {
        WAITING_REVIEW, APPROVED, REJECTED;
    }

}


