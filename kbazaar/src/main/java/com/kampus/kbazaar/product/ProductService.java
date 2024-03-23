package com.kampus.kbazaar.product;

import com.kampus.kbazaar.exceptions.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream().map(Product::toResponse).toList();
    }

    public ProductResponse getBySku(String sku) {
        Optional<Product> product = productRepository.findBySku(sku);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        return product.get().toResponse();
    }

    public Product getProductBySku(String sku) {
        Optional<Product> product = productRepository.findBySku(sku);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found");
        }
        return product.get();
    }

    public List<ProductResponse> findByNameContaining(String name, Pageable pageable) {
        Page<Product> byNameContaining = productRepository.findByNameContaining(name, pageable);
        List<Product> products =
                byNameContaining.hasContent()
                        ? byNameContaining.getContent()
                        : Collections.emptyList();
        List<ProductResponse> collect =
                products.stream()
                        .map(
                                p ->
                                        new ProductResponse(
                                                p.getId(),
                                                p.getName(),
                                                p.getSku(),
                                                p.getPrice(),
                                                p.getQuantity()))
                        .collect(Collectors.toList());
        return collect;
    }
}
