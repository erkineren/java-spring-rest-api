package com.erkineren.demo.service.impl;

import com.erkineren.demo.persistence.model.entity.Product;
import com.erkineren.demo.persistence.model.entity.User;
import com.erkineren.demo.persistence.repository.ProductRepository;
import com.erkineren.demo.persistence.repository.SupplierRepository;
import com.erkineren.demo.persistence.repository.UserRepository;
import com.erkineren.demo.persistence.specification.SearchCriteria;
import com.erkineren.demo.persistence.specification.SearchSpecification;
import com.erkineren.demo.web.exception.ResourceNotFoundException;
import com.erkineren.demo.web.exception.UnauthorizedException;
import com.erkineren.demo.web.payload.response.ApiResponse;
import com.erkineren.demo.web.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends SearchableEntityService<Product> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public Page<Product> getAll(UserPrincipal currentUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");

        SearchSpecification<Product> spec =
                new SearchSpecification<>(new SearchCriteria("title", ":", "Product4"));
        return productRepository.findAll(spec, pageable);
//        return productRepository.findAllByUserId(currentUser.getId(), pageable);
    }

    public Product add(Product product, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), "id", currentUser.getId()));
        product.setUser(user);

        if (product.getSupplier() != null && product.getSupplier().getId() == null) {
            product.setSupplier(supplierRepository.findByName(product.getSupplier().getName()).orElseGet(product::getSupplier));
        }

        if (product.getImages() != null && product.getImages().size() > 0) {
            product.getImages().forEach((im) -> {
                im.setProduct(product);
            });
        }


        return productRepository.save(product);
    }

    public Product update(Long id, Product newProduct, UserPrincipal currentUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", id));

        if (!currentUser.hasRoleAdmin() &&
                !product.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission"));
        }

            product
                .setTitle(newProduct.getTitle())
                .setDescription(newProduct.getDescription())
                .setPrice(newProduct.getPrice())
                .setImages(newProduct.getImages())
                .setStock(newProduct.getStock())
        ;

        if (product.getImages() != null && product.getImages().size() > 0) {
            product.getImages().forEach((im) -> {
                im.setProduct(product);
            });
        }

        return productRepository.save(product);
    }

    public void delete(Long id, UserPrincipal currentUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", id));
        if (!product.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission"));
        }

        productRepository.deleteById(id);
    }

    public Product get(Long id, UserPrincipal currentUser) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), "id", id));

        if (!currentUser.hasRoleAdmin() &&
                !product.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "You don't have permission"));
        }

        return product;
    }


    public Product updateApprovalStatus(Long id, Product.ApprovalStatus approvalStatus, UserPrincipal currentUser) {
        Product product = this.get(id, currentUser);
        product.setApprovalStatus(approvalStatus);
        product.setStatus(approvalStatus == Product.ApprovalStatus.REJECTED ? Product.Status.NOT_ACTIVE : Product.Status.ACTIVE);

        productRepository.save(product);

        return product;
    }

    public Product updateStatus(Long id, Product.Status status, UserPrincipal currentUser) {
        Product product = this.get(id, currentUser);
        if (product.getApprovalStatus() != Product.ApprovalStatus.APPROVED) {
            throw new UnauthorizedException(new ApiResponse(Boolean.FALSE, "Product is not approved yet"));
        }
        product.setStatus(status);
        productRepository.save(product);

        return product;
    }
}
