package com.kinandcarta.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductsController.class)
@Import(ProductsHandler.class)
class ProductsWebIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductsRepository productsRepository;

    ObjectMapper mapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateANewProduct() throws Exception {
        final String json = mapper.writeValueAsString(product());
        whenConditionsFor_CreateProductById(product());
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((json)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test void shouldUpdateAProduct() throws Exception {
        final String json = mapper.writeValueAsString(Products.builder()
                .id(1L)
                .name("Product Number Four")
                .productDescription("This is product four")
                .unitPrice(new BigDecimal("123.49"))
                .image("gs://bucket-cloud/widget-4.png")
                .build());

        whenConditionsFor_FindProductById(Products.builder()
                .id(1L)
                .name("Product Number Four")
                .unitPrice(new BigDecimal("123.49"))
                .image("gs://bucket-cloud/widget-4.png")
                .build());

        mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content((json)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Products product() {
        return Products.builder()
                .id(1L)
                .name("Product Widget 1II")
                .productDescription("Relax in style with a custom lounge chair widget, VERSION 2...")
                .unitPrice(new BigDecimal("120"))
                .image("gs://bucket-cloud/widget-2-newimproved.png")
                .build();
    }

    private void whenConditionsFor_FindProductById(final Products productsFind) {
        when (productsRepository.save(productsFind)).thenReturn(productsFind);
        when (productsRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        when (productsRepository.getReferenceById(productsFind.getId())).thenReturn(productsFind);
    }

    private void whenConditionsFor_CreateProductById(final Products productsFind) {
        when (productsRepository.save(productsFind)).thenReturn(productsFind);
        when (productsRepository.getReferenceById(productsFind.getId())).thenReturn(productsFind);
    }
}
