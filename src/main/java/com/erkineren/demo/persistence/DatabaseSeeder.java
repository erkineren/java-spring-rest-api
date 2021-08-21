package com.erkineren.demo.persistence;


import com.erkineren.demo.persistence.model.entity.Product;
import com.erkineren.demo.persistence.model.entity.Supplier;
import com.erkineren.demo.persistence.model.entity.User;
import com.erkineren.demo.persistence.repository.ProductRepository;
import com.erkineren.demo.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class DatabaseSeeder {

    private final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    @Autowired
    public DatabaseSeeder(
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;

    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUsersTable();
        seedProductsTable();

    }

    private void seedUsersTable() {

        List<User> users = new ArrayList<User>() {
            {
                add(new User()
                        .setName("Hipicon Admin")
                        .setEmail("hipicon@hipicon.com")
                        .setPassword(new BCryptPasswordEncoder().encode("password"))
                        .setRole(User.Role.ROLE_ADMIN)
                );
                add(new User()
                        .setName("Hipicon User")
                        .setEmail("hipicon2@hipicon.com")
                        .setPassword(new BCryptPasswordEncoder().encode("password"))
                        .setRole(User.Role.ROLE_USER)
                );
            }
        };


        if (userRepository.count() == 0) {
            userRepository.saveAll(users);
            logger.info("Users Seeded");
        } else {
            logger.info("Users Seeding Not Required");
        }


    }

    private void seedProductsTable() {

        Optional<User> user = userRepository.findById(1L);
        List<Product> products = new ArrayList<Product>() {
            {
                add(new Product()
                        .setTitle("Product1")
                        .setDescription("Description 1")
                        .setPrice(new BigDecimal("1.2"))
                        .setSupplier(new Supplier().setName("Supplier 1"))
                        .setStock(12)
                        .setUser(user.orElse(null))
                        .setStatus(Product.Status.ACTIVE)
                        .setApprovalStatus(Product.ApprovalStatus.APPROVED)
                        .addImage("https://cdn.hipicon.com/images/Products/2021/02/14/1613291108_1.jpeg&w=350&h=350")

                );
                add(new Product()
                        .setTitle("Product2")
                        .setDescription("Description 2")
                        .setPrice(new BigDecimal("1.2"))
                        .setSupplier(new Supplier().setName("Supplier 1"))
                        .setUser(user.orElse(null))
                        .setStock(1)
                        .setStatus(Product.Status.NOT_ACTIVE)
                        .setApprovalStatus(Product.ApprovalStatus.WAITING_REVIEW)
                        .addImage("https://cdn.hipicon.com/images/Products/2021/07/06/1625556306_1.jpeg&w=350&h=350")
                );
                add(new Product()
                        .setTitle("Product3")
                        .setDescription("Description 3")
                        .setPrice(new BigDecimal("1.2"))
                        .setSupplier(new Supplier().setName("Supplier 2"))
                        .setUser(user.orElse(null))
                        .setStock(0)
                        .setStatus(Product.Status.NOT_ACTIVE)
                        .setApprovalStatus(Product.ApprovalStatus.REJECTED)
                        .addImage("https://cdn.hipicon.com/images/Products/2018/05/21/1526916204_1.jpeg&w=350&h=350")
                );
                add(new Product()
                        .setTitle("Product4")
                        .setDescription("Description 4")
                        .setPrice(new BigDecimal("1.2"))
                        .setSupplier(new Supplier().setName("Supplier 2"))
                        .setUser(user.orElse(null))
                        .setStock(0)
                        .setStatus(Product.Status.NOT_ACTIVE)
                        .setApprovalStatus(Product.ApprovalStatus.APPROVED)
                        .addImage("https://cdn.hipicon.com/images/Blog/2021/08/10/1628605408_1.jpeg")
                );

            }
        };

        if (productRepository.count() == 0) {
            productRepository.saveAll(products);
            logger.info("Products Seeded");
        } else {
            logger.info("Products Seeding Not Required");
        }


    }


}
