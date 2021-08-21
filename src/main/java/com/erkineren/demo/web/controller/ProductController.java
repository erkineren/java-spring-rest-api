package com.erkineren.demo.web.controller;

import com.erkineren.demo.persistence.model.dto.ProductDto;
import com.erkineren.demo.persistence.model.entity.Product;
import com.erkineren.demo.persistence.model.entity.User;
import com.erkineren.demo.persistence.specification.SearchCriteria;
import com.erkineren.demo.service.impl.ProductImageService;
import com.erkineren.demo.service.impl.ProductService;
import com.erkineren.demo.web.payload.request.ProductApprovalStatusRequest;
import com.erkineren.demo.web.payload.request.ProductRequest;
import com.erkineren.demo.web.payload.request.ProductStatusRequest;
import com.erkineren.demo.web.payload.request.SearchableAndSortableRequest;
import com.erkineren.demo.web.payload.response.ApiResponse;
import com.erkineren.demo.web.payload.response.PagedResponse;
import com.erkineren.demo.web.security.CurrentUser;
import com.erkineren.demo.web.security.UserPrincipal;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Product", description = "Product Controller")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    ModelMapper modelMapper;

    @Tag(name = "", description = "")
    @GetMapping
    public ResponseEntity<PagedResponse<ProductDto>> search(
            @CurrentUser UserPrincipal currentUser,
            SearchableAndSortableRequest searchableAndSortableRequest) {


        List<SearchCriteria> params = searchableAndSortableRequest.prepareSearchCriteriaList();
        if (!currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(User.Role.ROLE_ADMIN.toString()))) {
//            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission!");
//            throw new AccessDeniedException(apiResponse);
            params.add(new SearchCriteria("user", ":", currentUser.getUser()));
        }

        Page<Product> productPage = productService.search(params, searchableAndSortableRequest);

        List<ProductDto> content = productPage.stream()
                .map(m -> modelMapper.map(m, ProductDto.class))
                .collect(Collectors.toList());

        PagedResponse<ProductDto> response = new PagedResponse<>(
                content,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );

        return ResponseEntity.ok(response);

//        List<ProductDto> content = products.getContent().stream()
//                .map(m -> modelMapper.map(m, ProductDto.class))
//                .collect(Collectors.toList());
//
//        PagedResponse<ProductDto> response = new PagedResponse<>(content, products.getNumber(), products.getSize(), products.getTotalElements(),
//                products.getTotalPages(), products.isLast());
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable(name = "id") Long id
    ) {
        Product product = productService.get(id, currentUser);

        return ResponseEntity.ok(modelMapper.map(product, ProductDto.class));
    }

    @PostMapping
    public ResponseEntity<ProductDto> add(
            @Valid @RequestBody ProductRequest productRequest,
            @CurrentUser UserPrincipal currentUser) {

        Product newProduct = productService.add(modelMapper.map(productRequest, Product.class), currentUser);

//        if (productDto.getImages() != null && productDto.getImages().size() > 0) {
//            List<ProductImage> productImages = productDto.getImages().stream()
//                    .map(m -> modelMapper.map(m, ProductImage.class).setProduct(newProduct))
//                    .collect(Collectors.toList());
//            productImageService.saveAll(productImages);
//        }

        return new ResponseEntity<>(modelMapper.map(newProduct, ProductDto.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody ProductRequest productRequest,
            @CurrentUser UserPrincipal currentUser) {

        Product product = productService.update(id, modelMapper.map(productRequest, Product.class), currentUser);

        return ResponseEntity.ok(modelMapper.map(product, ProductDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable(name = "id") Long id,
            @CurrentUser UserPrincipal currentUser) {

        productService.delete(id, currentUser);

        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "Successfully deleted"));
    }


    @PutMapping("/{id}/approvalStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateApprovalStatus(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductApprovalStatusRequest approvalRequest,
            @CurrentUser UserPrincipal currentUser) {

        Product product = productService.updateApprovalStatus(id, approvalRequest.getStatus(), currentUser);

        return ResponseEntity.ok(modelMapper.map(product, ProductDto.class));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProductDto> updateStatus(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductStatusRequest statusRequest,
            @CurrentUser UserPrincipal currentUser) {

        Product product = productService.updateStatus(id, statusRequest.getStatus(), currentUser);

        return ResponseEntity.ok(modelMapper.map(product, ProductDto.class));
    }

}
