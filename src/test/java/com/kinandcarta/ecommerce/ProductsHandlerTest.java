package com.kinandcarta.ecommerce;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;

class ProductsHandlerTest {
    TestEntityManager entityManager = Mockito.mock(TestEntityManager.class);

    ProductsRepository productsRepository = Mockito.mock(ProductsRepository.class);

    ProductsHandler productsHandler;
    
    Products initialProductOffer = Products.builder()
            .id(19L)
            .name("Product Widget 1")
            .productDescription("Relax in style with a custom lounge chair widget...")
            .unitPrice(new BigDecimal("90"))
            .image("gs://bucket-cloud/widget-1.png")
            .build();
    Products initialProductOfferTwo = Products.builder()
            .id(19L)
            .name("Product Widget 1II")
            .productDescription("Relax in style with a custom lounge chair widget, VERSION 2...")
            .unitPrice(new BigDecimal("120"))
            .image("gs://bucket-cloud/widget-2-newimproved.png")
            .build();


    @BeforeEach
    void setUp() {
        productsHandler = new ProductsHandler(productsRepository);
        entityManager.persist(initialProductOffer);
    }

    @AfterEach void tearDown() { entityManager.flush(); }

    @Test
    void shouldCreate_find_andUpdateProduct() {
        // create the product
        when(productsRepository.save(initialProductOffer)).thenReturn(initialProductOffer);
        // does it exist?
        when(productsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        // find product by id
        when(productsRepository.getReferenceById(1L)).thenReturn(initialProductOffer);

        Products update = productsHandler.update(1L, initialProductOffer);
        update.setProductDescription("Let's change the description.");
        update.setUnitPrice(new BigDecimal("102.00"));
        assertThat(update).isNotNull();
    }

    @Test void shouldDeleteProduct() {
        when (productsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        willDoNothing().given(productsRepository).deleteById(1L);
        productsHandler.delete(1L);
        assertThat(productsHandler.findById(1L)).isNull();
    }

    @Test void shouldFindOrder_byId() {
        when (productsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        when (productsRepository.getReferenceById(1L)).thenReturn(initialProductOffer);
        Products productNotTooBad = productsHandler.findById(1L);
        assertThat(productNotTooBad).isNotNull();
        assertThat(productNotTooBad.getName()).isEqualTo(initialProductOffer.getName());
    }

    @Test void shouldFindAll_Products() {
        when(productsRepository.findAll()).thenReturn(List.of(initialProductOffer));
        Set<Products> findAllProducts = productsHandler.findAll();
        assertThat(findAllProducts).isNotEmpty();
    }
    
}
