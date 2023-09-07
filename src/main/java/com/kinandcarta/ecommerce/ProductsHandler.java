package com.kinandcarta.ecommerce;

import jakarta.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
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
        if (StringUtils.isEmpty(model.getName()) || StringUtils.isEmpty(model.getProductDescription()) || model.getUnitPrice() == null)
            throw new MissingProductInformationException("Name, description and unit price required to create Product.");

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
            throw new ProductNotFoundException("findbyId - not found by id - " + id);
        }
        return productsRepository.getReferenceById(id);
    }

    @Override
    public Set<Products> findAll() {
        return new HashSet<>(productsRepository.findAll());
    }
}
