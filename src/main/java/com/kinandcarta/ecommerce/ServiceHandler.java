package com.kinandcarta.ecommerce;


import java.util.Set;

public interface ServiceHandler {
    Products create(final Products model);
    Products update(final Long id, final Products model);
    void delete(final Long id);
    Products findById(final Long id);

    Set<Products> findAll();

}
