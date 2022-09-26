package com.acme;


import com.acme.product.domain.ProductRepository;
import com.acme.product.domain.ProductService;
import com.acme.user.domain.UserRepository;
import com.acme.user.domain.UserService;
import com.acme.vendingmachine.domain.VendingMachineAccountRepository;
import com.acme.vendingmachine.domain.VendingMachineAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;

@Configuration
@EnableJpaAuditing
class VendingMachineApplicationConfiguration {


    @Bean
    public VendingMachineAccountService vendingMachineAccountService(VendingMachineAccountRepository vendingMachineAccountRepository) {
        return new VendingMachineAccountService(vendingMachineAccountRepository);
    }

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }


    @Bean
    public Clock defaultClock() {
        return Clock.systemDefaultZone();
    }
}
