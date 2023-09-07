package com.kinandcarta.ecommerce;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@Slf4j
public class ProductsController implements CrudUseCase<Products> {
    private final ProductsHandler productsHandler;

    public ProductsController(ProductsHandler productsHandler) {
        this.productsHandler = productsHandler;
    }

    @Override
    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Products> create(@RequestBody Products model) {
        try {
            return new ResponseEntity<>(productsHandler.create(model), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof MissingProductInformationException) {
                log.error("::METHOD, create, MissingProductInformationException.");
                return ResponseEntity.badRequest().build();
            }
            log.error("::METHOD, create, exception occured.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PutMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Products> update(@PathVariable("id") @NotNull final Long id, @RequestBody Products model) {
        try {
        return new ResponseEntity<>(productsHandler.update(id, model), HttpStatus.OK);
        } catch (final Exception e) {
            log.error("::METHOD, update, exception occured.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("id") @NotNull final Long id) {
        try {
            productsHandler.delete(id);
        } catch (final Exception e) {
            log.error("::METHOD, delete, exception occured.", e);
        }
    }

    @Override
    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Products> findById(@PathVariable("id") @NotNull final Long id) {
        try {
            return new ResponseEntity<>(productsHandler.findById(id), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof ProductNotFoundException) {
                log.error("ProductNotFoundException, for ID -> " + id);
                return ResponseEntity.badRequest().build();
            }
            log.error("::METHOD, findById, exception occured.", e);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Products>> findAll() {
        try {
            return new ResponseEntity<>(
                    Optional.ofNullable(productsHandler.findAll()).orElse(new HashSet<>()), HttpStatus.OK);
        } catch(final Exception e) {
            log.error("::METHOD, findAll, exception occured.", e);
            return ResponseEntity.notFound().build();
        }
    }
}
