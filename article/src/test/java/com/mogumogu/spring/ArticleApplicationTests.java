package com.mogumogu.spring;

import com.mogumogu.spring.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootTest
class ArticleApplicationTests {

    @Test
    void contextLoads() {
    }

}
