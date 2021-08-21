package com.erkineren.demo.persistence.repository;


import com.erkineren.demo.persistence.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findAllByUserId(Long userId, Pageable pageable);

    @Query("select p from Product p where p.stock between ?1 and ?2")
    Page<Product> findByStockBetween(@NonNull int stockStart, @NonNull int stockEnd, Pageable pageable);



}
