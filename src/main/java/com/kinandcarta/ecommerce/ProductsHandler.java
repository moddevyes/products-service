package com.kinandcarta.ecommerce;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductsHandler implements ServiceHandler {

    private final ProductsRepository productsRepository;

    public ProductsHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    @Transactional
    public Products create(final Products model) {
        return productsRepository.save(model);
    }

    @Override
    @Transactional
    public Products update(final Long id, final Products model) {
        Products product = findById(id);
        product.setName(model.getName());
        product.setProductDescription(model.getProductDescription());
        product.setImage(model.getImage());
        product.setUnitPrice(model.getUnitPrice());
        return productsRepository.save(product);
    }

    @Override
    public void delete(final Long id) {
        productsRepository.deleteById(id);
    }

    @Override
    public Products findById(final Long id) {
        if (!productsRepository.existsById(id)) {
            throw new EntityNotFoundException("findbyId - not found by id - " + id);
        }
        return productsRepository.getReferenceById(id);
    }

    @Override
    public Set<Products> findAll() {
        return new HashSet<>(productsRepository.findAll());
    }
}
