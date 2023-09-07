package com.kinandcarta.ecommerce;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProductsControllerTest {
    Products products = Products.builder()
            .id(1L)
            .image("gs://commerce-bucket/mouse-pad-v.png")
            .name("MoonBase Mouse Pad - Violet Color")
            .productDescription("Sensational mouse pad with reflective surface in various colors.")
            .unitPrice(new BigDecimal("12.49"))
            .build();

    Products productsUnknown = Products.builder()
            .id(1L)
            .image("gs://commerce-bucket/.png")
            .name(null)
            .productDescription(null)
            .unitPrice(new BigDecimal(".9"))
            .build();

    @Mock
    ProductsHandler productsHandler;

    ProductsController controller;

    @BeforeEach void setUp() {
        controller = new ProductsController(productsHandler);
    }

    @Test void should_Create_NewProduct() {
        when(productsHandler.create(products)).thenReturn(products);
        ResponseEntity<Products> productCreateCmd = controller.create(products);
        assertThat(productCreateCmd).isNotNull();
        assertThat(productCreateCmd.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(productCreateCmd.getBody()).isNotNull();
        Products mousePad = productCreateCmd.getBody();
        assertThat(mousePad).hasFieldOrPropertyWithValue("image", "gs://commerce-bucket/mouse-pad-v.png");
    }

    @Test void shouldNot_CreateProduct_WithMissingInformation() {
        when(productsHandler.create(productsUnknown)).thenThrow(MissingProductInformationException.class);
        ResponseEntity<Products> productCreateCmd = controller.create(productsUnknown);
        assertThat(productCreateCmd).isNotNull();
        assertThat(productCreateCmd.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test void should_UpdateProduct() {
        when(productsHandler.update(1L, products)).thenReturn(products);
        ResponseEntity<Products> productUpdateCommand = controller.update(1L, products);
        assertThat(productUpdateCommand).isNotNull();
        assertThat(productUpdateCommand.getBody()).isNotNull();
        Products updatingThisProduct = productUpdateCommand.getBody();
        updatingThisProduct.setProductDescription("Follow along with the popup game, devise a neat strategy to get your pieces 4 in a row first!");
        updatingThisProduct.setName("Scrum PopUp Themed Board Game");
        updatingThisProduct.setUnitPrice(new BigDecimal("21.99"));
        assertThat(products.getName()).isEqualTo(updatingThisProduct.getName());
        assertThat(products.getUnitPrice()).isEqualByComparingTo(updatingThisProduct.getUnitPrice());
    }

    @Test void shouldDeleteProduct() {
        when(productsHandler.create(products)).thenReturn(products);
        doNothing().when(productsHandler).delete(products.getId());
        ResponseEntity<Products> productCreateCmd = controller.create(products);
        assertThat(productCreateCmd).isNotNull();
        controller.delete(1L);

        verify(productsHandler, times(1)).delete(products.getId());
    }

    @Test void shouldFindAll_Products() {
        when(productsHandler.findAll()).thenReturn(Set.of(products));
        assertThat(controller.findAll()).isNotNull();
        assertThat(controller.findAll().getBody()).isNotNull();
        Set<Products> productsRetrieved = controller.findAll().getBody();
        assertThat(productsRetrieved).isNotNull()
                .contains(products);
    }

    @Test void shouldReturnEmptySet_whenNoProductsFound() {
        when(productsHandler.findAll()).thenReturn(new HashSet<>());
        assertThat(controller.findAll()).isNotNull();
        assertThat(controller.findAll().getBody()).isEmpty();
    }

    @Test void shouldNot_FindOne_Product() {
        when(productsHandler.findById(1L)).thenThrow(ProductNotFoundException.class);
        assertThat(controller.findById(1L).getStatusCode()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test void shouldFindOne_Product() {
        when(productsHandler.findById(1L)).thenReturn(products);
        assertThat(controller.findById(1L).getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(controller.findById(1L).getBody()).isNotNull();
        Products productFindById = controller.findById(1L).getBody();
        assertThat(productFindById)
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("unitPrice");
    }
}
